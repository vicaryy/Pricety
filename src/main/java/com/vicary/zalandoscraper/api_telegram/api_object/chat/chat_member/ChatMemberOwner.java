package com.vicary.zalandoscraper.api_telegram.api_object.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import com.vicary.zalandoscraper.api_telegram.api_object.User;

@Getter
@ToString
@EqualsAndHashCode
public class ChatMemberOwner implements ChatMember {
    @JsonProperty("status")
    private final String status = "creator";

    @JsonProperty("user")
    private User user;

    @JsonProperty("is_anonymous")
    private Boolean isAnonymous;

    @JsonProperty("custom_title")
    private String customTitle;

    private ChatMemberOwner() {
    }
}
