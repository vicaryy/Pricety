package com.vicary.zalandoscraper.service;

import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import com.vicary.zalandoscraper.api_telegram.api_object.Update;
import com.vicary.zalandoscraper.api_telegram.api_object.User;
import com.vicary.zalandoscraper.api_telegram.api_object.message.Message;
import com.vicary.zalandoscraper.entity.ActiveRequestEntity;
import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.exception.ActiveUserException;
import com.vicary.zalandoscraper.exception.ZalandoScraperBotException;
import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.pattern.Pattern;
import com.vicary.zalandoscraper.service.repository_services.ActiveRequestService;
import com.vicary.zalandoscraper.service.repository_services.AwaitedMessageService;
import com.vicary.zalandoscraper.service.repository_services.MessageService;
import com.vicary.zalandoscraper.service.repository_services.UserService;
import com.vicary.zalandoscraper.service.map.UserMapper;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.Locale;
import java.util.ResourceBundle;


@Service
@RequiredArgsConstructor
public class UserAuthentication {
    private final static Logger logger = LoggerFactory.getLogger(UserAuthentication.class);

    private final UserService userService;

    private final ActiveRequestService activeRequestService;

    private final AwaitedMessageService awaitedMessageService;

    private final UserMapper userMapper;

    private final MessageService messageService;

    private final QuickSender quickSender;


    public ActiveUser authenticate(Update update) {
        Message message = update.getCallbackQuery() == null
                ? update.getMessage()
                : update.getCallbackQuery().getMessage();

        String chatId = message.getChat().getId();

        int messageId = message.getMessageId();

        String text = update.getCallbackQuery() == null
                ? update.getMessage().getText()
                : update.getCallbackQuery().getData();

        boolean awaitedMessage = isAwaitedMessage(chatId);


        UserEntity userEntity = checkUserInRepository(message.getFrom(), chatId);

        saveMessageToRepository(userEntity, text);

        setActiveLanguage(userEntity.getNationality());

        checkActiveUser(chatId);

        return setActiveUserInThread(userEntity, text, chatId, messageId, awaitedMessage);
    }

    private boolean isAwaitedMessage(String chatId) {
        return awaitedMessageService.existsByUserId(chatId);
    }

    private void checkActiveUser(String chatId) {
        if (activeRequestService.existsByUserId(chatId)) {
            logger.info("User %s is trying to do more than one request".formatted(chatId));
            quickSender.popupMessage(chatId, Messages.other("oneRequest"));
            throw new ActiveUserException();
        }
        activeRequestService.saveActiveUser(new ActiveRequestEntity(chatId));
    }

    private UserEntity checkUserInRepository(User user, String chatId) {
        if (userService.existsByUserId(chatId))
            return userService.findByUserId(chatId);

        UserEntity userEntity = userMapper.map(user);
        userService.saveUser(userEntity);
        return userEntity;
    }

    private void saveMessageToRepository(UserEntity user, String text) {
        messageService.saveEntity(user, text);
    }

    private ActiveUser setActiveUserInThread(UserEntity userEntity, String text, String chatId, int messageId, boolean awaitedMessage) {
        ActiveUser activeUser = ActiveUser.get();
        activeUser.setUserId(userEntity.getUserId());
        activeUser.setChatId(chatId);
        activeUser.setMessageId(messageId);
        activeUser.setText(text.trim());
        activeUser.setPremium(userEntity.isPremium());
        activeUser.setNick(userEntity.getNick());
        activeUser.setNotifyByEmail(userEntity.isNotifyByEmail());
        activeUser.setAwaitedMessage(awaitedMessage);
        activeUser.setEmail(userEntity.getEmail());
        activeUser.setVerifiedEmail(userEntity.isVerifiedEmail());
        activeUser.setAdmin(userEntity.isAdmin());
        return activeUser;
    }

    private void setActiveLanguage(String l) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", Locale.of(l));
//        ResourceBundle.clearCache(Thread.currentThread().getContextClassLoader());
        ActiveLanguage.get().setResourceBundle(resourceBundle);
    }
}
