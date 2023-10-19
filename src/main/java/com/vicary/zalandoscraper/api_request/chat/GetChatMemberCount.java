package com.vicary.zalandoscraper.api_request.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import com.vicary.zalandoscraper.api_request.ApiRequest;
import com.vicary.zalandoscraper.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class GetChatMemberCount implements ApiRequest<Integer> {
    /**
     * Use this method to get the number of members in a chat.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @Override
    public Integer getReturnObject() {
        return 0;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.GET_CHAT_MEMBER_COUNT.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
