package com.vicary.pricety.service.response;

import com.vicary.pricety.api_telegram.service.QuickSender;
import com.vicary.pricety.messages.Messages;
import com.vicary.pricety.service.response.inline_markup.InlineKeyboardMarkupFactory;
import com.vicary.pricety.thread_local.ActiveLanguage;
import com.vicary.pricety.thread_local.ActiveUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.mockito.Mockito.*;

class CommandResponseTest {

    private static CommandResponse commandResponse;
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
    void response_sendStartWithNick() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/start");
        givenUser.setNick("user");
        String givenNick = " user";

        //when
        commandResponse = new CommandResponse(responseFacade, givenUser, quickSender);
        commandResponse.response();

        //then
        verify(quickSender, times(1)).message(givenUser.getChatId(), Messages.command("start").formatted(givenNick), true);
    }

    @Test
    void response_sendStartWithoutNick() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/start");
        String givenNick = " ";

        //when
        commandResponse = new CommandResponse(responseFacade, givenUser, quickSender);
        commandResponse.response();

        //then
        verify(quickSender, times(1)).message(givenUser.getChatId(), Messages.command("start").formatted(givenNick), true);
    }

    @Test
    void response_sendMenu() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/menu");

        //when
        commandResponse = new CommandResponse(responseFacade, givenUser, quickSender);
        commandResponse.response();

        //then
        verify(quickSender, times(1)).inlineMarkup(
                eq(givenUser.getChatId()),
                anyString(),
                eq(InlineKeyboardMarkupFactory.getMenu()),
                eq(true));
    }

    @Test
    void response_sendUpdate() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/update");
        String givenLastUpdatedTime = "1 hour ago";

        //when
        when(responseFacade.getLastUpdateTime()).thenReturn(givenLastUpdatedTime);
        commandResponse = new CommandResponse(responseFacade, givenUser, quickSender);
        commandResponse.response();

        //then
        verify(responseFacade, times(1)).getLastUpdateTime();
        verify(quickSender, times(1)).message(
                givenUser.getChatId(),
                Messages.command("update").formatted(givenLastUpdatedTime),
                true);
    }

    @Test
    void response_sendLimits() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/limits");

        //when
        commandResponse = new CommandResponse(responseFacade, givenUser, quickSender);
        commandResponse.response();

        //then
        verify(quickSender, times(1)).message(
                givenUser.getChatId(),
                Messages.command("limits"),
                true);
    }

    @Test
    void response_sendHelp() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/help");

        //when
        commandResponse = new CommandResponse(responseFacade, givenUser, quickSender);
        commandResponse.response();

        //then
        verify(quickSender, times(1)).message(
                givenUser.getChatId(),
                Messages.command("help"),
                true);
    }

    @Test
    void response_sendTip() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/tip");

        //when
        commandResponse = new CommandResponse(responseFacade, givenUser, quickSender);
        commandResponse.response();

        //then
        verify(quickSender, times(1)).message(
                givenUser.getChatId(),
                Messages.command("tip"),
                true);
    }

    @Test
    void response_expectNothing_InvalidCommand() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/invalidCommand");

        //when
        commandResponse = new CommandResponse(responseFacade, givenUser, quickSender);
        commandResponse.response();

        //then
        verify(quickSender, times(0)).message(
                anyString(),
                anyString(),
                anyBoolean());
        verify(quickSender, times(0)).inlineMarkup(
                anyString(),
                anyString(),
                any(),
                anyBoolean());
    }




    private ActiveUser getDefaultActiveUser() {
        ActiveUser givenUser = new ActiveUser();
        givenUser.setTelegramId("123");
        givenUser.setChatId("123");
        givenUser.setMessageId(123);
        return givenUser;
    }
}