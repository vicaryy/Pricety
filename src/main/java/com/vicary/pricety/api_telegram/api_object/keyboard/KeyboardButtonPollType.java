package com.vicary.pricety.api_telegram.api_object.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import com.vicary.pricety.api_telegram.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class KeyboardButtonPollType implements ApiObject {
    @JsonProperty("type")
    private String type;

    private KeyboardButtonPollType() {
    }
}
