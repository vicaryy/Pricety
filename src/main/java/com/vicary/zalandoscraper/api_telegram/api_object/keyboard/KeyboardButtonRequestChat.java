package com.vicary.zalandoscraper.api_telegram.api_object.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.zalandoscraper.api_telegram.api_object.ApiObject;
import com.vicary.zalandoscraper.api_telegram.api_object.chat.ChatAdministratorRights;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class KeyboardButtonRequestChat implements ApiObject {
    @JsonProperty("request_id")
    private Integer requestId;

    @JsonProperty("chat_is_channel")
    private Boolean chatIsChannel;

    @JsonProperty("chat_is_forum")
    private Boolean chatIsForum;

    @JsonProperty("chat_has_username")
    private Boolean chatHasUsername;

    @JsonProperty("chat_is_created")
    private Boolean chatIsCreated;

    @JsonProperty("user_administrator_rights")
    private ChatAdministratorRights userAdministratorRights;

    @JsonProperty("bot_administrator_rights")
    private ChatAdministratorRights botAdministratorRights;

    @JsonProperty("bot_is_member")
    private Boolean botIsMember;

    private KeyboardButtonRequestChat() {
    }
}
