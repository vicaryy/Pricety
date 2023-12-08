package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.api_telegram.api_object.bot.bot_command.BotCommand;
import com.vicary.zalandoscraper.api_telegram.api_request.ApiRequest;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.api_telegram.service.RequestService;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.exception.ZalandoScraperBotException;
import com.vicary.zalandoscraper.service.UpdateReceiverService;
import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import com.vicary.zalandoscraper.updater.AutoUpdater;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AdminResponseTest {

    @Autowired
    private AdminResponse adminResponse;
    @MockBean
    private ResponseFacade responseFacade;
    @MockBean
    private QuickSender quickSender;
    @MockBean
    private RequestService requestService;
    @MockBean
    private UpdateReceiverService updateReceiverService;
    @MockBean
    private AutoUpdater autoUpdater;


    @BeforeAll
    static void beforeAll() {
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages", Locale.of("en")));
    }


    @Test
    void response_setPremium_ValidUser() {
        //given
        String givenNick = "user123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/set premium " + givenNick);

        //when
        when(responseFacade.updateUserToPremiumByNick(givenNick)).thenReturn(true);
        adminResponse.setActiveUser(givenUser);
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
        adminResponse.setActiveUser(givenUser);

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
        adminResponse.setActiveUser(givenUser);
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
        adminResponse.setActiveUser(givenUser);

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
        adminResponse.setActiveUser(givenUser);
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
        adminResponse.setActiveUser(givenUser);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
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
        adminResponse.setActiveUser(givenUser);
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
        adminResponse.setActiveUser(givenUser);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
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
        adminResponse.setActiveUser(givenUser);
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
        adminResponse.setActiveUser(givenUser);

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
        adminResponse.setActiveUser(givenUser);

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
        adminResponse.setActiveUser(givenUser);

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
        adminResponse.setActiveUser(givenUser);

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
        adminResponse.setActiveUser(givenUser);

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
        adminResponse.setActiveUser(givenUser);
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
        adminResponse.setActiveUser(givenUser);
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
        adminResponse.setActiveUser(givenUser);

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
        adminResponse.setActiveUser(givenUser);
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
        adminResponse.setActiveUser(givenUser);
        adminResponse.response();

        //then
        verify(quickSender, times(0)).message(anyString(), anyString(), anyBoolean());
        verify(requestService, times(0)).send((ApiRequest<?>) any());
    }

    @Test
    void response_startUpdated_JustStart() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/update start");

        //when
        adminResponse.setActiveUser(givenUser);
        adminResponse.response();

        //then
        verify(quickSender, times(1)).message(givenUser.getChatId(), "Auto Updater started successfully.", false);

        verify(autoUpdater, times(1)).start();
    }

    @Test
    void response_startUpdated_ThrowsException() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/update start");

        //when
        doThrow(new ZalandoScraperBotException("asd", "dsa")).when(autoUpdater).start();
        adminResponse.setActiveUser(givenUser);
        adminResponse.response();

        //then
        verify(autoUpdater, times(1)).start();
        verify(quickSender, times(1)).message(givenUser.getChatId(), "asd", false);
    }

    @Test
    void response_stopUpdated_JustStop() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/update stop");

        //when
        adminResponse.setActiveUser(givenUser);
        adminResponse.response();

        //then
        verify(quickSender, times(1)).message(givenUser.getChatId(), "Auto Updater stopped.", false);

        verify(autoUpdater, times(1)).stop();
    }

    @Test
    void response_stopUpdated_ThrowsException() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/update stop");

        //when
        doThrow(new ZalandoScraperBotException("asd", "dsa")).when(autoUpdater).stop();
        adminResponse.setActiveUser(givenUser);
        adminResponse.response();

        //then
        verify(autoUpdater, times(1)).stop();
        verify(quickSender, times(1)).message(givenUser.getChatId(), "asd", false);
    }

    @Test
    void response_startOnceUpdated_JustStart() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/update start once");

        //when
        adminResponse.setActiveUser(givenUser);
        adminResponse.response();

        //then
        verify(quickSender, times(1)).message(givenUser.getChatId(), "Auto Updater Once started successfully.", false);
        verify(autoUpdater, times(1)).startOnce();
    }

    @Test
    void response_startOnceUpdated_ThrowsException() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/update start once");

        //when
        doThrow(new ZalandoScraperBotException("asd", "dsa")).when(autoUpdater).startOnce();
        adminResponse.setActiveUser(givenUser);
        adminResponse.response();

        //then
        verify(autoUpdater, times(1)).startOnce();
        verify(quickSender, times(1)).message(givenUser.getChatId(), "asd", false);
    }

    @Test
    void response_getState_JustGetState() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/update state");

        //when
        adminResponse.setActiveUser(givenUser);
        adminResponse.response();

        //then
        verify(autoUpdater, times(1)).getCurrentState();
        verify(quickSender, times(1)).message(eq(givenUser.getChatId()), anyString(), eq(false));
    }

    @Test
    void response_getAll_JustGetAllCommands() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("/get all");

        //when
        adminResponse.setActiveUser(givenUser);
        adminResponse.response();

        //then
        verify(quickSender, times(1)).message(eq(givenUser.getChatId()), anyString(), eq(true));
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










