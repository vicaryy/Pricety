package com.vicary.zalandoscraper.updater;

import com.vicary.zalandoscraper.exception.TimeoutException;
import com.vicary.zalandoscraper.scraper.Scraper;
import com.vicary.zalandoscraper.scraper.ZalandoScraper;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private final List<Future<?>> activeThreads = new ArrayList<>();
    private final AtomicInteger completedThreads = new AtomicInteger();
    private final List<ProductDTO> DTOs;
    private final Scraper scraper;

    public ProductUpdater(Scraper scraper, List<ProductDTO> DTOs) {
        this.scraper = scraper;
        this.DTOs = DTOs;
        setDefaultAmountOfThreads();
    }


    public void update() {
        executeThreads();

        setUpdatesTimeout();

        waitUntilThreadsEndsAndCheckTimeout();
    }

    private void executeThreads() {
        final ExecutorService executorService = Executors.newFixedThreadPool(amountOfThreads);
        List<List<ProductDTO>> cuttedList = cutProductDTOList();

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
                    throw new TimeoutException();
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

    private boolean isUpdateTimeouts() {
        return System.currentTimeMillis() > updatesTimeout;
    }

    private boolean isEveryThreadEnds() {
        return completedThreads.get() == activeThreads.size();
    }

    private List<List<ProductDTO>> cutProductDTOList() {
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

    private void setUpdatesTimeout() {
        updatesTimeout = System.currentTimeMillis() + (long) DTOs.size() * 2000;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
