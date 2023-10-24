package com.vicary.zalandoscraper.api_object.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.zalandoscraper.api_request.Validation;
import lombok.*;
import com.vicary.zalandoscraper.api_object.ApiObject;
import com.vicary.zalandoscraper.api_object.games.CallbackGame;
import com.vicary.zalandoscraper.api_object.other.LoginUrl;
import com.vicary.zalandoscraper.api_object.chat.SwitchInlineQueryChosenChat;
import com.vicary.zalandoscraper.api_object.other.WebAppInfo;

@Data
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class InlineKeyboardButton implements ApiObject, Validation {
    @NonNull
    @JsonProperty("text")
    private String text;

    @JsonProperty("url")
    private String url;

    @NonNull
    @JsonProperty("callback_data")
    private String callbackData;

    @JsonProperty("web_app")
    private WebAppInfo webApp;

    @JsonProperty("login_url")
    private LoginUrl loginUrl;

    @JsonProperty("switch_inline_query")
    private String switchInlineQuery;

    @JsonProperty("switch_inline_query_current_chat")
    private String switchInlineQueryCurrentChat;

    @JsonProperty("switch_inline_query_chosen_chat")
    private SwitchInlineQueryChosenChat switchInlineQueryChosenChat;

    @JsonProperty("callback_game")
    private CallbackGame callbackGame;

    @JsonProperty("pay")
    private Boolean pay;

    @Override
    public void checkValidation() {
        if (url == null) url = "";
        if (callbackData == null) callbackData = "unknown";
    }
}

