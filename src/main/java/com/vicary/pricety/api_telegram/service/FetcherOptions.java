package com.vicary.pricety.api_telegram.service;

public class FetcherOptions {
    private final int breakBeforeStart;
    private final int tryingToReconnectFrequency;
    private int executingThreadsDelay;
    private int updateFrequency;
    private int fastModeUpdatesFrequency;
    private int slowModeUpdatesFrequency;
    private int fastModeTimeout;
    private int maxSizeOfUpdates;

    public FetcherOptions() {
        breakBeforeStart = 1000;
        tryingToReconnectFrequency = 8000;
        executingThreadsDelay = 150;
        updateFrequency = 1000;
        fastModeUpdatesFrequency = 1000;
        slowModeUpdatesFrequency = 6000;
        fastModeTimeout = 50000;
        maxSizeOfUpdates = 6;
    }

    public int getExecutingThreadsDelay() {
        return executingThreadsDelay;
    }

    public void setExecutingThreadsDelay(int millis) {
        this.executingThreadsDelay = millis;
    }

    public int getUpdateFrequency() {
        return updateFrequency;
    }

    public void setUpdateFrequency(int millis) {
        this.updateFrequency = millis;
    }

    public int getFastModeUpdatesFrequency() {
        return fastModeUpdatesFrequency;
    }

    public void setFastModeUpdatesFrequency(int millis) {
        this.fastModeUpdatesFrequency = millis;
    }

    public int getSlowModeUpdatesFrequency() {
        return slowModeUpdatesFrequency;
    }

    public void setSlowModeUpdatesFrequency(int millis) {
        this.slowModeUpdatesFrequency = millis;
    }

    public int getFastModeTimeout() {
        return fastModeTimeout;
    }

    public void setFastModeTimeout(int millis) {
        this.fastModeTimeout = millis;
    }

    public int getMaxSizeOfUpdates() {
        return maxSizeOfUpdates;
    }

    public void setMaxSizeOfUpdates(int millis) {
        this.maxSizeOfUpdates = millis;
    }

    public int getBreakBeforeStart() {
        return breakBeforeStart;
    }

    public int getTryingToReconnectFrequency() {
        return tryingToReconnectFrequency;
    }
}
