package com.vicary.zalandoscraper.api_telegram.api_request.forum_topic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import com.vicary.zalandoscraper.api_telegram.api_request.ApiRequest;
import com.vicary.zalandoscraper.api_telegram.service.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class CloseForumTopic implements ApiRequest<Boolean> {
    /**
     * Use this method to close an open topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights,
     * unless it is the creator of the topic.
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
        return EndPoint.CLOSE_FORUM_TOPIC.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
