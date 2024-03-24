package com.vicary.zalandoscraper.service.repository_services;

import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.repository.UserRepository;
import com.vicary.zalandoscraper.service.UpdateReceiverService;
import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService service;

    @MockBean
    private UserRepository repository;

    @MockBean
    private UpdateReceiverService updateReceiverService;

    @BeforeAll
    static void beforeAll() {
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages", Locale.of("en")));
    }

    @Test
    void updateUserNick_expectThrows_LengthUnderThreeCharacters() {
        //given
        long givenUserId = 123;
        String givenNick = "as";
        //when
        //then
        assertThrows(IllegalInputException.class, () -> service.updateUserNick(givenUserId, givenNick));
        verify(repository, times(0)).existsByNick(givenNick);
    }

    @Test
    void updateUserNick_expectThrows_LengthAboveTwentyFiveCharacters() {
        //given
        long givenUserId = 123;
        String givenNick = "qwertyuiopasdfghjklzxcvbnm";
        //when
        //then
        assertThrows(IllegalInputException.class, () -> service.updateUserNick(givenUserId, givenNick));
        verify(repository, times(0)).existsByNick(givenNick);
    }

    @Test
    void updateUserNick_expectThrows_NickWithSpaces() {
        //given
        long givenUserId = 123;
        String givenNick = "nick nick";
        //when
        //then
        assertThrows(IllegalInputException.class, () -> service.updateUserNick(givenUserId, givenNick));
        verify(repository, times(0)).existsByNick(givenNick);
    }

    @Test
    void updateUserNick_expectThrows_NickWithSpecialCharacters() {
        //given
        long givenUserId = 123;
        String givenNick = "nick%#@nick";
        //when
        //then
        assertThrows(IllegalInputException.class, () -> service.updateUserNick(givenUserId, givenNick));
        verify(repository, times(0)).existsByNick(givenNick);
    }

    @Test
    void updateUserNick_expectThrows_NickAlreadyExists() {
        //given
        long givenUserId = 123;
        String givenNick = "nick";
        //when
        when(repository.existsByNick(givenNick)).thenReturn(true);

        //then
        assertThrows(IllegalInputException.class, () -> service.updateUserNick(givenUserId, givenNick));
        verify(repository, times(1)).existsByNick(givenNick);
    }

//    @Test
//    void updateUserNick_expectThrows_UserIdNotFound() {
//        //given
//        long givenUserId = invalidUserId;
//        String givenNick = "nick123";
//        //when
//        when(repository.existsByNick(givenNick)).thenReturn(false);
//        when(repository.findByUserId(givenUserId)).thenReturn(Optional.empty());
//
//        //then
//        assertThrows(IllegalInputException.class, () -> service.updateUserNick(givenUserId, givenNick));
//        verify(repository, times(1)).existsByNick(givenNick);
//        verify(repository, times(1)).findByUserId(givenUserId);
//    }

    @Test
    void updateUserNick_expectNotThrow_ValidNick() {
        //given
        long givenUserId = 123;
        List<String> givenNicks = List.of(
                "vicary",
                "Vicary",
                "VICARY",
                "vicary1",
                "12345",
                "123vIcArY321"
        );
        //when
        when(repository.existsByNick(anyString())).thenReturn(false);
        when(repository.findByUserId(anyLong())).thenReturn(Optional.of(UserEntity.builder().build()));

        //then
        givenNicks.forEach(nick -> assertDoesNotThrow(() -> service.updateUserNick(givenUserId, nick)));
        verify(repository, times(3)).existsByNick("vicary");
        verify(repository, times(1)).existsByNick("vicary1");
        verify(repository, times(1)).existsByNick("12345");
        verify(repository, times(1)).existsByNick("123vicary321");
    }
}











