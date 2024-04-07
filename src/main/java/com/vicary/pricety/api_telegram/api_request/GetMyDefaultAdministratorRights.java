package com.vicary.pricety.api_telegram.api_request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.pricety.api_telegram.api_object.chat.ChatAdministratorRights;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.vicary.pricety.api_telegram.service.EndPoint;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetMyDefaultAdministratorRights implements ApiRequest<ChatAdministratorRights> {
    /**
     * Use this method to get the current default administrator rights of the bot.
     *
     * @param forChannels Pass True to get default administrator rights of the bot in channels.
     * Otherwise, default administrator rights of the bot for groups and supergroups will be returned.
     */
    @JsonProperty("for_channels")
    private Boolean forChannels;

    @Override
    public ChatAdministratorRights getReturnObject() {
        return new ChatAdministratorRights();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.GET_MY_DEFAULT_ADMINISTRATOR_RIGHTS.getPath();
    }

    @Override
    public void checkValidation() {
    }
}
