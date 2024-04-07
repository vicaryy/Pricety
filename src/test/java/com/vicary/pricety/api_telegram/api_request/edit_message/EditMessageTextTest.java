package com.vicary.pricety.api_telegram.api_request.edit_message;

import com.vicary.pricety.api_telegram.api_object.message.MessageEntity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EditMessageTextTest {

    @Test
    void checkValidation_expectDoesNotThrow_ValidParseAndNullEntities() {
        //given
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId("chatId")
                .messageId(123)
                .text("text")
                .parseMode("HTML")
                .entities(null)
                .build();

        //when
        //then
        assertDoesNotThrow(editMessageText::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_NullParseAndNullEntities() {
        //given
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId("chatId")
                .messageId(123)
                .text("text")
                .parseMode(null)
                .entities(null)
                .build();

        //when
        //then
        assertDoesNotThrow(editMessageText::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_NullParseAndValidEntities() {
        //given
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId("chatId")
                .messageId(123)
                .text("text")
                .parseMode(null)
                .entities(new ArrayList<>(List.of(new MessageEntity())))
                .build();

        //when
        //then
        assertDoesNotThrow(editMessageText::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_ProperParseModeButEntitiesIsEmpty() {
        //given
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId("chatId")
                .messageId(123)
                .text("text")
                .parseMode("HTML")
                .entities(new ArrayList<>())
                .build();

        //when
        //then
        assertDoesNotThrow(editMessageText::checkValidation);
    }

    @Test
    void checkValidation_expectThrowIllegalArgumentEx_WrongParseMode() {
        //given
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId("chatId")
                .messageId(123)
                .text("text")
                .parseMode("randomParseMode")
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, editMessageText::checkValidation);
    }

    @Test
    void checkValidation_expectThrowIllegalArgumentEx_ProperParseModeButEntitiesIsNotEmptyAndNull() {
        //given
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId("chatId")
                .messageId(123)
                .text("text")
                .parseMode("HTML")
                .entities(new ArrayList<>(List.of(new MessageEntity())))
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, editMessageText::checkValidation);
    }
}