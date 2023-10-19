package com.vicary.zalandoscraper.api_object.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.zalandoscraper.api_request.Validation;
import lombok.*;
import com.vicary.zalandoscraper.api_object.ApiObject;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class InlineKeyboardMarkup implements ApiObject, ReplyMarkup, Validation {
    @JsonProperty("inline_keyboard")
    private List<List<InlineKeyboardButton>> inlineKeyboard;

    @Override
    public void checkValidation() {
//        if (inlineKeyboard == null)
//            throw new IllegalArgumentException("Inline Keyboard cannot be null.");

        if (inlineKeyboard != null) {
            for (List<InlineKeyboardButton> i : inlineKeyboard) {
                for (InlineKeyboardButton ii : i) {
                    ii.checkValidation();
                }
            }
        }
    }
}
