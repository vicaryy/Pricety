package com.vicary.zalandoscraper.service.response.admin;

import com.vicary.zalandoscraper.api_telegram.api_object.bot.bot_command.BotCommand;
import com.vicary.zalandoscraper.api_telegram.api_request.ApiRequest;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.api_telegram.service.RequestService;
import com.vicary.zalandoscraper.api_telegram.service.UpdateFetcher;
import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.exception.ScraperBotException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.model.User;
import com.vicary.zalandoscraper.security.Role;
import com.vicary.zalandoscraper.service.UpdateReceiverService;
import com.vicary.zalandoscraper.service.response.ResponseFacade;
import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import com.vicary.zalandoscraper.updater.AutoUpdater;
import com.vicary.zalandoscraper.utils.ApplicationCrasher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;
import java.util.stream.IntStream;

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
    @MockBean
    private ApplicationCrasher applicationCrasher;
    @Mock
    private UpdateFetcher updateFetcher;

    @BeforeAll
    static void beforeAll() {
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages", Locale.of("en")));
    }


    @Test
    void response_setPremium_ValidUser() {
        //given
        String givenUserId = "123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set premium " + givenUserId);

        //when
        when(responseFacade.updateUserToPremiumByUserId(Long.parseLong(givenUserId))).thenReturn(true);
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(responseFacade, times(1)).updateUserToPremiumByUserId(Long.parseLong(givenUserId));
        verify(quickSender, times(1)).message(givenUser.getChatId(), String.format("User %s successfully updated to Premium.", givenUserId), false);
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s does not exist.", givenUserId), false);
    }

    @Test
    void response_setPremium_UserDoesNotExists() {
        //given
        String givenUserId = "123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set premium " + givenUserId);

        //when
        when(responseFacade.updateUserToPremiumByUserId(Long.parseLong(givenUserId))).thenReturn(false);
        adminResponse.set(givenUser, updateFetcher);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(1)).updateUserToPremiumByUserId(Long.parseLong(givenUserId));
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s successfully updated to Premium.", givenUserId), false);
    }

    @Test
    void response_setStandard_ValidUser() {
        //given
        String givenUserId = "123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set standard " + givenUserId);

        //when
        when(responseFacade.updateUserToStandardByUserId(Long.parseLong(givenUserId))).thenReturn(true);
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(responseFacade, times(1)).updateUserToStandardByUserId(Long.parseLong(givenUserId));
        verify(quickSender, times(1)).message(givenUser.getChatId(), String.format("User %s successfully updated to Standard.", givenUserId), false);
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s does not exist.", givenUserId), false);
    }

    @Test
    void response_setStandard_UserDoesNotExists() {
        //given
        String givenUserId = "123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set standard " + givenUserId);

        //when
        when(responseFacade.updateUserToStandardByUserId(Long.parseLong(givenUserId))).thenReturn(false);
        adminResponse.set(givenUser, updateFetcher);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(1)).updateUserToStandardByUserId(Long.parseLong(givenUserId));
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s successfully updated to Standard.", givenUserId), false);
    }

    @Test
    void response_setAdmin_ValidUser() {
        //given
        String givenUserId = "123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set admin " + givenUserId);

        //when
        when(responseFacade.updateUserToAdminByUserId(Long.parseLong(givenUserId))).thenReturn(true);
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(responseFacade, times(1)).updateUserToAdminByUserId(Long.parseLong(givenUserId));
        verify(quickSender, times(1)).message(givenUser.getChatId(), String.format("User %s successfully updated to Admin.", givenUserId), false);
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s does not exist.", givenUserId), false);
    }

    @Test
    void response_setAdmin_UserDoesNotExists() {
        //given
        String givenUserId = "123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set admin " + givenUserId);

        //when
        when(responseFacade.updateUserToAdminByUserId(Long.parseLong(givenUserId))).thenReturn(false);
        adminResponse.set(givenUser, updateFetcher);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(1)).updateUserToAdminByUserId(Long.parseLong(givenUserId));
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s successfully updated to Admin.", givenUserId), false);
    }

    @Test
    void response_setNonAdmin_ValidUser() {
        //given
        String givenUserId = "123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set non-admin " + givenUserId);

        //when
        when(responseFacade.updateUserToNonAdminByUserId(Long.parseLong(givenUserId))).thenReturn(true);
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(responseFacade, times(1)).updateUserToNonAdminByUserId(Long.parseLong(givenUserId));
        verify(quickSender, times(1)).message(givenUser.getChatId(), String.format("User %s successfully updated to Non-Admin.", givenUserId), false);
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s does not exist.", givenUserId), false);
    }

    @Test
    void response_setNonAdmin_UserDoesNotExists() {
        //given
        String givenUserId = "123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set non-admin " + givenUserId);

        //when
        when(responseFacade.updateUserToNonAdminByUserId(Long.parseLong(givenUserId))).thenReturn(false);
        adminResponse.set(givenUser, updateFetcher);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(1)).updateUserToNonAdminByUserId(Long.parseLong(givenUserId));
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s successfully updated to Non-Admin.", givenUserId), false);
    }

    @Test
    void response_setCommand_ValidCommand() {
        //given
        String givenCommand = "/command:description";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set command " + givenCommand);

        //when
        adminResponse.set(givenUser, updateFetcher);
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
        givenUser.setText("//set command " + givenCommand);

        //when
        adminResponse.set(givenUser, updateFetcher);

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
        givenUser.setText("//set command " + givenCommand);

        //when
        adminResponse.set(givenUser, updateFetcher);

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
        givenUser.setText("//set command " + givenCommand);

        //when
        adminResponse.set(givenUser, updateFetcher);

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
        givenUser.setText("//set command " + givenCommand);

        //when
        adminResponse.set(givenUser, updateFetcher);

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
        givenUser.setText("//set command " + givenCommand);

        //when
        when(requestService.send((ApiRequest<?>) any())).thenThrow(IllegalInputException.class);
        adminResponse.set(givenUser, updateFetcher);

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
        givenUser.setText("//delete command " + givenCommand);
        List<?> givenBotCommands = getDefaultBotCommands();

        //when
        when(requestService.sendRequestList(any())).thenReturn((List<Object>) givenBotCommands);
        adminResponse.set(givenUser, updateFetcher);
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
        givenUser.setText("//delete command " + givenCommand);
        List<?> givenBotCommands = getDefaultBotCommands();

        //when
        when(requestService.sendRequestList(any())).thenReturn((List<Object>) givenBotCommands);
        adminResponse.set(givenUser, updateFetcher);
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
        givenUser.setText("//delete command " + givenCommand);
        List<?> givenBotCommands = getDefaultBotCommands();

        //when
        when(requestService.sendRequestList(any())).thenReturn((List<Object>) givenBotCommands);
        adminResponse.set(givenUser, updateFetcher);

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
        givenUser.setText("//delete command all");

        //when
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(requestService, times(1)).send((ApiRequest<?>) any());
        verify(quickSender, times(1)).message(givenUser.getChatId(), "Successfully removed all commands.", false);
    }

    @Test
    void response_expectNothing_WrongCommand() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//wrong command");

        //when
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(quickSender, times(0)).message(anyString(), anyString(), anyBoolean());
        verify(requestService, times(0)).send((ApiRequest<?>) any());
    }

    @Test
    void response_startUpdated_JustStart() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//update start");

        //when
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(quickSender, times(1)).message(givenUser.getChatId(), "Auto Updater started successfully.", false);

        verify(autoUpdater, times(1)).start();
    }

    @Test
    void response_startUpdated_ThrowsException() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//update start");

        //when
        doThrow(new ScraperBotException("asd", "dsa")).when(autoUpdater).start();
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(autoUpdater, times(1)).start();
        verify(quickSender, times(1)).message(givenUser.getChatId(), "asd", false);
    }

    @Test
    void response_stopUpdated_JustStop() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//update stop");

        //when
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(quickSender, times(1)).message(givenUser.getChatId(), "Auto Updater stopped.", false);

        verify(autoUpdater, times(1)).stop();
    }

    @Test
    void response_stopUpdated_ThrowsException() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//update stop");

        //when
        doThrow(new ScraperBotException("asd", "dsa")).when(autoUpdater).stop();
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(autoUpdater, times(1)).stop();
        verify(quickSender, times(1)).message(givenUser.getChatId(), "asd", false);
    }

    @Test
    void response_startOnceUpdated_JustStart() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//update start once");

        //when
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(quickSender, times(1)).message(givenUser.getChatId(), "Auto Updater Once started successfully.", false);
        verify(autoUpdater, times(1)).startOnce();
    }

    @Test
    void response_startOnceUpdated_ThrowsException() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//update start once");

        //when
        doThrow(new ScraperBotException("asd", "dsa")).when(autoUpdater).startOnce();
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(autoUpdater, times(1)).startOnce();
        verify(quickSender, times(1)).message(givenUser.getChatId(), "asd", false);
    }

    @Test
    void response_getState_JustGetState() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//update state");

        //when
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(autoUpdater, times(1)).getCurrentState();
        verify(quickSender, times(1)).message(eq(givenUser.getChatId()), anyString(), eq(false));
    }

    @Test
    void response_getAll_JustGetAllCommands() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//get all");

        //when
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(quickSender, times(1)).message(eq(givenUser.getChatId()), anyString(), eq(true));
    }

    @Test
    void response_sendMessage_SendMessageToUser() {
        //given
        UserEntity givenUserEntity = getDefaultUserEntity();
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("""
                //send message 1234 text text text
                text
                *text*
                text.""");
        String givenTo = "1234";
        String givenText = """
                text text text
                text
                *text*
                text\\.""";

        //when
        when(responseFacade.getUser(Long.parseLong(givenTo))).thenReturn(givenUserEntity);
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(responseFacade, times(1)).getUser(Long.parseLong(givenTo));
        verify(responseFacade, times(0)).getAllUsers();
        verify(quickSender, times(1)).message(givenTo, givenText, true);
    }

    @Test
    void response_sendMessage_SendMessageToAllUsers() {
        //given
        List<UserEntity> givenUserEntity = List.of(
                getUserEntity("1"),
                getUserEntity("2"),
                getUserEntity("3"),
                getUserEntity("4"),
                getUserEntity("5"));
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("""
                //send message all text text text
                text
                *text*
                text.""");
        String givenTo = "1234";
        String givenText = """
                text text text
                text
                *text*
                text\\.""";

        //when
        when(responseFacade.getAllUsers()).thenReturn(givenUserEntity);
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(responseFacade, times(0)).getUser(Long.parseLong(givenTo));
        verify(responseFacade, times(1)).getAllUsers();
        verify(quickSender, times(1)).message("1", givenText, true);
        verify(quickSender, times(1)).message("2", givenText, true);
        verify(quickSender, times(1)).message("3", givenText, true);
        verify(quickSender, times(1)).message("4", givenText, true);
        verify(quickSender, times(1)).message("5", givenText, true);
    }

    @Test
    void response_sendMessage_SendMessageToAllUsersWithMultiLanguage() {
        //given
        List<UserEntity> givenUserEntity = List.of(
                getUserEntity("1", "en"),
                getUserEntity("2", "en"),
                getUserEntity("3", "pl"),
                getUserEntity("4", "en"),
                getUserEntity("5", "pl"));
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//send message all tekst po polsku -en- text in english");
        String givenTo = "1234";
        String givenTextPL = "tekst po polsku";
        String givenTextEN = "text in english";

        //when
        when(responseFacade.getAllUsers()).thenReturn(givenUserEntity);
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(responseFacade, times(0)).getUser(Long.parseLong(givenTo));
        verify(responseFacade, times(1)).getAllUsers();
        verify(quickSender, times(1)).message("1", givenTextEN, true);
        verify(quickSender, times(1)).message("2", givenTextEN, true);
        verify(quickSender, times(1)).message("3", givenTextPL, true);
        verify(quickSender, times(1)).message("4", givenTextEN, true);
        verify(quickSender, times(1)).message("5", givenTextPL, true);
    }

    @Test
    void response_sendMessage_SendMessageButEmptyText() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("""
                //send message all """);

        //when
        adminResponse.set(givenUser, updateFetcher);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(0)).getUser(anyLong());
        verify(responseFacade, times(0)).getAllUsers();
        verify(quickSender, times(0)).message(anyString(), anyString(), anyBoolean());
    }

    @Test
    void response_sendMessage_SendMessageButEmptyUserAndText() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("""
                //send message""");

        //when
        adminResponse.set(givenUser, updateFetcher);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(0)).getUser(anyLong());
        verify(responseFacade, times(0)).getAllUsers();
        verify(quickSender, times(0)).message(anyString(), anyString(), anyBoolean());
    }


    @Test
    void response_getUser_displayOneUser() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("""
                //get user 1234""");
        String givenUserId = "1234";
        UserEntity givenUserEntity = getDefaultUserEntity();
        String expectedText = """
                *User nr\\. 1*
                    id: 1234
                    nick: nick\\_
                    email: email
                    nationality: pl
                    premium: true
                    admin: false
                    notifyByEmail: true
                    verifiedEmail: true""";

        //when
        when(responseFacade.getUser(Long.parseLong(givenUserId))).thenReturn(givenUserEntity);
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedText, true);
    }

    @Test
    void response_getUser_displayThreeUsers() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("""
                //get user all""");
        List<UserEntity> givenUserEntities = getListOfDefaultUserEntity(3);
        String expectedText1 = """
                *User nr\\. 1*
                    id: 1234
                    nick: nick\\_
                    email: email
                    nationality: pl
                    premium: true
                    admin: false
                    notifyByEmail: true
                    verifiedEmail: true""";
        String expectedText2 = """
                *User nr\\. 2*
                    id: 1234
                    nick: nick\\_
                    email: email
                    nationality: pl
                    premium: true
                    admin: false
                    notifyByEmail: true
                    verifiedEmail: true""";
        String expectedText3 = """
                *User nr\\. 3*
                    id: 1234
                    nick: nick\\_
                    email: email
                    nationality: pl
                    premium: true
                    admin: false
                    notifyByEmail: true
                    verifiedEmail: true""";

        //when
        when(responseFacade.getAllUsers()).thenReturn(givenUserEntities);
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedText1, true);
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedText2, true);
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedText3, true);
    }

    @Test
    void response_startReceiver() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//start");

        //when
        when(updateFetcher.isReceiverRunning()).thenReturn(false);
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(updateFetcher, times(1)).isReceiverRunning();
        verify(updateFetcher, times(1)).setReceiverRunning(true);
        verify(quickSender, times(1)).message(givenUser.getChatId(), "Receiver started.", false);
    }

    @Test
    void response_startReceiverButAlreadyRunning() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//start");

        //when
        when(updateFetcher.isReceiverRunning()).thenReturn(true);
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(updateFetcher, times(1)).isReceiverRunning();
        verify(updateFetcher, times(0)).setReceiverRunning(true);
        verify(quickSender, times(1)).message(givenUser.getChatId(), "Receiver is already running.", false);
    }

    @Test
    void response_stopReceiver() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//stop");

        //when
        when(updateFetcher.isReceiverRunning()).thenReturn(true);
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(updateFetcher, times(1)).isReceiverRunning();
        verify(updateFetcher, times(1)).setReceiverRunning(false);
        verify(quickSender, times(1)).message(givenUser.getChatId(), "Receiver stopped.", false);
    }

    @Test
    void response_stopReceiverButStoppedAlready() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//stop");

        //when
        when(updateFetcher.isReceiverRunning()).thenReturn(false);
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(updateFetcher, times(1)).isReceiverRunning();
        verify(updateFetcher, times(0)).setReceiverRunning(false);
        verify(quickSender, times(1)).message(givenUser.getChatId(), "Receiver is already stopped.", false);
    }

    @Test
    void response_crashApplication() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//crash");

        //when
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(applicationCrasher, times(1)).crash();
        verify(quickSender, times(1)).message(givenUser.getChatId(), "Crashing application...", false);
    }

    @Test
    void response_setNick_validParams() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set nick 12345 delfin");

        //when
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(responseFacade, times(1)).updateUserNick(12345, "delfin");
        verify(quickSender, times(1)).message(givenUser.getChatId(), "UserId: 12345 nick updated to delfin successfully.", false);
    }

    @Test
    void response_setNick_NoUserId() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set nick delfin");

        //when
        adminResponse.set(givenUser, updateFetcher);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(0)).updateUserNick(anyLong(), anyString());
        verify(quickSender, times(0)).message(anyString(), anyString(), anyBoolean());
    }

    @Test
    void response_setNick_NoNick() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set nick 12345 ");

        //when
        adminResponse.set(givenUser, updateFetcher);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(0)).updateUserNick(anyLong(), anyString());
        verify(quickSender, times(0)).message(anyString(), anyString(), anyBoolean());
    }

    @Test
    void response_setNick_NoUserIdAndNick() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set nick ");

        //when
        adminResponse.set(givenUser, updateFetcher);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(0)).updateUserNick(anyLong(), anyString());
        verify(quickSender, times(0)).message(anyString(), anyString(), anyBoolean());
    }

    @Test
    void response_setNick_MoreNicks() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set nick 12345 nick1 nick2 nick3");

        //when
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(responseFacade, times(1)).updateUserNick(12345, "nick1");
        verify(quickSender, times(1)).message(givenUser.getChatId(), "UserId: 12345 nick updated to nick1 successfully.", false);
    }

    @Test
    void response_deleteUser_JustDelete() {
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//delete user 12345");

        //when
        when(responseFacade.isUserExists(12345)).thenReturn(true);
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(responseFacade, times(1)).deleteUser(12345);
        verify(quickSender, times(1)).message(givenUser.getChatId(), "User deleted.", false);
    }

    @Test
    void response_deleteUser_UserNotFound() {
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//delete user 12345");

        //when
        when(responseFacade.isUserExists("12345")).thenReturn(false);
        adminResponse.set(givenUser, updateFetcher);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(0)).deleteUser(12345);
        verify(quickSender, times(0)).message(givenUser.getChatId(), "User deleted.", false);
    }


    @Test
    void response_deleteUser_UserIdEmpty() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//delete user   ");

        //when
        adminResponse.set(givenUser, updateFetcher);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(0)).deleteUser(12345);
        verify(quickSender, times(0)).message(givenUser.getChatId(), "User deleted.", false);
    }

    @Test
    void response_deleteProduct_JustDelete() {
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//delete product 12345");

        //when
        when(responseFacade.isProductExists("12345")).thenReturn(true);
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(responseFacade, times(1)).deleteProduct(12345L);
        verify(quickSender, times(1)).message(givenUser.getChatId(), "Product deleted.", false);
    }

    @Test
    void response_deleteProduct_UserNotFound() {
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//delete product 12345");

        //when
        when(responseFacade.isProductExists("12345")).thenReturn(false);
        adminResponse.set(givenUser, updateFetcher);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(0)).deleteProduct(12345L);
        verify(quickSender, times(0)).message(givenUser.getChatId(), "Product deleted.", false);
    }


    @Test
    void response_deleteProduct_UserIdEmpty() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//delete product   ");

        //when
        adminResponse.set(givenUser, updateFetcher);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(0)).deleteProduct(12345L);
        verify(quickSender, times(0)).message(givenUser.getChatId(), "Product deleted.", false);
    }

    @Test
    void response_getProduct_AllProductsThreeProducts() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//get product all");
        List<Product> givenProducts = getListOfDefaultProducts(3);
        String expectedFirstProduct = """
                *Product nr\\. 1*
                    productId: 123
                    userId: 1234
                    name: name
                    description: desc\\_
                    variant: M
                    price: 100,00
                    priceAlert: AUTO
                    currency: zł
                    link: [service․com](link)""";
        String expectedSecondProduct = """
                *Product nr\\. 2*
                    productId: 123
                    userId: 1234
                    name: name
                    description: desc\\_
                    variant: M
                    price: 100,00
                    priceAlert: AUTO
                    currency: zł
                    link: [service․com](link)""";
        String expectedThirdProduct = """
                *Product nr\\. 3*
                    productId: 123
                    userId: 1234
                    name: name
                    description: desc\\_
                    variant: M
                    price: 100,00
                    priceAlert: AUTO
                    currency: zł
                    link: [service․com](link)""";

        //when
        when(responseFacade.getAllProducts()).thenReturn(givenProducts);
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedFirstProduct, true);
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedSecondProduct, true);
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedThirdProduct, true);
    }

    @Test
    void response_getProduct_NoProducts() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//get product all");

        //when
        when(responseFacade.getAllProducts()).thenReturn(Collections.emptyList());
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(quickSender, times(1)).message(givenUser.getChatId(), "No products to display.", true);
        verify(responseFacade, times(0)).isProductExists(anyString());
        verify(responseFacade, times(0)).getProductById(1234L);
    }

    @Test
    void response_getProduct_OneProduct() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//get product 1234");
        Product givenProducts = getDefaultProduct();
        String expectedProduct = """
                *Product nr\\. 1*
                    productId: 123
                    userId: 1234
                    name: name
                    description: desc\\_
                    variant: M
                    price: 100,00
                    priceAlert: AUTO
                    currency: zł
                    link: [service․com](link)""";

        //when
        when(responseFacade.isProductExists("1234")).thenReturn(true);
        when(responseFacade.getProductById(1234L)).thenReturn(givenProducts);
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(responseFacade, times(1)).isProductExists("1234");
        verify(responseFacade, times(1)).getProductById(1234L);
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedProduct, true);
    }

    @Test
    void response_getProduct_ProductDoesNotExists() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//get product 1234");

        //when
        when(responseFacade.isProductExists("1234")).thenReturn(false);
        adminResponse.set(givenUser, updateFetcher);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(1)).isProductExists("1234");
        verify(responseFacade, times(0)).getProductById(1234L);
    }

    @Test
    void response_getProduct_EmptyUserId() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//get product   ");

        //when
        when(responseFacade.isProductExists("1234")).thenReturn(false);
        adminResponse.set(givenUser, updateFetcher);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(0)).isProductExists(anyString());
        verify(quickSender, times(0)).message(givenUser.getChatId(), "Product does not exists.", false);
        verify(responseFacade, times(0)).getProductById(1234L);
    }


    @Test
    void response_getProductUser_DisplayUsersProducts() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//get productUser 1234");
        List<Product> givenProducts = getListOfDefaultProducts(3);
        String expectedFirstProduct = """
                *Product nr\\. 1*
                    productId: 123
                    userId: 1234
                    name: name
                    description: desc\\_
                    variant: M
                    price: 100,00
                    priceAlert: AUTO
                    currency: zł
                    link: [service․com](link)""";
        String expectedSecondProduct = """
                *Product nr\\. 2*
                    productId: 123
                    userId: 1234
                    name: name
                    description: desc\\_
                    variant: M
                    price: 100,00
                    priceAlert: AUTO
                    currency: zł
                    link: [service․com](link)""";
        String expectedThirdProduct = """
                *Product nr\\. 3*
                    productId: 123
                    userId: 1234
                    name: name
                    description: desc\\_
                    variant: M
                    price: 100,00
                    priceAlert: AUTO
                    currency: zł
                    link: [service․com](link)""";

        //when
        when(responseFacade.isUserExists(1234)).thenReturn(true);
        when(responseFacade.getAllProductsByUserId(1234)).thenReturn(givenProducts);
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedFirstProduct, true);
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedSecondProduct, true);
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedThirdProduct, true);
    }

    @Test
    void response_getProductUser_UserDontHaveProducts() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//get productUser 1234");
        List<Product> givenProducts = Collections.emptyList();

        //when
        when(responseFacade.isUserExists(1234)).thenReturn(true);
        when(responseFacade.getAllProductsByUserId(1234)).thenReturn(givenProducts);
        adminResponse.set(givenUser, updateFetcher);
        adminResponse.response();

        //then
        verify(quickSender, times(1)).message(givenUser.getChatId(), "No products to display.", true);
    }

    @Test
    void response_getProductUser_NoUserInCommand() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//get productUser    ");

        //when
        adminResponse.set(givenUser, updateFetcher);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(quickSender, times(0)).message(anyString(), anyString(), anyBoolean());
    }

    @Test
    void response_getProductUser_UserDoesNotExists() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//get productUser 1234");

        //when
        when(responseFacade.isUserExists("1234")).thenReturn(false);
        adminResponse.set(givenUser, updateFetcher);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(1)).isUserExists(1234);
        verify(quickSender, times(0)).message(anyString(), anyString(), anyBoolean());
    }

    private Product getDefaultProduct() {
        return Product.builder()
                .productId(123L)
                .name("name")
                .description("desc_")
                .price(100)
                .newPrice(0)
                .currency("zł")
                .variant("M")
                .priceAlert("AUTO")
                .link("link")
                .serviceName("service.com")
                .user(getDefaultUser())
                .build();
    }

    private User getDefaultUser() {
        return User.builder()
                .userId("1234")
                .build();
    }

    private UserEntity getDefaultUserEntity() {
        return UserEntity.builder()
                .telegramId("1234")
                .email("email")
                .nick("nick_")
                .nationality("pl")
                .premium(true)
                .role(Role.USER)
                .emailNotifications(true)
                .verifiedEmail(true)
                .build();
    }

    private List<UserEntity> getListOfDefaultUserEntity(int size) {
        return IntStream.range(0, size)
                .mapToObj(i -> getDefaultUserEntity())
                .toList();
    }

    private List<Product> getListOfDefaultProducts(int size) {
        return IntStream.range(0, size)
                .mapToObj(i -> getDefaultProduct())
                .toList();
    }

    private UserEntity getUserEntity(String userId) {
        return UserEntity.builder()
                .telegramId(userId)
                .build();
    }

    private UserEntity getUserEntity(String userId, String language) {
        return UserEntity.builder()
                .telegramId(userId)
                .nationality(language)
                .build();
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
        givenUser.setUserId(123);
        givenUser.setTelegramId("123");
        givenUser.setChatId("123");
        givenUser.setMessageId(123);
        return givenUser;
    }
}