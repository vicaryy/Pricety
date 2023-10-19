package com.vicary.zalandoscraper.api_object.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import com.vicary.zalandoscraper.api_object.ApiObject;
import com.vicary.zalandoscraper.api_object.User;
import com.vicary.zalandoscraper.api_request.Validation;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEntity implements ApiObject, Validation {
    @JsonProperty("type")
    private String type;

    @JsonProperty("offset")
    private Integer offset;

    @JsonProperty("length")
    private Integer length;

    @JsonProperty("url")
    private String url;

    @JsonProperty("user")
    private User user;

    @JsonProperty("language")
    private String language;

    @JsonProperty("custom_emoji_id")
    private String customEmojiId;

    @Override
    public void checkValidation() {
        if (this.type == null || this.offset == null || this.length == null)
            throw new IllegalArgumentException("Type, offset and length cannot be null.");
    }
}
