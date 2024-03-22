package com.vicary.zalandoscraper.service.repository_services;

import com.vicary.zalandoscraper.api_telegram.api_object.User;
import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.repository.UserRepository;
import com.vicary.zalandoscraper.service.map.UserMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;

    private final UserMapper mapper;
    private final static Pattern NICK_PATTERN = Pattern.compile("^[a-z0-9]+$");


    public void updateLanguage(String telegramId, String language) {
        repository.updateLanguage(telegramId, language);
    }

    public void saveUser(UserEntity userEntity) {
        repository.save(userEntity);
        logger.info("[User Service] Added new user to database, telegramId '{}'", userEntity.getTelegramId());
    }

    public UserEntity saveUser(User user) {
        UserEntity userEntity = mapper.map(user);
        saveUser(userEntity);
        return userEntity;
    }

    public boolean existsByTelegramId(String telegramId) {
        return repository.existsByTelegramId(telegramId);
    }

    public UserEntity findByTelegramId(String telegramId) {
        return repository.findByTelegramId(telegramId).orElseThrow(() -> new NoSuchElementException("User '%s' not found".formatted(telegramId)));
    }

    public Optional<UserEntity> findByTelegramIdOptional(String telegramId) {
        return repository.findByTelegramId(telegramId);
    }

    public List<UserEntity> findAllUsers() {
        return repository.findAll(Sort.by("telegramId"));
    }

    public UserEntity findByUserNick(String nick) {
        return repository.findByNick(nick).orElseThrow(() -> new NoSuchElementException("User '%s' not found".formatted(nick)));
    }

    public void updateNotifyByEmailByTelegramId(String telegramId, boolean notifyByEmail) {
        repository.updateEmailNotificationsByTelegramId(telegramId, notifyByEmail);
    }

    public void deleteEmailByTelegramId(String telegramId) {
        repository.deleteEmailByTelegramId(telegramId);
    }

    public void updateEmailByTelegramId(String telegramId, String email) {
        repository.updateEmailByTelegramId(telegramId, email);
    }

    public boolean isUserAdmin(String telegramId) {
        return findByTelegramId(telegramId).isAdmin();
    }

    @Transactional
    public boolean updateUserToPremiumByTelegramId(String telegramId) {
        UserEntity updatedUser;
        try {
            updatedUser = findByTelegramId(telegramId);
        } catch (Exception ex) {
            return false;
        }
        updatedUser.setPremium(true);
        logger.info("User '{}' updated to Premium.", telegramId);
        return true;
    }

    @Transactional
    public boolean updateUserToStandardByTelegramId(String telegramId) {
        UserEntity updatedUser;
        try {
            updatedUser = findByTelegramId(telegramId);
        } catch (Exception ex) {
            return false;
        }
        updatedUser.setPremium(false);
        logger.info("User '{}' updated to Standard.", telegramId);
        return true;
    }

    @Transactional
    public boolean updateUserToAdminByTelegramId(String telegramId) {
        UserEntity updatedUser;
        try {
            updatedUser = findByTelegramId(telegramId);
        } catch (Exception ex) {
            return false;
        }
        updatedUser.setRoleAdmin();
        logger.info("User '{}' updated to Admin.", telegramId);
        return true;
    }


    @Transactional
    public boolean updateUserToNonAdminByTelegramId(String telegramId) {
        UserEntity updatedUser;
        try {
            updatedUser = findByTelegramId(telegramId);
        } catch (Exception ex) {
            return false;
        }
        updatedUser.setRoleUser();
        logger.info("User '{}' updated to Non-Admin.", telegramId);
        return true;
    }

    @Transactional
    public boolean updateUserToPremiumByNick(String nick) {
        UserEntity updatedUser;
        try {
            updatedUser = findByUserNick(nick);
        } catch (Exception ex) {
            return false;
        }
        updatedUser.setPremium(true);
        logger.info("User '{}' updated to Premium.", nick);
        return true;
    }

    public boolean updateUserToStandardByNick(String nick) {
        UserEntity updatedUser;
        try {
            updatedUser = findByUserNick(nick);
        } catch (Exception ex) {
            return false;
        }
        updatedUser.setPremium(false);
        logger.info("User '{}' updated to Standard.", nick);
        return true;
    }

    public boolean updateUserToAdminByNick(String nick) {
        UserEntity updatedUser;
        try {
            updatedUser = findByUserNick(nick);
        } catch (Exception ex) {
            return false;
        }
        updatedUser.setRoleAdmin();
        logger.info("User '{}' updated to Admin.", nick);
        return true;
    }

    public boolean updateUserToNonAdminByNick(String nick) {
        UserEntity updatedUser;
        try {
            updatedUser = findByUserNick(nick);
        } catch (Exception ex) {
            return false;
        }
        updatedUser.setRoleAdmin();
        logger.info("User '{}' updated to Non-Admin.", nick);
        return true;
    }

    public void setVerifiedEmail(String telegramId, boolean verified) {
        repository.setVerifiedEmail(telegramId, verified);
    }

    @Transactional
    public void updateUserNick(String telegramId, String nick) {
        nick = nick.toLowerCase();
        validateNick(nick);
        UserEntity user = findByTelegramIdOptional(telegramId).orElseThrow(() -> new IllegalInputException("User not found.", "User to nick change not found"));
        user.setNick(nick);
    }

    public void validateNick(String nick) {
        if (nick.length() > 25)
            throw new IllegalInputException(Messages.other("nickCharacterLimitAbove"), "User entered nick above 25 characters.");
        if (nick.length() < 3)
            throw new IllegalInputException(Messages.other("nickCharacterLimitUnder"), "User entered nick under 3 characters.");
        if (!NICK_PATTERN.matcher(nick).matches())
            throw new IllegalInputException(Messages.other("nickCharacterIllegal"), "User entered nick but with illegal characters.");
        if (isNickExists(nick))
            throw new IllegalInputException(Messages.other("nickAlreadyExists"), "User entered nick which already exists.");
    }

    private boolean isNickExists(String nick) {
        return repository.existsByNick(nick);
    }

    public void deleteUser(String telegramId) {
        repository.deleteById(telegramId);
    }
}
