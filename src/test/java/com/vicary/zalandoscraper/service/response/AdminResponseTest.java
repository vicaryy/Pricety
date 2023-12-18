package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.api_telegram.api_object.bot.bot_command.BotCommand;
import com.vicary.zalandoscraper.api_telegram.api_request.ApiRequest;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.api_telegram.service.RequestService;
import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.exception.ZalandoScraperBotException;
import com.vicary.zalandoscraper.service.UpdateReceiverService;
import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import com.vicary.zalandoscraper.updater.AutoUpdater;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
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


    @BeforeAll
    static void beforeAll() {
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages", Locale.of("en")));
    }


    @Test
    void response_setPremium_ValidUser() {
        //given
        String givenUserId = "user123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set premium " + givenUserId);

        //when
        when(responseFacade.updateUserToPremiumByUserId(givenUserId)).thenReturn(true);
        adminResponse.setActiveUser(givenUser);
        adminResponse.response();

        //then
        verify(responseFacade, times(1)).updateUserToPremiumByUserId(givenUserId);
        verify(quickSender, times(1)).message(givenUser.getChatId(), String.format("User %s successfully updated to Premium.", givenUserId), false);
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s does not exist.", givenUserId), false);
    }

    @Test
    void response_setPremium_UserDoesNotExists() {
        //given
        String givenUserId = "user123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set premium " + givenUserId);

        //when
        when(responseFacade.updateUserToPremiumByUserId(givenUserId)).thenReturn(false);
        adminResponse.setActiveUser(givenUser);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(1)).updateUserToPremiumByUserId(givenUserId);
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s successfully updated to Premium.", givenUserId), false);
    }

    @Test
    void response_setStandard_ValidUser() {
        //given
        String givenUserId = "user123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set standard " + givenUserId);

        //when
        when(responseFacade.updateUserToStandardByUserId(givenUserId)).thenReturn(true);
        adminResponse.setActiveUser(givenUser);
        adminResponse.response();

        //then
        verify(responseFacade, times(1)).updateUserToStandardByUserId(givenUserId);
        verify(quickSender, times(1)).message(givenUser.getChatId(), String.format("User %s successfully updated to Standard.", givenUserId), false);
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s does not exist.", givenUserId), false);
    }

    @Test
    void response_setStandard_UserDoesNotExists() {
        //given
        String givenUserId = "user123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set standard " + givenUserId);

        //when
        when(responseFacade.updateUserToStandardByUserId(givenUserId)).thenReturn(false);
        adminResponse.setActiveUser(givenUser);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(1)).updateUserToStandardByUserId(givenUserId);
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s successfully updated to Standard.", givenUserId), false);
    }

    @Test
    void response_setAdmin_ValidUser() {
        //given
        String givenUserId = "user123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set admin " + givenUserId);

        //when
        when(responseFacade.updateUserToAdminByUserId(givenUserId)).thenReturn(true);
        adminResponse.setActiveUser(givenUser);
        adminResponse.response();

        //then
        verify(responseFacade, times(1)).updateUserToAdminByUserId(givenUserId);
        verify(quickSender, times(1)).message(givenUser.getChatId(), String.format("User %s successfully updated to Admin.", givenUserId), false);
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s does not exist.", givenUserId), false);
    }

    @Test
    void response_setAdmin_UserDoesNotExists() {
        //given
        String givenUserId = "user123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set admin " + givenUserId);

        //when
        when(responseFacade.updateUserToAdminByUserId(givenUserId)).thenReturn(false);
        adminResponse.setActiveUser(givenUser);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(1)).updateUserToAdminByUserId(givenUserId);
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s successfully updated to Admin.", givenUserId), false);
    }

    @Test
    void response_setNonAdmin_ValidUser() {
        //given
        String givenUserId = "user123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set non-admin " + givenUserId);

        //when
        when(responseFacade.updateUserToNonAdminByUserId(givenUserId)).thenReturn(true);
        adminResponse.setActiveUser(givenUser);
        adminResponse.response();

        //then
        verify(responseFacade, times(1)).updateUserToNonAdminByUserId(givenUserId);
        verify(quickSender, times(1)).message(givenUser.getChatId(), String.format("User %s successfully updated to Non-Admin.", givenUserId), false);
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s does not exist.", givenUserId), false);
    }

    @Test
    void response_setNonAdmin_UserDoesNotExists() {
        //given
        String givenUserId = "user123";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set non-admin " + givenUserId);

        //when
        when(responseFacade.updateUserToNonAdminByUserId(givenUserId)).thenReturn(false);
        adminResponse.setActiveUser(givenUser);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(1)).updateUserToNonAdminByUserId(givenUserId);
        verify(quickSender, times(0)).message(givenUser.getChatId(), String.format("User %s successfully updated to Non-Admin.", givenUserId), false);
    }

    @Test
    void response_setCommand_ValidCommand() {
        //given
        String givenCommand = "/command:description";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("//set command " + givenCommand);

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
        givenUser.setText("//set command " + givenCommand);

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
        givenUser.setText("//set command " + givenCommand);

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
        givenUser.setText("//set command " + givenCommand);

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
        givenUser.setText("//set command " + givenCommand);

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
        givenUser.setText("//set command " + givenCommand);

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
        givenUser.setText("//delete command " + givenCommand);
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
        givenUser.setText("//delete command " + givenCommand);
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
        givenUser.setText("//delete command " + givenCommand);
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
        givenUser.setText("//delete command all");

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
        givenUser.setText("//wrong command");

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
        givenUser.setText("//update start");

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
        givenUser.setText("//update start");

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
        givenUser.setText("//update stop");

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
        givenUser.setText("//update stop");

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
        givenUser.setText("//update start once");

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
        givenUser.setText("//update start once");

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
        givenUser.setText("//update state");

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
        givenUser.setText("//get all");

        //when
        adminResponse.setActiveUser(givenUser);
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
        when(responseFacade.getUserByUserId(givenTo)).thenReturn(givenUserEntity);
        adminResponse.setActiveUser(givenUser);
        adminResponse.response();

        //then
        verify(responseFacade, times(1)).getUserByUserId(givenTo);
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
        adminResponse.setActiveUser(givenUser);
        adminResponse.response();

        //then
        verify(responseFacade, times(0)).getUserByUserId(givenTo);
        verify(responseFacade, times(1)).getAllUsers();
        verify(quickSender, times(1)).message("1", givenText, true);
        verify(quickSender, times(1)).message("2", givenText, true);
        verify(quickSender, times(1)).message("3", givenText, true);
        verify(quickSender, times(1)).message("4", givenText, true);
        verify(quickSender, times(1)).message("5", givenText, true);
    }

    @Test
    void response_sendMessage_SendMessageButEmptyText() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("""
                //send message all """);

        //when
        adminResponse.setActiveUser(givenUser);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(0)).getUserByUserId(anyString());
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
        adminResponse.setActiveUser(givenUser);

        //then
        assertThrows(IllegalInputException.class, () -> adminResponse.response());
        verify(responseFacade, times(0)).getUserByUserId(anyString());
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
                    nick: nick
                    email: email
                    nationality: pl
                    premium: true
                    admin: false
                    notifyByEmail: true
                    verifiedEmail: true""";

        //when
        when(responseFacade.getUserByUserId(givenUserId)).thenReturn(givenUserEntity);
        adminResponse.setActiveUser(givenUser);
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
                    nick: nick
                    email: email
                    nationality: pl
                    premium: true
                    admin: false
                    notifyByEmail: true
                    verifiedEmail: true""";
        String expectedText2 = """
                *User nr\\. 2*
                    id: 1234
                    nick: nick
                    email: email
                    nationality: pl
                    premium: true
                    admin: false
                    notifyByEmail: true
                    verifiedEmail: true""";
        String expectedText3 = """
                *User nr\\. 3*
                    id: 1234
                    nick: nick
                    email: email
                    nationality: pl
                    premium: true
                    admin: false
                    notifyByEmail: true
                    verifiedEmail: true""";

        //when
        when(responseFacade.getAllUsers()).thenReturn(givenUserEntities);
        adminResponse.setActiveUser(givenUser);
        adminResponse.response();

        //then
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedText1, true);
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedText2, true);
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedText3, true);
    }

