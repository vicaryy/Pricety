package com.vicary.pricety.api_telegram.service;

import com.vicary.pricety.api_telegram.api_object.Update;

public interface UpdateReceiver {
    void receive(Update update);

    void setRunning(boolean isRunning);

    boolean isRunning();

    String botToken();
}
