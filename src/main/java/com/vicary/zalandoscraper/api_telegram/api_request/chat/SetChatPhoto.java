package com.vicary.zalandoscraper.api_telegram.api_request.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import com.vicary.zalandoscraper.api_telegram.api_request.ApiRequest;
import com.vicary.zalandoscraper.api_telegram.api_request.InputFile;
import com.vicary.zalandoscraper.api_telegram.service.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class SetChatPhoto implements ApiRequest<Boolean> {
    /**
     * Use this method to set a new profile photo for the chat.
     * Photos can't be changed for private chats. The bot must be an administrator in the chat for this to work
     * and must have the appropriate administrator rights.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param photo  New chat photo, uploaded using multipart/form-data
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @NonNull
    private InputFile photo;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SET_CHAT_PHOTO.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
