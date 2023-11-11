package com.vicary.zalandoscraper.api_telegram.api_request.forum_topic;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.zalandoscraper.api_telegram.api_request.ApiRequest;
import lombok.*;
import com.vicary.zalandoscraper.api_telegram.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class DeleteForumTopic implements ApiRequest<Boolean> {
    /**
     * Use this method to delete a forum topic along with all its messages in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have the can_delete_messages administrator rights.
     *
     * @param chatId           Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
     * @param messageThreadId  Unique identifier for the target message thread of the forum topic
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @NonNull
    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.DELETE_FORUM_TOPIC.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
