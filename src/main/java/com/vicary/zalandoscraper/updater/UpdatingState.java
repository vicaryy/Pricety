package com.vicary.zalandoscraper.updater;

import com.vicary.zalandoscraper.exception.ZalandoScraperBotException;

public class UpdatingState extends UpdaterState {
    public UpdatingState(AutoUpdater autoUpdater) {
        super(autoUpdater);
    }

    @Override
    public void start() {
        throw new ZalandoScraperBotException(
                "Auto Updater is running already.",
                "Admin tried to start Auto Updater but is running already.");
    }

    @Override
    public void startOnce() {
        throw new ZalandoScraperBotException(
                "Auto Updater is running already.\n\nTry to stop running thread first.",
                "Admin tried to start Auto Updater Once but it is running already.");
    }

    @Override
    public void stop() {
        throw new ZalandoScraperBotException(
                "Fail to stop Auto Updater.\n\nProduct Updater is running, try again in a moment.",
                "Admin tried to stop Auto Updater but Product Updater running.");
    }

    @Override
    public boolean isRunning() {
        return true;
    }
}
