package com.vicary.zalandoscraper.api_telegram.api_object.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import com.vicary.zalandoscraper.api_telegram.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class UserShared implements ApiObject {
    @JsonProperty("request_id")
    private Integer requestId;

    @JsonProperty("user_id")
    private Integer userId;

    private UserShared() {
    }
}
