package com.vicary.zalandoscraper.service;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.api_object.Update;
import com.vicary.zalandoscraper.api_object.User;
import com.vicary.zalandoscraper.entity.ActiveRequestEntity;
import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.exception.ActiveUserException;
import com.vicary.zalandoscraper.exception.ZalandoScraperBotException;
import com.vicary.zalandoscraper.service.mapper.UserMapper;
import com.vicary.zalandoscraper.service.quick_sender.QuickSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UpdateVerification {
    private final static Logger logger = LoggerFactory.getLogger(UpdateVerification.class);

    private final UserService userService;

    private final ActiveRequestService activeRequestService;

    private final QuickSender quickSender;

    private final UserMapper userMapper;

    public void verify(Update update) {
        String userId = update.getCallbackQuery() == null
                ? update.getMessage().getFrom().getId().toString()
                : update.getCallbackQuery().getFrom().getId().toString();

        String chatId = update.getCallbackQuery() == null
                ? update.getMessage().getChat().getId().toString()
                : update.getCallbackQuery().getMessage().getChat().getId().toString();

        String text = update.getCallbackQuery() == null
                ? update.getMessage().getText()
                : update.getCallbackQuery().getData();

        checkActiveUser(userId, chatId);

        UserEntity userEntity = checkUserInRepository(update.getMessage().getFrom(), userId);

        setActiveUserInThread(userEntity, text, chatId);
    }

    private void checkActiveUser(String userId, String chatId) {
        if (activeRequestService.existsByUserId(userId)) {
            logger.info("User %s is trying to do more than one request".formatted(userId));
            quickSender.message(chatId, "One request at a time please.", false);
            throw new ActiveUserException();
        }
        activeRequestService.saveActiveUser(new ActiveRequestEntity(userId));
    }

    private UserEntity checkUserInRepository(User user, String userId) {
        if (userService.existsByUserId(userId))
            return userService.findByUserId(userId).orElseThrow(ZalandoScraperBotException::new);

        UserEntity userEntity = userMapper.map(user);
        userService.saveUser(userEntity);
        return userEntity;
    }

    private void setActiveUserInThread(UserEntity userEntity, String text, String chatId) {
        ActiveUser activeUser = ActiveUser.get();
        activeUser.setUserId(userEntity.getUserId());
        activeUser.setChatId(chatId);
        activeUser.setText(text);
        activeUser.setPremium(userEntity.isPremium());
        activeUser.setAdmin(userEntity.isAdmin());
    }
}
