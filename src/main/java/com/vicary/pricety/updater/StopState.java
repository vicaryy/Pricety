package com.vicary.pricety.updater;

import com.vicary.pricety.exception.ScraperBotException;

public class StopState extends UpdaterState {
    public StopState(AutoUpdater autoUpdater) {
        super(autoUpdater);
    }

    @Override
    public void start() {
        autoUpdater.startRunningThread(() -> {
            autoUpdater.setState(new RunningState(autoUpdater));
            autoUpdater.run();
            autoUpdater.setState(new StopState(autoUpdater));
        });
    }

    @Override
    public void startOnce() {
        autoUpdater.startRunningThread(() -> {
            autoUpdater.setState(new RunningOnceState(autoUpdater));
            autoUpdater.runOnce();
            autoUpdater.setState(new StopState(autoUpdater));
        });
    }

    @Override
    public void stop() {
        throw new ScraperBotException("Auto Updater is stopped already.", "Admin tried to stop Auto Updater but is stopped already.");
    }

    @Override
    public boolean isUpdating() {
        return false;
    }

    @Override
    public String getState() {
        return "Stopped";
    }
}
