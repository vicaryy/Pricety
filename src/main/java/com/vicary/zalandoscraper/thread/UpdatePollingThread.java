package com.vicary.zalandoscraper.thread;

import com.vicary.zalandoscraper.configuration.BotInfo;
import com.vicary.zalandoscraper.service.UpdateReceiverService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.vicary.zalandoscraper.api_object.UpdateResponse;
import com.vicary.zalandoscraper.api_object.Update;
import com.vicary.zalandoscraper.end_point.EndPoint;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class UpdatePollingThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(UpdatePollingThread.class);

    private final UpdateReceiverService updateReceiverService;

//    private final ActiveRequestService activeRequestService;

    private final WebClient client = WebClient.create();
    private final ExecutorService cachedExecutor = Executors.newCachedThreadPool();
    private final ExecutorService singleExecutor = Executors.newSingleThreadExecutor();
    private List<Update> updates;
    private static final int BREAK_BEFORE_START = 1000; // milliseconds
    private static final int TRYING_TO_RECONNECT_DELAY = 4000; // milliseconds
    private static final int EXECUTING_THREADS_DELAY = 100; // milliseconds
    private static final int GET_UPDATES_DELAY = 1500; // milliseconds
    private static final int MAX_UPDATES_SIZE = 6;

    @PostConstruct
    private void startThread() {
        logger.info("Telegram Bot API started successfully.");
        singleExecutor.execute(this);
    }

    @Override
    public void run() {
        sleep(BREAK_BEFORE_START);
//        activeRequestService.deleteAllActiveUsers();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                updates = getUpdates();
            } catch (WebClientResponseException ex) {
                handleWebClientResponseException(ex);
                break;
            } catch (WebClientRequestException ex) {
                handleWebClientRequestException();
            }
            executeUpdates();
            sleep(GET_UPDATES_DELAY);
        }
    }

    public void executeUpdates() {
        if (updates != null && updates.size() < MAX_UPDATES_SIZE)
            for (Update update : updates) {
                cachedExecutor.execute(() -> updateReceiverService.updateReceiver(update));
                sleep(EXECUTING_THREADS_DELAY);
            }
    }

    public void handleWebClientResponseException(WebClientResponseException ex) {
        logger.error("---------------------------");
        logger.error("Status code: " + ex.getStatusCode());
        logger.error("Description: " + ex.getStatusText());
        logger.error("Check your bot token etc. and try again.");
        logger.error("---------------------------");
    }

    public void handleWebClientRequestException() {
        logger.warn("---------------------------");
        logger.warn("Can't connect to Telegram, check your internet connection.");
        logger.warn("Trying to reconnect...");
        logger.warn("---------------------------");
        sleep(TRYING_TO_RECONNECT_DELAY);
    }

    public void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
        }
    }

    public List<Update> getUpdates() throws WebClientResponseException, WebClientRequestException {
        String pollUrl = BotInfo.getURL() + EndPoint.GET_UPDATES.getPath();

        UpdateResponse<Update> response = client
                .get()
                .uri(pollUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<UpdateResponse<Update>>() {
                })
                .block();

        if (response == null || response.getResult().isEmpty())
            return null;

        int offset = response.getResult().get(response.getResult().size() - 1).getUpdateId() + 1;

        String deletePollUrl = BotInfo.getURL() + EndPoint.GET_UPDATES_OFFSET.getPath() + offset;


        client.get()
                .uri(deletePollUrl)
                .retrieve()
                .bodyToMono(UpdateResponse.class)
                .block();
        return response.getResult();
    }

    public void resetUpdates() throws WebClientResponseException, WebClientRequestException {
        String pollUrl = BotInfo.getURL() + EndPoint.GET_UPDATES.getPath();

        UpdateResponse<Update> response = client
                .get()
                .uri(pollUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<UpdateResponse<Update>>() {
                })
                .block();

        if (response == null || response.getResult().isEmpty())
            return;

        int offset = (response.getResult().get(response.getResult().size() - 1)).getUpdateId() + 1;

        String deletePollUrl = BotInfo.getURL() + EndPoint.GET_UPDATES_OFFSET.getPath() + offset;

        client.get()
                .uri(deletePollUrl)
                .retrieve()
                .bodyToMono(UpdateResponse.class)
                .block();
    }
}
