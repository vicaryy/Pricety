package com.vicary.pricety.api_telegram.api_object.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.pricety.api_telegram.api_object.ApiObject;
import com.vicary.pricety.api_telegram.api_object.User;
import com.vicary.pricety.api_telegram.api_object.chat.Chat;
import com.vicary.pricety.api_telegram.api_object.chat.ChatInviteLink;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class ChatJoinRequest implements ApiObject {
    @JsonProperty("chat")
    private Chat chat;

    @JsonProperty("from")
    private User from;

    @JsonProperty("user_chat_id")
    private Long userChatId;

    @JsonProperty("date")
    private Integer date;

    @JsonProperty("bio")
    private String bio;

    @JsonProperty("invite_link")
    private ChatInviteLink inviteLink;

    private ChatJoinRequest() {
    }
}
