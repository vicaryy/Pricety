package com.vicary.zalandoscraper.api_telegram.api_object.chat.chat_member;

import com.vicary.zalandoscraper.api_telegram.api_object.ApiObject;
import com.vicary.zalandoscraper.api_telegram.api_object.User;

public interface ChatMember extends ApiObject {
    String getStatus();

    User getUser();
}
