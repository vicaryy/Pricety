package com.vicary.pricety.api_telegram.api_object.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import com.vicary.pricety.api_telegram.api_object.User;

@Data
public class ChatMemberLeft implements ChatMember {
    @JsonProperty("status")
    private final String status = "left";

    @JsonProperty("user")
    private User user;

    public ChatMemberLeft() {
    }
}
