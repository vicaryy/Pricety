package com.vicary.zalandoscraper.api_telegram.api_request.send;

import com.vicary.zalandoscraper.api_telegram.api_object.message.MessageEntity;
import com.vicary.zalandoscraper.api_telegram.api_request.InputFile;
import com.vicary.zalandoscraper.api_telegram.api_request.send.SendVideo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class SendVideoTest {

    @Test
    void checkValidation_expectDoesNotThrow_ValidParseAndNullEntities() {
        //given
        SendVideo sendVideo = SendVideo.builder()
                .chatId("chatId")
                .video(new InputFile())
                .parseMode("HTML")
                .build();

        //when
        //then
        Assertions.assertDoesNotThrow(sendVideo::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_NullParseAndNullEntities() {
        //given
        SendVideo sendVideo = SendVideo.builder()
                .chatId("chatId")
                .video(new InputFile())
                .build();

        //when
        //then
        Assertions.assertDoesNotThrow(sendVideo::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_NullParseAndValidEntities() {
        //given
        SendVideo sendVideo = SendVideo.builder()
                .chatId("chatId")
                .video(new InputFile())
                .captionEntities(new ArrayList<>(List.of(new MessageEntity())))
                .build();

        //when
        //then
        Assertions.assertDoesNotThrow(sendVideo::checkValidation);
    }

    @Test
    void checkValidation_expectThrowIllegalArgumentEx_WrongParseMode() {
        //given
        SendVideo sendVideo = SendVideo.builder()
                .chatId("chatId")
                .video(new InputFile())
                .parseMode("random parse")
                .build();

        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, sendVideo::checkValidation);
    }

    @Test
    void checkValidation_expectThrowIllegalArgumentEx_ProperParseModeButEntitiesIsNotEmptyAndNull() {
        //given
        SendVideo sendVideo = SendVideo.builder()
                .chatId("chatId")
                .video(new InputFile())
                .parseMode("HTML")
                .captionEntities(new ArrayList<>(List.of(new MessageEntity())))
                .build();

        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, sendVideo::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_ProperParseModeButEntitiesIsEmpty() {
        //given
        SendVideo sendVideo = SendVideo.builder()
                .chatId("chatId")
                .video(new InputFile())
                .parseMode("HTML")
                .captionEntities(new ArrayList<>())
                .build();

        //when
        //then
        Assertions.assertDoesNotThrow(sendVideo::checkValidation);
    }
}