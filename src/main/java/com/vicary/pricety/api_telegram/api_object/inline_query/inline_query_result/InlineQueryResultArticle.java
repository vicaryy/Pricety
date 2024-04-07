package com.vicary.pricety.api_telegram.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.pricety.api_telegram.api_object.inline_query.input_message_content.InputMessageContent;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InlineQueryResultArticle implements InlineQueryResult {
    /**
     * Represents a link to an article or web page.
     */

    @JsonProperty("type")
    private final String type = "article";

    @NonNull
    @JsonProperty("id")
    private String id;

    @NonNull
    @JsonProperty("title")
    private String title;

    @NonNull
    @JsonProperty("input_message_content")
    private InputMessageContent inputMessageContent;

//    @JsonProperty("reply_markup")
//    private InlineKeyboardMarkup replyMarkup;
//
//    @JsonProperty("url")
//    private final String url = "";
//
//    @JsonProperty("hide_url")
//    private boolean hideUrl;
//
//    @JsonProperty("description")
//    private final String description = "";
//
//    @JsonProperty("thumbnail_url")
//    private final String thumbnailUrl = "";
//
//    @JsonProperty("thumbnail_width")
//    private final Integer thumbnailWidth = 0;
//
//    @JsonProperty("thumbnail_height")
//    private final Integer thumbnailHeight = 0;
}
