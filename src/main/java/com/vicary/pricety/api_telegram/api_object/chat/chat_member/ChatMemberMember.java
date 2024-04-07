package com.vicary.pricety.api_telegram.api_object.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import com.vicary.pricety.api_telegram.api_object.User;

@Data
public class ChatMemberMember implements ChatMember {
    @JsonProperty("status")
    private final String status = "member";

    @JsonProperty("user")
    private User user;

    public ChatMemberMember() {
    }
}
