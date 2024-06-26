package com.vicary.pricety.updater;

public abstract class UpdaterState {
    protected AutoUpdater autoUpdater;

    public UpdaterState(AutoUpdater autoUpdater) {
        this.autoUpdater = autoUpdater;
    }

    public abstract void start();
    public abstract void startOnce();
    public abstract void stop();
    public abstract boolean isUpdating();
    public abstract String getState();
}
