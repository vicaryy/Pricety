package com.vicary.pricety.updater;

import com.vicary.pricety.exception.ScraperBotException;

public class RunningState extends UpdaterState {
    public RunningState(AutoUpdater autoUpdater) {
        super(autoUpdater);
    }

    @Override
    public void start() {
        throw new ScraperBotException(
                "Auto Updater is running already.",
                "Admin tried to start Auto Updater but it is running already.");
    }

    @Override
    public void startOnce() {
        throw new ScraperBotException(
                "Auto Updater is running already.\n\nTry to stop running thread first.",
                "Admin tried to start Auto Updater Once but Auto Updater is running already.");
    }

    @Override
    public void stop() {
        autoUpdater.getRunningThread().interrupt();
        autoUpdater.setState(new StopState(autoUpdater));
    }

    @Override
    public boolean isUpdating() {
        return false;
    }

    @Override
    public String getState() {
        return "Running";
    }
}
