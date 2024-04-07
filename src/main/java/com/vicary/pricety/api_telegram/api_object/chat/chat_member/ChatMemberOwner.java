package com.vicary.pricety.api_telegram.api_object.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import com.vicary.pricety.api_telegram.api_object.User;

@Data
public class ChatMemberOwner implements ChatMember {
    @JsonProperty("status")
    private final String status = "creator";

    @JsonProperty("user")
    private User user;

    @JsonProperty("is_anonymous")
    private Boolean isAnonymous;

    @JsonProperty("custom_title")
    private String customTitle;

    public ChatMemberOwner() {
    }
}
