package com.vicary.zalandoscraper.updater;

import com.vicary.zalandoscraper.exception.ScraperBotException;

public class RunningOnceState extends UpdaterState {
    public RunningOnceState(AutoUpdater autoUpdater) {
        super(autoUpdater);
    }

    @Override
    public void start() {
        throw new ScraperBotException(
                "Auto Updater Once is running already.\n\nTry to stop running thread first.",
                "Admin tried to start Auto Updater but Auto Updater Once is running already.");
    }

    @Override
    public void startOnce() {
        throw new ScraperBotException(
                "Auto Updater Once is running already.",
                "Admin tried to start Auto Updater Once but it is running already.");
    }

    @Override
    public void stop() {
        throw new ScraperBotException(
                "Fail to stop Auto Update Once.\n\nWait till it is over.",
                "Admin tried to stop Auto Update Once.");
    }

    @Override
    public boolean isUpdating() {
        return true;
    }

    @Override
    public String getState() {
        return "Running Once";
    }
}
