package com.vicary.zalandoscraper.api_telegram.api_object.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import com.vicary.zalandoscraper.api_telegram.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class LoginUrl implements ApiObject {
    @JsonProperty("url")
    private String url;

    @JsonProperty("forward_text")
    private String forwardText;

    @JsonProperty("bot_username")
    private String botUsername;

    @JsonProperty("request_write_access")
    private Boolean requestWriteAccess;

    private LoginUrl() {
    }
}
