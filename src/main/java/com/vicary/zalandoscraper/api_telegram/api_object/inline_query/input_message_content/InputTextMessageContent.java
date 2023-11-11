package com.vicary.zalandoscraper.api_telegram.api_object.inline_query.input_message_content;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.zalandoscraper.api_telegram.api_object.message.MessageEntity;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class InputTextMessageContent implements InputMessageContent {
    @NonNull
    @JsonProperty("message_text")
    private String messageText;

    @JsonProperty("parse_mode")
    private final String parseMode = "";

    @JsonProperty("entities")
    private List<MessageEntity> entities;

    @JsonProperty("disable_web_page_preview")
    private boolean disableWebPagePreview;
}
