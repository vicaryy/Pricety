package com.vicary.zalandoscraper.api_telegram.api_request.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import com.vicary.zalandoscraper.api_telegram.api_request.ApiRequest;
import com.vicary.zalandoscraper.api_telegram.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class LeaveChat implements ApiRequest<Boolean> {
    /**
     * Use this method for your bot to leave a group, supergroup or channel.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.LEAVE_CHAT.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
