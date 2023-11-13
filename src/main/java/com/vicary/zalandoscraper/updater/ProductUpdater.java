package com.vicary.zalandoscraper.updater;

import com.vicary.zalandoscraper.exception.TimeoutException;
import com.vicary.zalandoscraper.service.Scraper;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class ProductUpdater {

    private final static Logger logger = LoggerFactory.getLogger(ProductUpdater.class);
    private long updatesTimeout;
    private int amountOfThreads;
    private static ProductUpdater INSTANCE;
    private final Scraper scraper = Scraper.getInstance();
    private final List<Future<?>> activeThreads = new ArrayList<>();
    private final AtomicInteger completedThreads = new AtomicInteger();


    private ProductUpdater() {
        setDefaultAmountOfThreads();
    }

    public static ProductUpdater getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ProductUpdater();

        return INSTANCE;
    }


    public void update(List<ProductDTO> DTOs) {
        logger.info("[Product Updater] Starting updating '{}' products", DTOs.size());
        final long startingTime = System.currentTimeMillis();

        executeThreads(DTOs);

        setUpdatesTimeout(DTOs.size());

        waitUntilThreadsEndsAndCheckTimeout();

        logger.info("[Product Updater] Products updated successfully, it takes {} seconds", (System.currentTimeMillis() - startingTime) / 1000);
    }

    private void executeThreads(List<ProductDTO> DTOs) {
        final ExecutorService executorService = Executors.newFixedThreadPool(amountOfThreads);
        List<List<ProductDTO>> cuttedList = cutProductDTOList(DTOs);

        for (List<ProductDTO> p : cuttedList) {
            Future<?> future = executorService.submit(() -> {
                scraper.updateProducts(p);
                completedThreads.getAndIncrement();
            });
            activeThreads.add(future);
        }
    }

    private void waitUntilThreadsEndsAndCheckTimeout() {
        try {
            while (!isEveryThreadEnds()) {
                sleep(1000);
                if (isUpdateTimeouts())
                    handleUpdateTimeout();
            }
            scraper.setBugged(false);
        } finally {
            resetThreadsInfo();
        }
    }

    private void resetThreadsInfo() {
        activeThreads.clear();
        completedThreads.set(0);
    }

    private void handleUpdateTimeout() {
        scraper.setBugged(true);
        throw new TimeoutException();
    }

    private boolean isUpdateTimeouts() {
        return System.currentTimeMillis() > updatesTimeout;
    }

    private boolean isEveryThreadEnds() {
        return completedThreads.get() == activeThreads.size();
    }

    private List<List<ProductDTO>> cutProductDTOList(List<ProductDTO> DTOs) {
        List<List<ProductDTO>> cuttedList = new ArrayList<>();

        for (int i = 0; i < amountOfThreads; i++) {
            if (i == 0)
                cuttedList.add(DTOs.subList(0, (DTOs.size() / amountOfThreads)));
            else if (i + 1 == amountOfThreads)
                cuttedList.add(DTOs.subList((DTOs.size() / amountOfThreads) * (i), (DTOs.size())));
            else
                cuttedList.add(DTOs.subList((DTOs.size() / amountOfThreads) * (i), (DTOs.size() / amountOfThreads) * (i + 1)));
        }
        return cuttedList;
    }

    private void setUpdatesTimeout(int amountOfProducts) {
        updatesTimeout = System.currentTimeMillis() + (long) amountOfProducts * 2000;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isRunning() {
        return !activeThreads.isEmpty();
    }

    public void setDefaultAmountOfThreads() {
        amountOfThreads = 2;
    }

    public void setAmountOfThreads(int amount) {
        amountOfThreads = amount;
    }

    public int getAmountOfThreads() {
        return amountOfThreads;
    }
}
