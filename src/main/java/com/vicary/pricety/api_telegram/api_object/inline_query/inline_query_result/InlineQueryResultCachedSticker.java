package com.vicary.pricety.api_telegram.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.pricety.api_telegram.api_object.inline_query.input_message_content.InputMessageContent;
import com.vicary.pricety.api_telegram.api_object.keyboard.InlineKeyboardMarkup;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class InlineQueryResultCachedSticker implements InlineQueryResult {
    /**
     * Represents a link to a sticker stored on the Telegram servers.
     */
    @JsonProperty("type")
    private final String type = "sticker";

    @JsonProperty("id")
    private String id;

    @JsonProperty("sticker_file_id")
    private String stickerFileId;

    @JsonProperty("reply_markup")
    private InlineKeyboardMarkup replyMarkup;

    @JsonProperty("input_message_content")
    private InputMessageContent inputMessageContent;

    private InlineQueryResultCachedSticker() {
    }
}
