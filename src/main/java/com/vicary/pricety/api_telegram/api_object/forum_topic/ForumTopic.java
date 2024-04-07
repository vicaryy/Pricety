package com.vicary.pricety.api_telegram.api_object.forum_topic;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.pricety.api_telegram.api_object.ApiObject;
import lombok.*;

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
