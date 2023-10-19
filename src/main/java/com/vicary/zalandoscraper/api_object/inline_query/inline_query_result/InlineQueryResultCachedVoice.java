package com.vicary.zalandoscraper.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.zalandoscraper.api_object.inline_query.input_message_content.InputMessageContent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import com.vicary.zalandoscraper.api_object.keyboard.InlineKeyboardMarkup;
import com.vicary.zalandoscraper.api_object.message.MessageEntity;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class InlineQueryResultCachedVoice implements InlineQueryResult {
    /**
     * Represents a link to a voice message stored on the Telegram servers.
     */
    @JsonProperty("type")
    private final String type = "voice";

    @JsonProperty("id")
    private String id;

    @JsonProperty("voice_file_id")
    private String voiceFileId;

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

    private InlineQueryResultCachedVoice() {
    }
}
