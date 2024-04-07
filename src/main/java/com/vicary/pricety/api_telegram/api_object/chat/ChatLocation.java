package com.vicary.pricety.api_telegram.api_object.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.pricety.api_telegram.api_object.ApiObject;
import com.vicary.pricety.api_telegram.api_object.Location;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class ChatLocation implements ApiObject {
    @JsonProperty("location")
    private Location location;

    @JsonProperty("address")
    private String address;

    private ChatLocation() {
    }
}
