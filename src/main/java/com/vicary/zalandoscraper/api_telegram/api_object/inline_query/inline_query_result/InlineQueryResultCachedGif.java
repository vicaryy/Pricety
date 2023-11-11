package com.vicary.zalandoscraper.api_telegram.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.zalandoscraper.api_telegram.api_object.inline_query.input_message_content.InputMessageContent;
import com.vicary.zalandoscraper.api_telegram.api_object.keyboard.InlineKeyboardMarkup;
import com.vicary.zalandoscraper.api_telegram.api_object.message.MessageEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class InlineQueryResultCachedGif implements InlineQueryResult {
    /**
     * Represents a link to an animated GIF file stored on the Telegram servers.
     */
    @JsonProperty("type")
    private final String type = "gif";

    @JsonProperty("id")
    private String id;

    @JsonProperty("gif_file_id")
    private String gifFileId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("caption")
    private String caption;

    @JsonProperty("parse_mode")
    private String parseMode;

    @JsonProperty("caption_entities")
    private List<MessageEntity> captionEntities;

    @JsonProperty("reply_markup")
    private InlineKeyboardMarkup replyMarkup;

    @JsonProperty("input_message_content")
    private InputMessageContent inputMessageContent;

    private InlineQueryResultCachedGif() {
    }
}
