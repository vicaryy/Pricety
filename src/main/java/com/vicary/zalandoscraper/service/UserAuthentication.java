package com.vicary.zalandoscraper.service;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.api_object.Update;
import com.vicary.zalandoscraper.api_object.User;
import com.vicary.zalandoscraper.api_object.message.Message;
import com.vicary.zalandoscraper.entity.ActiveRequestEntity;
import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.exception.ActiveUserException;
import com.vicary.zalandoscraper.exception.ZalandoScraperBotException;
import com.vicary.zalandoscraper.service.map.UserMapper;
import com.vicary.zalandoscraper.service.quick_sender.QuickSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserAuthentication {
    private final static Logger logger = LoggerFactory.getLogger(UserAuthentication.class);

    private final UserService userService;

    private final ActiveRequestService activeRequestService;

    private final QuickSender quickSender;

    private final UserMapper userMapper;

    public void authenticate(Update update) {
        Message message = update.getCallbackQuery() == null
                ? update.getMessage()
                : update.getCallbackQuery().getMessage();

        String chatId = message.getChat().getId().toString();
        int messageId = message.getMessageId();
        String text = update.getCallbackQuery() == null
                ? update.getMessage().getText()
                : update.getCallbackQuery().getData();

        checkActiveUser(chatId);

        UserEntity userEntity = checkUserInRepository(message.getFrom(), chatId);

        setActiveUserInThread(userEntity, text, chatId, messageId);
    }

    private void checkActiveUser(String chatId) {
        if (activeRequestService.existsByUserId(chatId)) {
            logger.info("User %s is trying to do more than one request".formatted(chatId));
            quickSender.message(chatId, "One request at a time please.", false);
            throw new ActiveUserException();
        }
        activeRequestService.saveActiveUser(new ActiveRequestEntity(chatId));
    }

    private UserEntity checkUserInRepository(User user, String chatId) {
        if (userService.existsByUserId(chatId))
            return userService.findByUserId(chatId).orElseThrow(ZalandoScraperBotException::new);

        UserEntity userEntity = userMapper.map(user);
        userService.saveUser(userEntity);
        return userEntity;
    }

    private void setActiveUserInThread(UserEntity userEntity, String text, String chatId, int messageId) {
        ActiveUser activeUser = ActiveUser.get();
        activeUser.setUserId(userEntity.getUserId());
        activeUser.setChatId(chatId);
        activeUser.setMessageId(messageId);
        activeUser.setText(text);
        activeUser.setPremium(userEntity.isPremium());
        activeUser.setAdmin(userEntity.isAdmin());
    }
}
