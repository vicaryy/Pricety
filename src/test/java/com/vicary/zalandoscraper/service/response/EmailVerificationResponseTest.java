package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailVerificationResponseTest {

    private static EmailVerificationResponse emailVerificationResponse;
    private static ResponseFacade responseFacade;
    private static QuickSender quickSender;

    @BeforeAll
    static void beforeAll() {
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages", Locale.of("en")));
    }

    @BeforeEach
    void beforeEach() {
        responseFacade = mock(ResponseFacade.class);
        quickSender = mock(QuickSender.class);
    }

    @Test
    void response_EverythingIsValid() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setEmail("email@email.com");
        givenUser.setText("v-validToken");
        String givenToken = "validToken";

        //when
        when(responseFacade.emailVerExistsByTelegramIdAndToken(givenUser.getChatId(), givenToken)).thenReturn(true);
        emailVerificationResponse = new EmailVerificationResponse(responseFacade, givenUser, quickSender);
        emailVerificationResponse.response();

        //then
        verify(responseFacade, times(1)).emailVerExistsByTelegramIdAndToken(anyString(), anyString());
        verify(responseFacade, times(1)).setUserVerifiedEmail(givenUser.getTelegramId(), true);
        verify(responseFacade, times(1)).deleteEmailVerByToken(givenToken);
        verify(quickSender, times(1)).message(givenUser.getChatId(), Messages.other("emailVerifiedSuccessfully"), false);
    }

    @Test
    void response_UserDontHaveEmail() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setEmail(null);

        //when
        emailVerificationResponse = new EmailVerificationResponse(responseFacade, givenUser, quickSender);
        emailVerificationResponse.response();

        //then
        verify(responseFacade, times(0)).emailVerExistsByTelegramIdAndToken(anyString(), anyString());
    }

    @Test
    void response_UserAlreadyHaveVerifiedEmail() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setEmail("email@email.com");
        givenUser.setVerifiedEmail(true);

        //when
        emailVerificationResponse = new EmailVerificationResponse(responseFacade, givenUser, quickSender);
        emailVerificationResponse.response();

        //then
        verify(responseFacade, times(0)).emailVerExistsByTelegramIdAndToken(anyString(), anyString());
    }

    @Test
    void response_expectThrow_UserTypeWrongEmailToken() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setEmail("email@email.com");
        givenUser.setText("v-wrongToken");
        String givenToken = "wrongToken";

        //when
        emailVerificationResponse = new EmailVerificationResponse(responseFacade, givenUser, quickSender);
        when(responseFacade.emailVerExistsByTelegramIdAndToken(givenUser.getChatId(), givenToken)).thenReturn(false);

        //then
        assertThrows(IllegalInputException.class, () -> emailVerificationResponse.response());
        verify(responseFacade, times(1)).emailVerExistsByTelegramIdAndToken(anyString(), anyString());
    }

    private ActiveUser getDefaultActiveUser() {
        ActiveUser givenUser = new ActiveUser();
        givenUser.setTelegramId("123");
        givenUser.setChatId("123");
        givenUser.setMessageId(123);
        givenUser.setText("v-123456789");
        return givenUser;
    }
}