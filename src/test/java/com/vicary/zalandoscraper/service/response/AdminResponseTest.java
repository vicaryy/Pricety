package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.api_telegram.api_object.bot.bot_command.BotCommand;
import com.vicary.zalandoscraper.api_telegram.api_request.ApiRequest;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.api_telegram.service.RequestService;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminResponseTest {

    private static AdminResponse adminResponse;
    private static ResponseFacade responseFacade;
    private static QuickSender quickSender;
    private static RequestService requestService;

    @BeforeAll
    static void beforeAll() {
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages", Locale.of("en")));
    }

    @BeforeEach
    void beforeEach() {
        responseFacade = mock(ResponseFacade.class);
        quickSender = mock(QuickSender.class);
        requestService = mock(RequestService.class);
    }

    @Test
    void response_setPremium_ValidUser() {
        //given
        String givenNick = "user123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/set premium " + givenNick);

        //when
        when(responseFacade.updateUserToPremiumByNick(givenNick)).thenReturn(true);
        adminResponse = new AdminResponse(responseFacade, givenUser, quickSender, requestService);
        adminResponse.response();

        //then
        verify(responseFacade, times(1)).updateUserToPremiumByNick(givenNick);
        verify(quickSender, times(1)).message(givenUser.getChatId(), String.format("User %s successfully updated to Premium.", givenNick), false);
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s does not exist.", givenNick), false);
    }

    @Test
    void response_setPremium_UserDoesNotExists() {
        //given
        String givenNick = "user123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/set premium " + givenNick);

        //when
        when(responseFacade.updateUserToPremiumByNick(givenNick)).thenReturn(false);
        adminResponse = new AdminResponse(responseFacade, givenUser, quickSender, requestService);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(1)).updateUserToPremiumByNick(givenNick);
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s successfully updated to Premium.", givenNick), false);
    }

    @Test
    void response_setStandard_ValidUser() {
        //given
        String givenNick = "user123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/set standard " + givenNick);

        //when
        when(responseFacade.updateUserToStandardByNick(givenNick)).thenReturn(true);
        adminResponse = new AdminResponse(responseFacade, givenUser, quickSender, requestService);
        adminResponse.response();

        //then
        verify(responseFacade, times(1)).updateUserToStandardByNick(givenNick);
        verify(quickSender, times(1)).message(givenUser.getChatId(), String.format("User %s successfully updated to Standard.", givenNick), false);
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s does not exist.", givenNick), false);
    }

    @Test
    void response_setStandard_UserDoesNotExists() {
        //given
        String givenNick = "user123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/set standard " + givenNick);

        //when
        when(responseFacade.updateUserToStandardByNick(givenNick)).thenReturn(false);
        adminResponse = new AdminResponse(responseFacade, givenUser, quickSender, requestService);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(1)).updateUserToStandardByNick(givenNick);
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s successfully updated to Standard.", givenNick), false);
    }

    @Test
    void response_setAdmin_ValidUser() {
        //given
        String givenNick = "user123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/set admin " + givenNick);

        //when
        when(responseFacade.updateUserToAdminByNick(givenNick)).thenReturn(true);
        adminResponse = new AdminResponse(responseFacade, givenUser, quickSender, requestService);
        adminResponse.response();

        //then
        verify(responseFacade, times(1)).updateUserToAdminByNick(givenNick);
        verify(quickSender, times(1)).message(givenUser.getChatId(), String.format("User %s successfully updated to Admin.", givenNick), false);
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s does not exist.", givenNick), false);
    }

    @Test
    void response_setAdmin_UserDoesNotExists() {
        //given
        String givenNick = "user123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/set admin " + givenNick);

        //when
        when(responseFacade.updateUserToAdminByNick(givenNick)).thenReturn(false);
        adminResponse = new AdminResponse(responseFacade, givenUser, quickSender, requestService);

        //then
        assertThrows(IllegalInputException.class ,() -> adminResponse.response());
        verify(responseFacade, times(1)).updateUserToAdminByNick(givenNick);
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s successfully updated to Admin.", givenNick), false);
    }

    @Test
    void response_setNonAdmin_ValidUser() {
        //given
        String givenNick = "user123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/set non-admin " + givenNick);

        //when
        when(responseFacade.updateUserToNonAdminByNick(givenNick)).thenReturn(true);
        adminResponse = new AdminResponse(responseFacade, givenUser, quickSender, requestService);
        adminResponse.response();

        //then
        verify(responseFacade, times(1)).updateUserToNonAdminByNick(givenNick);
        verify(quickSender, times(1)).message(givenUser.getChatId(), String.format("User %s successfully updated to Non-Admin.", givenNick), false);
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s does not exist.", givenNick), false);
    }

    @Test
    void response_setNonAdmin_UserDoesNotExists() {
        //given
        String givenNick = "user123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/set non-admin " + givenNick);

        //when
        when(responseFacade.updateUserToNonAdminByNick(givenNick)).thenReturn(false);
        adminResponse = new AdminResponse(responseFacade, givenUser, quickSender, requestService);

        //then
        assertThrows(IllegalInputException.class ,() -> adminResponse.response());
        verify(responseFacade, times(1)).updateUserToNonAdminByNick(givenNick);
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s successfully updated to Non-Admin.", givenNick), false);
    }

    @Test
    void response_setCommand_ValidCommand() {
        //given
        String givenCommand = "/command:description";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/set command " + givenCommand);

        //when
        adminResponse = new AdminResponse(responseFacade, givenUser, quickSender, requestService);
        adminResponse.response();

        //then
        verify(requestService, times(1)).sendRequestList(any());
        verify(requestService, times(1)).send((ApiRequest<?>) any());
        verify(quickSender, times(1)).message(givenUser.getChatId(), "Successfully add /command command.", false);
    }

    @Test
    void response_setCommand_expectThrow_CommandWithoutColon() {
        //given
        String givenCommand = "/commandDescription";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/set command " + givenCommand);

        //when
        adminResponse = new AdminResponse(responseFacade, givenUser, quickSender, requestService);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(requestService, times(0)).sendRequestList(any());
        verify(requestService, times(0)).send((ApiRequest<?>) any());
        verify(quickSender, times(0)).message(anyString(), anyString(), anyBoolean());
    }

    @Test
    void response_setCommand_expectThrow_CommandIsEmpty() {
        //given
        String givenCommand = "  :Description";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/set command " + givenCommand);

        //when
        adminResponse = new AdminResponse(responseFacade, givenUser, quickSender, requestService);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(requestService, times(0)).sendRequestList(any());
        verify(requestService, times(0)).send((ApiRequest<?>) any());
        verify(quickSender, times(0)).message(anyString(), anyString(), anyBoolean());
    }

    @Test
    void response_setCommand_expectThrow_DescriptionIsEmpty() {
        //given
        String givenCommand = "command:    ";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/set command " + givenCommand);

        //when
        adminResponse = new AdminResponse(responseFacade, givenUser, quickSender, requestService);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(requestService, times(0)).sendRequestList(any());
        verify(requestService, times(0)).send((ApiRequest<?>) any());
        verify(quickSender, times(0)).message(anyString(), anyString(), anyBoolean());
    }

    @Test
    void response_setCommand_expectThrow_BothEmpty() {
        //given
        String givenCommand = ":";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/set command " + givenCommand);

        //when
        adminResponse = new AdminResponse(responseFacade, givenUser, quickSender, requestService);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(requestService, times(0)).sendRequestList(any());
        verify(requestService, times(0)).send((ApiRequest<?>) any());
        verify(quickSender, times(0)).message(anyString(), anyString(), anyBoolean());
    }

    @Test
    void response_setCommand_expectThrow_WeirdCommand() {
        //given
        String givenCommand = "//command:///asd";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/set command " + givenCommand);

        //when
        when(requestService.send((ApiRequest<?>) any())).thenThrow(IllegalInputException.class);
        adminResponse = new AdminResponse(responseFacade, givenUser, quickSender, requestService);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(requestService, times(1)).sendRequestList(any());
        verify(requestService, times(1)).send((ApiRequest<?>) any());
        verify(quickSender, times(0)).message(anyString(), anyString(), anyBoolean());
    }

    @Test
    void response_removeCommand_ValidCommand() {
        //given
        String givenCommand = "menu";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/remove command " + givenCommand);
        List<?> givenBotCommands = getDefaultBotCommands();

        //when
        when(requestService.sendRequestList(any())).thenReturn((List<Object>) givenBotCommands);
        adminResponse = new AdminResponse(responseFacade, givenUser, quickSender, requestService);
        adminResponse.response();

        //then
        verify(requestService, times(1)).send((ApiRequest<?>) any());
        verify(quickSender, times(1)).message(givenUser.getChatId(), "Successfully removed menu command.", false);
    }

    @Test
    void response_removeCommand_ValidCommandButWithSlash() {
        //given
        String givenCommand = "/menu";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/remove command " + givenCommand);
        List<?> givenBotCommands = getDefaultBotCommands();

        //when
        when(requestService.sendRequestList(any())).thenReturn((List<Object>) givenBotCommands);
        adminResponse = new AdminResponse(responseFacade, givenUser, quickSender, requestService);
        adminResponse.response();

        //then
        verify(requestService, times(1)).sendRequestList(any());
        verify(requestService, times(1)).send((ApiRequest<?>) any());
        verify(quickSender, times(1)).message(givenUser.getChatId(), "Successfully removed menu command.", false);
    }

    @Test
    void response_removeCommand_expectThrow_InvalidCommand() {
        //given
        String givenCommand = "invalid";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/remove command " + givenCommand);
        List<?> givenBotCommands = getDefaultBotCommands();

        //when
        when(requestService.sendRequestList(any())).thenReturn((List<Object>) givenBotCommands);
        adminResponse = new AdminResponse(responseFacade, givenUser, quickSender, requestService);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(requestService, times(1)).sendRequestList(any());
        verify(requestService, times(0)).send((ApiRequest<?>) any());
        verify(quickSender, times(0)).message(givenUser.getChatId(), "Successfully removed menu command.", false);
    }

    @Test
    void response_removeAllCommands_JustRemove() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/remove commands all");

        //when
        adminResponse = new AdminResponse(responseFacade, givenUser, quickSender, requestService);
        adminResponse.response();

        //then
        verify(requestService, times(1)).send((ApiRequest<?>) any());
        verify(quickSender, times(1)).message(givenUser.getChatId(), "Successfully removed all commands.", false);
    }

    @Test
    void response_expectNothing_WrongCommand() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/wrong command");

        //when
        adminResponse = new AdminResponse(responseFacade, givenUser, quickSender, requestService);
        adminResponse.response();

        //then
        verify(quickSender, times(0)).message(anyString(), anyString(), anyBoolean());
        verify(requestService, times(0)).send((ApiRequest<?>) any());
    }

    private List<BotCommand> getDefaultBotCommands() {
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("menu", "menu"));
        commands.add(new BotCommand("start", "start"));
        commands.add(new BotCommand("update", "update"));
        commands.add(new BotCommand("exit", "exit"));
        return commands;
    }

    private ActiveUser getDefaultActiveUser() {
        ActiveUser givenUser = new ActiveUser();
        givenUser.setUserId("123");
        givenUser.setChatId("123");
        givenUser.setMessageId(123);
        return givenUser;
    }
}










