package com.vicary.zalandoscraper.api_telegram.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.vicary.zalandoscraper.api_telegram.api_object.UpdateResponse;
import com.vicary.zalandoscraper.api_telegram.api_object.Update;
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
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private Mode mode = Mode.FAST;
    private List<Update> updates = new ArrayList<>();
    private final Thread thread;
    private final WebClient client = WebClient.create();
    private final ExecutorService cachedExecutor = Executors.newCachedThreadPool();
    private final UpdateReceiver updateReceiver;
    private FetcherOptions options;

    public UpdateFetcher(UpdateReceiver updateReceiver) {
        this.updateReceiver = updateReceiver;
        this.options = new FetcherOptions();

        logger.info("[Api Telegram] Telegram Bot API started successfully.");
        thread = new Thread(this::run);
        thread.start();
    }

    public UpdateFetcher(UpdateReceiver updateReceiver, FetcherOptions options) {
        this.updateReceiver = updateReceiver;
        this.options = options;

        logger.info("[Api Telegram] Telegram Bot API started successfully.");
        thread = new Thread(this::run);
        thread.start();
    }

    private void run() {
        isRunning.set(true);
        configBotInfo();
        sleep(options.getBreakBeforeStart());
        while (isRunning.get()) {
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
        botToken = updateReceiver.botToken();
        fetchURL = "https://api.telegram.org/bot" + botToken + EndPoint.GET_UPDATES.getPath();
        deleteURL = "https://api.telegram.org/bot" + botToken + EndPoint.GET_UPDATES_OFFSET.getPath();
    }

    public FetcherOptions getOptions() {
        return options;
    }

    public void setOptions(FetcherOptions options) {
        this.options = options;
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
        logger.error("---------------------------");
        logger.error("Telegram API Bot stopped.");
        logger.error("Status code: " + ex.getStatusCode());
        logger.error("Description: " + ex.getStatusText());
        logger.error("Check your bot token etc. and try again.");
        logger.error("---------------------------");
        isRunning.set(false);
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
        logger.error("Unexpected error: " + ex.getMessage());
        ex.printStackTrace();
        isRunning.set(false);
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
        }
    }
}
