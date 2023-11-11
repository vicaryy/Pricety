package com.vicary.zalandoscraper.api_telegram.api_object.forum_topic;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.zalandoscraper.api_telegram.api_object.ApiObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class ForumTopicCreated implements ApiObject {
    @JsonProperty("name")
    private String name;

    @JsonProperty("icon_color")
    private Integer iconColor;

    @JsonProperty("icon_custom_emoji_id")
    private String iconCustomEmojiId;

    private ForumTopicCreated() {
    }
}
