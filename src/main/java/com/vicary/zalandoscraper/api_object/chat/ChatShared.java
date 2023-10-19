package com.vicary.zalandoscraper.api_object.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import com.vicary.zalandoscraper.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class ChatShared implements ApiObject {
    @JsonProperty("request_id")
    private Integer requestId;

    @JsonProperty("chat_id")
    private Integer chatId;

    private ChatShared() {
    }
}
