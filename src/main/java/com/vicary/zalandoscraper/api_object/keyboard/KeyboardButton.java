package com.vicary.zalandoscraper.api_object.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import com.vicary.zalandoscraper.api_object.ApiObject;
import com.vicary.zalandoscraper.api_object.other.WebAppInfo;

@Getter
@ToString
@EqualsAndHashCode
public class KeyboardButton implements ApiObject {
    @JsonProperty("text")
    private String text;

    @JsonProperty("request_user")
    private KeyboardButtonRequestUser requestUser;

    @JsonProperty("request_chat")
    private KeyboardButtonRequestChat requestChat;

    @JsonProperty("request_contact")
    private Boolean requestContact;

    @JsonProperty("request_location")
    private Boolean requestLocation;

    @JsonProperty("request_poll")
    private KeyboardButtonPollType requestPoll;

    @JsonProperty("web_app")
    private WebAppInfo webApp;

    private KeyboardButton(){}
}
