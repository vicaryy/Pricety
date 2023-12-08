package com.vicary.zalandoscraper.updater;

import com.vicary.zalandoscraper.exception.ZalandoScraperBotException;

public class RunningOnceState extends UpdaterState {
    public RunningOnceState(AutoUpdater autoUpdater) {
        super(autoUpdater);
    }

    @Override
    public void start() {
        throw new ZalandoScraperBotException(
                "Auto Updater Once is running already.\n\nTry to stop running thread first.",
                "Admin tried to start Auto Updater but Auto Updater Once is running already.");
    }

    @Override
    public void startOnce() {
        throw new ZalandoScraperBotException(
                "Auto Updater Once is running already.",
                "Admin tried to start Auto Updater Once but it is running already.");
    }

    @Override
    public void stop() {
        throw new ZalandoScraperBotException(
                "Fail to stop Auto Update Once.\n\nWait till it is over.",
                "Admin tried to stop Auto Update Once.");
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    @Override
    public String getState() {
        return "Running Once";
    }
}
