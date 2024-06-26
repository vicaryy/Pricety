package com.vicary.pricety.service;

import com.vicary.pricety.entity.AwaitedMessageEntity;
import com.vicary.pricety.entity.UserEntity;
import com.vicary.pricety.format.MarkdownV2;
import com.vicary.pricety.thread_local.ActiveLanguage;
import com.vicary.pricety.thread_local.ActiveUser;
import com.vicary.pricety.api_telegram.api_object.Update;
import com.vicary.pricety.api_telegram.api_object.User;
import com.vicary.pricety.api_telegram.api_object.message.Message;
import com.vicary.pricety.entity.ActiveRequestEntity;
import com.vicary.pricety.exception.ActiveUserException;
import com.vicary.pricety.messages.Messages;
import com.vicary.pricety.service.repository_services.ActiveRequestService;
import com.vicary.pricety.service.repository_services.AwaitedMessageService;
import com.vicary.pricety.service.repository_services.MessageService;
import com.vicary.pricety.service.repository_services.UserService;
import com.vicary.pricety.api_telegram.service.QuickSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.ResourceBundle;


@Service
@RequiredArgsConstructor
public class UserAuthentication {
    private final static Logger logger = LoggerFactory.getLogger(UserAuthentication.class);

    private final UserService userService;

    private final ActiveRequestService activeRequestService;

    private final AwaitedMessageService awaitedMessageService;

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

        UserEntity userEntity;
        if (isUserExistsInRepository(chatId))
            userEntity = getUserFromRepository(chatId);
        else
            userEntity = createNewUser(message.getFrom());

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

    private boolean isUserNickValid(String nick) {
        try {
            userService.validateNick(nick);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private UserEntity createNewUser(User user) {
        UserEntity userEntity = userService.saveUser(user);
        if (isUserNickValid(userEntity.getNick()))
            return userEntity;

        setActiveLanguage(userEntity.getNationality());
        setAwaitedMessageForNick(userEntity.getTelegramId());
        throw new ActiveUserException();
    }

    private void setAwaitedMessageForNick(String userId) {
        awaitedMessageService.saveAwaitedMessage(AwaitedMessageEntity.builder()
                .userId(userId)
                .request("-setNick")
                .build());
        String message = MarkdownV2.applyWithManualBoldAndItalic(Messages.authenticate("setNick"));
        quickSender.message(userId, message, true);
    }

    private boolean isUserExistsInRepository(String chatId) {
        return userService.existsByTelegramId(chatId);
    }

    private UserEntity getUserFromRepository(String chatId) {
        return userService.findByTelegramId(chatId);
    }

    private void saveMessageToRepository(UserEntity user, String text) {
        messageService.saveEntity(user, text);
    }

    private ActiveUser setActiveUserInThread(UserEntity userEntity, String text, String chatId, int messageId, boolean awaitedMessage) {
        ActiveUser activeUser = ActiveUser.get();
        activeUser.setUserId(userEntity.getUserId());
        activeUser.setTelegramId(userEntity.getTelegramId());
        activeUser.setChatId(chatId);
        activeUser.setMessageId(messageId);
        activeUser.setText(text.trim());
        activeUser.setPremium(userEntity.isPremium());
        activeUser.setNick(userEntity.getNick());
        activeUser.setNotifyByEmail(userEntity.isEmailNotifications());
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
