package com.vicary.zalandoscraper.api_object.input_media;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import com.vicary.zalandoscraper.api_object.message.MessageEntity;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class InputMediaPhoto implements InputMedia {
    @JsonProperty("type")
    private final String type = "photo";

    @NonNull
    @JsonProperty("media")
    private String media;

    @JsonProperty("caption")
    private String caption;

    @JsonProperty("parse_mode")
    private String parseMode;

    @JsonProperty("caption_entities")
    private List<MessageEntity> captionEntities;

    @JsonProperty("has_spoiler")
    private Boolean hasSpoiler;
}
