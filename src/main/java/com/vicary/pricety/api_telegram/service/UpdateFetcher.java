package com.vicary.pricety.api_telegram.service;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.vicary.pricety.api_telegram.api_object.UpdateResponse;
import com.vicary.pricety.api_telegram.api_object.Update;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class UpdateFetcher {
    private static final Logger logger = LoggerFactory.getLogger(UpdateFetcher.class);
    private static String botToken;
    private static String fetchURL;
    private static String deleteURL;
    private long fastModeDuration;
    private final AtomicBoolean fetcherRunning = new AtomicBoolean(true);
    private Mode mode = Mode.FAST;
    private List<Update> updates = new ArrayList<>();
    private Thread thread;
    private final WebClient client = WebClient.create();
    private final ExecutorService cachedExecutor = Executors.newCachedThreadPool();
    private UpdateReceiver updateReceiver;
    @Setter
    @Getter
    private FetcherOptions options;
    private static UpdateFetcher updateFetcher;

    private UpdateFetcher(){}

    private UpdateFetcher(UpdateReceiver updateReceiver) {
        this.updateReceiver = updateReceiver;
        this.options = new FetcherOptions();
    }

    private UpdateFetcher(UpdateReceiver updateReceiver, FetcherOptions options) {
        this.updateReceiver = updateReceiver;
        this.options = options;
    }

    public static UpdateFetcher getInstance() {
        return updateFetcher;
    }

    public static UpdateFetcher getInstance(UpdateReceiver updateReceiver) {
        if (updateFetcher == null) {
            updateFetcher = new UpdateFetcher(updateReceiver);
        }
        return updateFetcher;
    }

    public static UpdateFetcher getInstance(UpdateReceiver updateReceiver, FetcherOptions options) {
        if (updateFetcher == null) {
            updateFetcher = new UpdateFetcher(updateReceiver, options);
        }
        return updateFetcher;
    }

    public void start() {
        logger.info("[Api Telegram] Telegram Bot API started successfully.");
        thread = new Thread(this::run);
        thread.start();
    }

    private void run() {
        configBotInfo();
        sleep(options.getBreakBeforeStart());
        while (fetcherRunning.get()) {
            try {
                updates = getUpdates();
            } catch (WebClientResponseException ex) {
                handleWebClientResponseException(ex);
            } catch (WebClientRequestException ex) {
                handleWebClientRequestException();
            } catch (Exception ex) {
                handleException(ex);
            }
            checkUpdatesDelay();
            executeUpdates();
            sleep(options.getUpdateFrequency());
        }
    }

    private void configBotInfo() {
        while (botToken == null) {
            botToken = updateReceiver.botToken();
            if (botToken == null)
                logger.warn("[Api Telegram] Can't fetch bot token, trying again...");
            else
                logger.info("[Api Telegram] Bot token implemented successfully.");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            }
        }

        fetchURL = "https://api.telegram.org/bot" + botToken + EndPoint.GET_UPDATES.getPath();
        deleteURL = "https://api.telegram.org/bot" + botToken + EndPoint.GET_UPDATES_OFFSET.getPath();
    }

    public void setReceiverRunning(boolean isRunning) {
        updateReceiver.setRunning(isRunning);
    }

    public boolean isReceiverRunning() {
        return updateReceiver.isRunning();
    }

    static String getBotToken() {
        return botToken;
    }

    private List<Update> getUpdates() throws WebClientResponseException, WebClientRequestException {
        UpdateResponse<Update> response = sendGetUpdatesRequest();

        if (isResponseEmpty(response))
            return Collections.emptyList();

        sendDeleteRequest(getOffset(response));

        return response.getResult();
    }

    private void cleanAwaitedUpdates() throws WebClientResponseException, WebClientRequestException {
        UpdateResponse<Update> response = sendGetUpdatesRequest();

        if (isResponseEmpty(response))
            return;

        sendDeleteRequest(getOffset(response));
    }

    private void executeUpdates() {
        if (!updates.isEmpty() && updates.size() < options.getMaxSizeOfUpdates())
            for (Update update : updates) {
                cachedExecutor.execute(() -> updateReceiver.receive(update));
                sleep(options.getExecutingThreadsDelay());
            }
    }

    private UpdateResponse<Update> sendGetUpdatesRequest() {
        return client
                .get()
                .uri(fetchURL)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<UpdateResponse<Update>>() {
                })
                .block();
    }

    private void sendDeleteRequest(int offset) {
        client.get()
                .uri(deleteURL + offset)
                .retrieve()
                .bodyToMono(UpdateResponse.class)
                .block();
    }

    private int getOffset(UpdateResponse<Update> response) {
        return (response.getResult().get(response.getResult().size() - 1)).getUpdateId() + 1;
    }

    private boolean isResponseEmpty(UpdateResponse<Update> response) {
        return response == null || response.getResult().isEmpty();
    }

    private void setMode(Mode mode) {
        this.mode = mode;
        if (mode == Mode.SLOW)
            options.setUpdateFrequency(options.getSlowModeUpdatesFrequency());
        else if (mode == Mode.FAST)
            options.setUpdateFrequency(options.getFastModeUpdatesFrequency());
    }


    private void checkUpdatesDelay() {
        if (!updates.isEmpty() && mode == Mode.FAST)
            resetFastModeDuration();

        else if (!updates.isEmpty() && mode == Mode.SLOW) {
            setMode(Mode.FAST);
            resetFastModeDuration();
            logger.debug("[Update Fetcher] Fast Mode Active");
        } else if (updates.isEmpty() && mode == Mode.FAST) {
            if (fastModeDuration == 0)
                startFastModeDuration();

            if (System.currentTimeMillis() - fastModeDuration > options.getFastModeTimeout()) {
                setMode(Mode.SLOW);
                resetFastModeDuration();
                logger.debug("[Update Fetcher] Slow Mode Active");
            }
        }
    }

    private void resetFastModeDuration() {
        fastModeDuration = 0;
    }

    private void startFastModeDuration() {
        fastModeDuration = System.currentTimeMillis();
    }

    private void handleWebClientResponseException(WebClientResponseException ex) {
        //TODO Status code: 429 TOO_MANY_REQUESTS
        //     Description: Too Many Requests
        logger.error("---------------------------");
        logger.error("Telegram API Bot stopped.");
        logger.error("Status code: {}", ex.getStatusCode());
        logger.error("Description: {}", ex.getStatusText());
        logger.error("Check your bot token etc. and try again.");
        logger.error("---------------------------");
        fetcherRunning.set(false);
    }

    private void handleWebClientRequestException() {
        logger.warn("---------------------------");
        logger.warn("Can't connect to Telegram, check your internet connection.");
        logger.warn("Trying to reconnect...");
        logger.warn("---------------------------");
        sleep(options.getTryingToReconnectFrequency());
    }

    private void handleException(Exception ex) {
        logger.error("Telegram API Bot stopped.");
        logger.error("Unexpected error: {}", ex.getMessage());
        fetcherRunning.set(false);
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
        }
    }
}
