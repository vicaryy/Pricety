package com.vicary.zalandoscraper.api_object.chat.chat_member;

import com.vicary.zalandoscraper.api_object.ApiObject;
import com.vicary.zalandoscraper.api_object.User;

public interface ChatMember extends ApiObject {
    String getStatus();

    User getUser();
}
