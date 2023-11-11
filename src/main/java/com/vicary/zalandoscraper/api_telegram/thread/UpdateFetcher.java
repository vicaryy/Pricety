package com.vicary.zalandoscraper.api_telegram.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.vicary.zalandoscraper.api_telegram.api_object.UpdateResponse;
import com.vicary.zalandoscraper.api_telegram.api_object.Update;
import com.vicary.zalandoscraper.api_telegram.end_point.EndPoint;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateFetcher {
    private static final Logger logger = LoggerFactory.getLogger(UpdateFetcher.class);
    private static final int BREAK_BEFORE_START = 1000; // milliseconds
    private static final int TRYING_TO_RECONNECT_DELAY = 4000; // milliseconds
    private static final int EXECUTING_THREADS_DELAY = 150; // milliseconds
    private static int UPDATES_DELAY = 1000; // milliseconds
    private static int FAST_MODE_DELAY = 1000;
    private static int SLOW_MODE_DELAY = 6000;
    private static final int MAX_UPDATES_SIZE = 6;
    private static String botToken;
    private static String fetchURL;
    private static String deleteURL;
    private long fastModeDuration;
    private static int FAST_MODE_TIMEOUT = 15000;
    private Mode mode = Mode.FAST;
    private List<Update> updates;
    private final WebClient client = WebClient.create();
    private final ExecutorService cachedExecutor = Executors.newCachedThreadPool();
    private final UpdateReceiver updateReceiver;
    private static UpdateFetcher INSTANCE;

    private UpdateFetcher(UpdateReceiver updateReceiver) {
        this.updateReceiver = updateReceiver;

        logger.info("Telegram Bot API started successfully.");
        new Thread(this::run).start();
    }

    public static UpdateFetcher getInstance(UpdateReceiver updateReceiver) {
        if (INSTANCE == null)
            INSTANCE = new UpdateFetcher(updateReceiver);
        return INSTANCE;
    }

    private void run() {
        configBotInfo();
        sleep(BREAK_BEFORE_START);
        while (!Thread.currentThread().isInterrupted()) {
            try {
                updates = getUpdates();
            } catch (WebClientResponseException ex) {
                handleWebClientResponseException(ex);
                break;
            } catch (WebClientRequestException ex) {
                handleWebClientRequestException();
            }
            checkUpdatesDelay();
            executeUpdates();
            sleep(UPDATES_DELAY);
        }
    }

    private void configBotInfo() {
        botToken = updateReceiver.botToken();
        fetchURL = "https://api.telegram.org/bot" + botToken + EndPoint.GET_UPDATES.getPath();
        deleteURL = "https://api.telegram.org/bot" + botToken + EndPoint.GET_UPDATES_OFFSET.getPath();
    }

    public static void setFastModeDelay(int delay) {
        FAST_MODE_DELAY = delay;
    }

    public static void setSlowModeDelay(int delay) {
        SLOW_MODE_DELAY = delay;
    }

    public static int getSlowModeDelay() {
        return SLOW_MODE_DELAY;
    }

    public static int getFastModeDelay() {
        return FAST_MODE_DELAY;
    }

    public static void setFastModeTimeout(int timeout) {
        FAST_MODE_TIMEOUT = timeout;
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
        if (!updates.isEmpty() && updates.size() < MAX_UPDATES_SIZE)
            for (Update update : updates) {
                cachedExecutor.execute(() -> updateReceiver.receive(update));
                sleep(EXECUTING_THREADS_DELAY);
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
            UPDATES_DELAY = SLOW_MODE_DELAY;
        else if (mode == Mode.FAST)
            UPDATES_DELAY = FAST_MODE_DELAY;
    }



    private void checkUpdatesDelay() {
        if (!updates.isEmpty() && mode == Mode.FAST)
            resetFastModeDuration();

        else if (!updates.isEmpty() && mode == Mode.SLOW) {
            setMode(Mode.FAST);
            resetFastModeDuration();
            logger.debug("[Update Polling Thread] Fast Mode Active");
        } else if (updates.isEmpty() && mode == Mode.FAST) {
            if (fastModeDuration == 0)
                startFastModeDuration();

            if (System.currentTimeMillis() - fastModeDuration > FAST_MODE_TIMEOUT) {
                setMode(Mode.SLOW);
                resetFastModeDuration();
                logger.debug("[Update Polling Thread] Slow Mode Active");
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
        logger.error("Status code: " + ex.getStatusCode());
        logger.error("Description: " + ex.getStatusText());
        logger.error("Check your bot token etc. and try again.");
        logger.error("---------------------------");
    }

    private void handleWebClientRequestException() {
        logger.warn("---------------------------");
        logger.warn("Can't connect to Telegram, check your internet connection.");
        logger.warn("Trying to reconnect...");
        logger.warn("---------------------------");
        sleep(TRYING_TO_RECONNECT_DELAY);
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
        }
    }
}
