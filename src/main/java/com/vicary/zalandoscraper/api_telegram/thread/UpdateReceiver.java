package com.vicary.zalandoscraper.api_telegram.thread;

import com.vicary.zalandoscraper.api_telegram.api_object.Update;

public interface UpdateReceiver {
    void receive(Update update);

    String botToken();
}
