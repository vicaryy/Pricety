package com.vicary.pricety.api_telegram.api_object.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import com.vicary.pricety.api_telegram.api_object.ApiObject;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyboardButton implements ApiObject {
    @NonNull
    @JsonProperty("text")
    private String text;
//
//    @JsonProperty("request_user")
//    private KeyboardButtonRequestUser requestUser;
//
//    @JsonProperty("request_chat")
//    private KeyboardButtonRequestChat requestChat;
//
//    @JsonProperty("request_contact")
//    private boolean requestContact;
//
//    @JsonProperty("request_location")
//    private boolean requestLocation;
//
//    @JsonProperty("request_poll")
//    private KeyboardButtonPollType requestPoll;
//
//    @JsonProperty("web_app")
//    private WebAppInfo webApp;
}
