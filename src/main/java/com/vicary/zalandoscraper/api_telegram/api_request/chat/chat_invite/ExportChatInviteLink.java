package com.vicary.zalandoscraper.api_telegram.api_request.chat.chat_invite;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.zalandoscraper.api_telegram.api_request.ApiRequest;
import lombok.*;
import com.vicary.zalandoscraper.api_telegram.service.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class ExportChatInviteLink implements ApiRequest<String> {
    /**
     * Use this method to generate a new primary invite link for a chat; any previously generated primary link is revoked.
     * The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights.
     *
     * @param chatId    Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @Override
    public String getReturnObject() {
        return "";
    }

    @Override
    public String getEndPoint() {
        return EndPoint.EXPORT_CHAT_INVITE_LINK.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
