package com.vicary.zalandoscraper.updater;

import com.vicary.zalandoscraper.exception.TimeoutException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.scraper.Scraper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class ProductUpdater {
    private long updatesTimeout;
    private int amountOfThreads;
    private final List<Future<?>> activeThreads = new ArrayList<>();
    private final AtomicInteger completedThreads = new AtomicInteger();
    private final List<Product> products;
    private final Scraper scraper;

    public ProductUpdater(Scraper scraper, List<Product> products) {
        this.scraper = scraper;
        this.products = products;
        setDefaultAmountOfThreads();
    }


    public void update() {
        executeThreads();

        setUpdatesTimeout();

        waitUntilThreadsEndsAndCheckTimeout();
    }

    private void executeThreads() {
        final ExecutorService executorService = Executors.newFixedThreadPool(amountOfThreads);
        List<List<Product>> cuttedList = cutProductList();

        for (List<Product> p : cuttedList) {
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

    private List<List<Product>> cutProductList() {
        List<List<Product>> cuttedList = new ArrayList<>();

        for (int i = 0; i < amountOfThreads; i++) {
            if (i == 0)
                cuttedList.add(products.subList(0, (products.size() / amountOfThreads)));
            else if (i + 1 == amountOfThreads)
                cuttedList.add(products.subList((products.size() / amountOfThreads) * (i), (products.size())));
            else
                cuttedList.add(products.subList((products.size() / amountOfThreads) * (i), (products.size() / amountOfThreads) * (i + 1)));
        }
        return cuttedList;
    }

    void setUpdatesTimeout() {
        if (products.size() < 2)
            updatesTimeout = System.currentTimeMillis() + 20_000;
        else if (products.size() < 7)
            updatesTimeout = System.currentTimeMillis() + (12_000 - (products.size() * 1000L)) * products.size();
        else
            updatesTimeout = System.currentTimeMillis() + (long) products.size() * 3500;
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

    long getUpdatesTimeout() {
        return updatesTimeout;
    }
}
