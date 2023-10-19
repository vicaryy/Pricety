package com.vicary.zalandoscraper.api_object.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.zalandoscraper.api_object.keyboard.ReplyMarkup;
import lombok.*;
import com.vicary.zalandoscraper.api_object.ApiObject;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ForceReply implements ApiObject, ReplyMarkup {
    @JsonProperty("force_reply")
    private Boolean forceReply;

    @JsonProperty("input_field_placeholder")
    private String inputFieldPlaceholder;

    @JsonProperty("selective")
    private Boolean selective;

    @Override
    public void checkValidation() {

    }
}
