package com.vicary.zalandoscraper.updater;

public abstract class UpdaterState {
    protected AutoUpdater autoUpdater;

    public UpdaterState(AutoUpdater autoUpdater) {
        this.autoUpdater = autoUpdater;
    }

    public abstract void start();
    public abstract void startOnce();
    public abstract void stop();
    public abstract boolean isRunning();
    public abstract String getState();
}