//    @Test
//    void response_startReceiver() {
//        //given
//        ActiveUser givenUser = getDefaultActiveUser();
//        givenUser.setText("//start");
//
//        //when
//        adminResponse.setActiveUser(givenUser);
//        adminResponse.response();
//
//        //then
//        verify(updateReceiverService, times(1)).running(true);
//        verify(quickSender, times(1)).message(givenUser.getChatId(), "Receiver started.", false);
//    }
//
//    @Test
//    void response_stopReceiver() {
//        //given
//        ActiveUser givenUser = getDefaultActiveUser();
//        givenUser.setText("//stop");
//
//        //when
//        adminResponse.setActiveUser(givenUser);
//        adminResponse.response();
//
//        //then
//        verify(updateReceiverService, times(1)).running(false);
//        verify(quickSender, times(1)).message(givenUser.getChatId(), "Receiver stopped.", false);
//    }
//
//    @Test
//    void response_crashApplication() {
//        //given
//        ActiveUser givenUser = getDefaultActiveUser();
//        givenUser.setText("//crash");
//
//        //when
//        adminResponse.setActiveUser(givenUser);
//        adminResponse.response();
//
//        //then
//        verify(updateReceiverService, times(1)).crash();
//        verify(quickSender, times(1)).message(givenUser.getChatId(), "Crashing application...", false);
//    }

    private UserEntity getDefaultUserEntity() {
        return UserEntity.builder()
                .userId("1234")
                .email("email")
                .nick("nick")
                .nationality("pl")
                .premium(true)
                .admin(false)
                .notifyByEmail(true)
                .verifiedEmail(true)
                .build();
    }

    private List<UserEntity> getListOfDefaultUserEntity(int size) {
        return IntStream.range(0, size)
                .mapToObj(i -> getDefaultUserEntity())
                .toList();
    }

    private UserEntity getUserEntity(String userId) {
        return UserEntity.builder()
                .userId(userId)
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
        givenUser.setUserId("123");
        givenUser.setChatId("123");
        givenUser.setMessageId(123);
        return givenUser;
    }
}










