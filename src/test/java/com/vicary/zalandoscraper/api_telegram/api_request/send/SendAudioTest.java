package com.vicary.zalandoscraper.api_telegram.api_request.send;

import com.vicary.zalandoscraper.api_telegram.api_object.message.MessageEntity;
import com.vicary.zalandoscraper.api_telegram.api_request.InputFile;
import com.vicary.zalandoscraper.api_telegram.api_request.send.SendAudio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SendAudioTest {

    @Test
    void checkValidation_expectDoesNotThrow_ValidParseAndNullEntities() {
        //given
        SendAudio sendAudio = SendAudio.builder()
                .chatId("chatId")
                .audio(new InputFile())
                .parseMode("HTML")
                .build();

        //when
        //then
        assertDoesNotThrow(sendAudio::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_NullParseAndNullEntities() {
        //given
        SendAudio sendAudio = SendAudio.builder()
                .chatId("chatId")
                .audio(new InputFile())
                .build();

        //when
        //then
        assertDoesNotThrow(sendAudio::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_NullParseAndValidEntities() {
        //given
        SendAudio sendAudio = SendAudio.builder()
                .chatId("chatId")
                .audio(new InputFile())
                .captionEntities(new ArrayList<>(List.of(new MessageEntity())))
                .build();

        //when
        //then
        assertDoesNotThrow(sendAudio::checkValidation);
    }

    @Test
    void checkValidation_expectThrowIllegalArgumentEx_WrongParseMode() {
        //given
        SendAudio sendAudio = SendAudio.builder()
                .chatId("chatId")
                .audio(new InputFile())
                .parseMode("random parse")
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, sendAudio::checkValidation);
    }

    @Test
    void checkValidation_expectThrowIllegalArgumentEx_ProperParseModeButEntitiesIsNotEmptyAndNull() {
        //given
        SendAudio sendAudio = SendAudio.builder()
                .chatId("chatId")
                .audio(new InputFile())
                .parseMode("HTML")
                .captionEntities(new ArrayList<>(List.of(new MessageEntity())))
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, sendAudio::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_ProperParseModeButEntitiesIsEmpty() {
        //given
        SendAudio sendAudio = SendAudio.builder()
                .chatId("chatId")
                .audio(new InputFile())
                .parseMode("HTML")
                .captionEntities(new ArrayList<>())
                .build();

        //when
        //then
        assertDoesNotThrow(sendAudio::checkValidation);
    }
}