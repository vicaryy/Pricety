package com.vicary.zalandoscraper.api_object.forum_topic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import com.vicary.zalandoscraper.api_object.ApiObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumTopic implements ApiObject {
    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("icon_color")
    private Integer iconColor;

    @JsonProperty("icon_custom_emoji_id")
    private String iconCustomEmojiId;
}
