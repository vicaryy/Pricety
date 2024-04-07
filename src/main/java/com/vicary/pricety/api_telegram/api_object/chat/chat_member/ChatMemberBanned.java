package com.vicary.pricety.api_telegram.api_object.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import com.vicary.pricety.api_telegram.api_object.User;

@Data
public class ChatMemberBanned implements ChatMember {
    @JsonProperty("status")
    private final String status = "kicked";

    @JsonProperty("user")
    private User user;

    @JsonProperty("until_date")
    private Integer untilDate;

    public ChatMemberBanned() {
    }
}
