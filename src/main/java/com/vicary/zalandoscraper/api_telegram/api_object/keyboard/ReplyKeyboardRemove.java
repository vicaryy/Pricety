package com.vicary.zalandoscraper.api_telegram.api_object.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import com.vicary.zalandoscraper.api_telegram.api_object.ApiObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyKeyboardRemove implements ApiObject, ReplyMarkup {
    @JsonProperty("remove_keyboard")
    private boolean removeKeyboard;

    @JsonProperty("selective")
    private boolean selective;

    @Override
    public void checkValidation() {
    }
}
