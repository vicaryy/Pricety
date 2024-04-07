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
public class InlineQueryResultAudio implements InlineQueryResult {
    /**
     * Represents a link to an MP3 audio file. By default, this audio file will be sent by the user. Alternatively,
     * you can use input_message_content to send a message with the specified content instead of the audio.
     */
    @JsonProperty("type")
    private final String type = "audio";

    @JsonProperty("id")
    private String id;

    @JsonProperty("audio_url")
    private String audioUrl;

    @JsonProperty("title")
    private String title;

    @JsonProperty("caption")
    private String caption;

    @JsonProperty("parse_mode")
    private String parseMode;

    @JsonProperty("caption_entities")
    private List<MessageEntity> captionEntities;

    @JsonProperty("performer")
    private String performer;

    @JsonProperty("audio_duration")
    private Integer audioDuration;

    @JsonProperty("reply_markup")
    private InlineKeyboardMarkup replyMarkup;

    @JsonProperty("input_message_content")
    private InputMessageContent inputMessageContent;

    private InlineQueryResultAudio() {
    }
}
