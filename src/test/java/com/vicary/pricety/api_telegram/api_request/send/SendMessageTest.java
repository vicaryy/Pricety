package com.vicary.pricety.api_telegram.api_request.send;

import com.vicary.pricety.api_telegram.api_object.ParseMode;
import com.vicary.pricety.api_telegram.api_object.message.MessageEntity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SendMessageTest {
    @Test
    void checkValidation_expectDoesNotThrow_ValidParseAndNullEntities() {
        //given
        SendMessage sendMessage = SendMessage.builder()
                .chatId("chatId")
                .text("text")
                .parseMode(ParseMode.MarkdownV2)
                .entities(null)
                .build();

        //when
        //then
        assertDoesNotThrow(sendMessage::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_NullParseAndNullEntities() {
        //given
        SendMessage sendMessage = SendMessage.builder()
                .chatId("chatId")
                .text("text")
                .parseMode(null)
                .entities(null)
                .build();

        //when
        //then
        assertDoesNotThrow(sendMessage::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_NullParseAndValidEntities() {
        //given
        SendMessage sendMessage = SendMessage.builder()
                .chatId("chatId")
                .text("text")
                .parseMode(null)
                .entities(new ArrayList<>(List.of(new MessageEntity())))
                .build();

        //when
        //then
        assertDoesNotThrow(sendMessage::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_ProperParseModeButEntitiesIsEmpty() {
        //given
        SendMessage sendMessage = SendMessage.builder()
                .chatId("chatId")
                .text("text")
                .parseMode(ParseMode.MarkdownV2)
                .entities(new ArrayList<>())
                .build();

        //when
        //then
        assertDoesNotThrow(sendMessage::checkValidation);
    }


    @Test
    void checkValidation_expectThrowIllegalArgumentEx_ProperParseModeButEntitiesIsNotEmptyAndNull() {
        //given
        SendMessage sendMessage = SendMessage.builder()
                .chatId("chatId")
                .text("text")
                .parseMode(ParseMode.MarkdownV2)
                .entities(new ArrayList<>(List.of(new MessageEntity())))
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, sendMessage::checkValidation);
    }
}