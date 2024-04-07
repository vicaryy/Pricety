package com.vicary.pricety.api_telegram.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.pricety.api_telegram.api_object.inline_query.input_message_content.InputMessageContent;
import com.vicary.pricety.api_telegram.api_object.keyboard.InlineKeyboardMarkup;
import com.vicary.pricety.api_telegram.api_object.message.MessageEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class InlineQueryResultDocument implements InlineQueryResult {
    /**
     * Represents a link to a file. By default, this file will be sent by the user with an optional caption.
     * Alternatively, you can use input_message_content to send a message with the specified content instead of the file.
     * Currently, only .PDF and .ZIP files can be sent using this method.
     */
    @JsonProperty("type")
    private final String type = "document";

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("caption")
    private String caption;

    @JsonProperty("parse_mode")
    private String parseMode;

    @JsonProperty("caption_entities")
    private List<MessageEntity> captionEntities;

    @JsonProperty("document_url")
    private String documentUrl;

    @JsonProperty("mime_type")
    private String mimeType;

    @JsonProperty("description")
    private String description;

    @JsonProperty("reply_markup")
    private InlineKeyboardMarkup replyMarkup;

    @JsonProperty("input_message_content")
    private InputMessageContent inputMessageContent;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @JsonProperty("thumbnail_width")
    private Integer thumbnailWidth;

    @JsonProperty("thumbnail_height")
    private Integer thumbnailHeight;

    private InlineQueryResultDocument() {
    }
}
