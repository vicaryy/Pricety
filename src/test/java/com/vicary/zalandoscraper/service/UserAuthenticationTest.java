package com.vicary.zalandoscraper.service;

import com.vicary.zalandoscraper.api_telegram.api_object.Update;
import com.vicary.zalandoscraper.api_telegram.api_object.User;
import com.vicary.zalandoscraper.api_telegram.api_object.chat.Chat;
import com.vicary.zalandoscraper.api_telegram.api_object.message.Message;
import com.vicary.zalandoscraper.api_telegram.api_object.other.CallbackQuery;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.entity.ActiveRequestEntity;
import com.vicary.zalandoscraper.entity.AwaitedMessageEntity;
import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.exception.ActiveUserException;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.security.Role;
import com.vicary.zalandoscraper.service.map.UserMapper;
import com.vicary.zalandoscraper.service.repository_services.ActiveRequestService;
import com.vicary.zalandoscraper.service.repository_services.AwaitedMessageService;
import com.vicary.zalandoscraper.service.repository_services.MessageService;
import com.vicary.zalandoscraper.service.repository_services.UserService;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserAuthenticationTest {
    @Autowired
    private UserAuthentication authentication;
    @Autowired
    private UserMapper userMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private ActiveRequestService activeRequestService;
    @MockBean
    private AwaitedMessageService awaitedMessageService;
    @MockBean
    private MessageService messageService;
    @MockBean
    private QuickSender quickSender;
    @MockBean
    private UpdateReceiverService updateReceiverService;


    @Test
    void authenticate_NormalAuthentication() {
        //given
        Update givenUpdate = getDefaultUpdate();
        UserEntity givenUserEntity = getDefaultUserEntity();
        ActiveUser expectedActiveUser = getExpectedActiveUser(givenUserEntity);

        //when
        when(userService.existsByTelegramId(givenUpdate.getMessage().getChat().getId())).thenReturn(true);
        when(userService.findByTelegramId(givenUpdate.getMessage().getChat().getId())).thenReturn(givenUserEntity);
        ActiveUser actualActiveUser = authentication.authenticate(givenUpdate);

        //then
        assertEquals(expectedActiveUser, actualActiveUser);
        verify(messageService, times(1)).saveEntity(givenUserEntity, givenUpdate.getMessage().getText());
        verify(activeRequestService, times(1)).saveActiveUser(any(ActiveRequestEntity.class));
        verify(awaitedMessageService, times(0)).saveAwaitedMessage(any());
    }

    @Test
    void authenticate_NormalAuthenticationAsCallbackQuery() {
        //given
        Update givenUpdate = getCallbackUpdate();
        UserEntity givenUserEntity = getDefaultUserEntity();
        ActiveUser expectedActiveUser = getExpectedActiveUserFromCallbackQuery(givenUserEntity);

        //when
        when(userService.existsByTelegramId(givenUpdate.getCallbackQuery().getMessage().getChat().getId())).thenReturn(true);
        when(userService.findByTelegramId(givenUpdate.getCallbackQuery().getMessage().getChat().getId())).thenReturn(givenUserEntity);
        ActiveUser actualActiveUser = authentication.authenticate(givenUpdate);

        //then
        assertEquals(expectedActiveUser, actualActiveUser);
        verify(messageService, times(1)).saveEntity(givenUserEntity, givenUpdate.getCallbackQuery().getData());
        verify(activeRequestService, times(1)).saveActiveUser(any(ActiveRequestEntity.class));
        verify(awaitedMessageService, times(0)).saveAwaitedMessage(any());
    }

    @Test
    void authenticate_UserTryToDoMoreThanOneRequest() {
        //given
        Update givenUpdate = getDefaultUpdate();
        UserEntity givenUserEntity = getDefaultUserEntity();

        //when
        when(activeRequestService.existsByUserId(givenUpdate.getMessage().getChat().getId())).thenReturn(true);
        when(userService.existsByTelegramId(givenUpdate.getMessage().getChat().getId())).thenReturn(true);
        when(userService.findByTelegramId(givenUpdate.getMessage().getChat().getId())).thenReturn(givenUserEntity);

        //then
        assertThrows(ActiveUserException.class, () -> authentication.authenticate(givenUpdate));
        verify(messageService, times(1)).saveEntity(givenUserEntity, givenUpdate.getMessage().getText());
        verify(activeRequestService, times(0)).saveActiveUser(any(ActiveRequestEntity.class));
        verify(awaitedMessageService, times(0)).saveAwaitedMessage(any());
    }

    @Test
    void authenticate_UserTryToDoMoreThanOneRequestAsCallbackQuery() {
        //given
        Update givenUpdate = getCallbackUpdate();
        UserEntity givenUserEntity = getDefaultUserEntity();

        //when
        when(activeRequestService.existsByUserId(givenUpdate.getCallbackQuery().getMessage().getChat().getId())).thenReturn(true);
        when(userService.existsByTelegramId(givenUpdate.getCallbackQuery().getMessage().getChat().getId())).thenReturn(true);
        when(userService.findByTelegramId(givenUpdate.getCallbackQuery().getMessage().getChat().getId())).thenReturn(givenUserEntity);

        //then
        assertThrows(ActiveUserException.class, () -> authentication.authenticate(givenUpdate));
        verify(messageService, times(1)).saveEntity(givenUserEntity, givenUpdate.getCallbackQuery().getData());
        verify(activeRequestService, times(0)).saveActiveUser(any(ActiveRequestEntity.class));
        verify(awaitedMessageService, times(0)).saveAwaitedMessage(any());
    }

    @Test
    void authenticate_UserNickIsInvalid() {
        //given
        Update givenUpdate = getDefaultUpdate();
        String givenNick = "!@#$invalid";
        UserEntity givenUserEntity = getDefaultUserEntity();
        givenUserEntity.setNick(givenNick);
        var givenAwaitedMessage = AwaitedMessageEntity.builder()
                .request("-setNick")
                .userId(givenUpdate.getMessage().getChat().getId())
                .build();

        //when
        doThrow(new IllegalInputException("throw", "throw")).when(userService).validateNick(givenNick);
        when(userService.existsByTelegramId(givenUpdate.getMessage().getChat().getId())).thenReturn(false);
        when(userService.saveUser(givenUpdate.getMessage().getFrom())).thenReturn(givenUserEntity);

        //then
        assertThrows(ActiveUserException.class, () -> authentication.authenticate(givenUpdate));
        verify(awaitedMessageService, times(1)).saveAwaitedMessage(givenAwaitedMessage);
        verify(messageService, times(0)).saveEntity(givenUserEntity, givenUpdate.getMessage().getText());
        verify(activeRequestService, times(0)).saveActiveUser(any(ActiveRequestEntity.class));
    }

    @Test
    void authenticate_UserNickIsInvalidAsCallbackQuery() {
        //given
        Update givenUpdate = getCallbackUpdate();
        String givenNick = "!@#$invalid";
        UserEntity givenUserEntity = getDefaultUserEntity();
        givenUserEntity.setNick(givenNick);
        var givenAwaitedMessage = AwaitedMessageEntity.builder()
                .request("-setNick")
                .userId(givenUpdate.getCallbackQuery().getMessage().getChat().getId())
                .build();

        //when
        doThrow(new IllegalInputException("throw", "throw")).when(userService).validateNick(givenNick);
        when(userService.existsByTelegramId(givenUpdate.getCallbackQuery().getMessage().getChat().getId())).thenReturn(false);
        when(userService.saveUser(givenUpdate.getCallbackQuery().getMessage().getFrom())).thenReturn(givenUserEntity);

        //then
        assertThrows(ActiveUserException.class, () -> authentication.authenticate(givenUpdate));
        verify(awaitedMessageService, times(1)).saveAwaitedMessage(givenAwaitedMessage);
        verify(messageService, times(0)).saveEntity(givenUserEntity, givenUpdate.getCallbackQuery().getMessage().getText());
        verify(activeRequestService, times(0)).saveActiveUser(any(ActiveRequestEntity.class));
    }

    private UserEntity getDefaultUserEntity() {
        return UserEntity.builder()
                .userId(123L)
                .telegramId("123")
                .nick("nick")
                .nationality("pl")
                .role(Role.USER)
                .build();
    }

    private Update getDefaultUpdate() {
        return Update.builder()
                .message(getDefaultMessage())
                .build();
    }

    private Update getCallbackUpdate() {
        return Update.builder()
                .callbackQuery(getDefaultCallbackQuery())
                .build();
    }

    private CallbackQuery getDefaultCallbackQuery() {
        return CallbackQuery.builder()
                .from(getDefaultFromUser())
                .data("data")
                .message(getDefaultMessage())
                .build();
    }

    private Message getDefaultMessage() {
        return Message.builder()
                .messageId(123)
                .text("text")
                .chat(getDefaultChat())
                .from(getDefaultFromUser())
                .build();
    }

    private User getDefaultFromUser() {
        return User.builder()
                .id(1L)
                .username("username")
                .languageCode("pl")
                .build();
    }

    private Chat getDefaultChat() {
        return Chat.builder()
                .id("123")
                .build();
    }

    private ActiveUser getExpectedActiveUser(UserEntity userEntity) {
        ActiveUser activeUser = new ActiveUser();
        activeUser.setUserId(123);
        activeUser.setTelegramId(userEntity.getTelegramId());
        activeUser.setChatId("123");
        activeUser.setMessageId(123);
        activeUser.setText("text");
        activeUser.setNick(userEntity.getNick());
        activeUser.setEmail(userEntity.getEmail());
        return activeUser;
    }

    private ActiveUser getExpectedActiveUserFromCallbackQuery(UserEntity userEntity) {
        ActiveUser activeUser = new ActiveUser();
        activeUser.setUserId(123);
        activeUser.setTelegramId(userEntity.getTelegramId());
        activeUser.setChatId("123");
        activeUser.setMessageId(123);
        activeUser.setText("data");
        activeUser.setNick(userEntity.getNick());
        activeUser.setEmail(userEntity.getEmail());
        return activeUser;
    }
}


























