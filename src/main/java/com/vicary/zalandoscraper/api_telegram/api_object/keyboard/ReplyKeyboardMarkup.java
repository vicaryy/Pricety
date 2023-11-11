package com.vicary.zalandoscraper.api_telegram.api_object.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import com.vicary.zalandoscraper.api_telegram.api_object.ApiObject;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyKeyboardMarkup implements ApiObject, ReplyMarkup {
    @JsonProperty("keyboard")
    private List<List<KeyboardButton>> keyboard;

    @JsonProperty("is_persistent")
    private boolean isPersistent;

    @JsonProperty("resize_keyboard")
    private boolean resizeKeyboard;

    @JsonProperty("one_time_keyboard")
    private boolean oneTimeKeyboard;

    @JsonProperty("input_field_placeholder")
    private final String inputFieldPlaceholder = "";

    @JsonProperty("selective")
    private boolean selective;

    @Override
    public void checkValidation() {

    }
}
