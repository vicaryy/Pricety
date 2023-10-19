package com.vicary.zalandoscraper.api_object.forum_topic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import com.vicary.zalandoscraper.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class ForumTopicEdited implements ApiObject {
    @JsonProperty("name")
    private String name;

    @JsonProperty("icon_custom_emoji_id")
    private String iconCustomEmojiId;

    private ForumTopicEdited() {
    }
}
