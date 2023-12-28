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


    public void updateLanguage(String userId, String language) {
        repository.updateLanguage(userId, language);
    }

    public void saveUser(UserEntity userEntity) {
        repository.save(userEntity);
        logger.info("[User Service] Added new user to database, userId '{}'", userEntity.getUserId());
    }

    public UserEntity saveUser(User user) {
        UserEntity userEntity = mapper.map(user);
        saveUser(userEntity);
        return userEntity;
    }

    public boolean existsByUserId(String userId) {
        return repository.existsByUserId(userId);
    }

    public UserEntity findByUserId(String userId) {
        return repository.findByUserId(userId).orElseThrow(() -> new NoSuchElementException("User '%s' not found".formatted(userId)));
    }

    public Optional<UserEntity> findByUserIdOptional(String userId) {
        return repository.findByUserId(userId);
    }

    public List<UserEntity> findAllUsers() {
        return repository.findAll(Sort.by("userId"));
    }

    public UserEntity findByUserNick(String nick) {
        return repository.findByNick(nick).orElseThrow(() -> new NoSuchElementException("User '%s' not found".formatted(nick)));
    }

    public void updateNotifyByEmailById(String userId, boolean notifyByEmail) {
        repository.updateNotifyByEmailById(userId, notifyByEmail);
    }

    public void deleteEmailById(String userId) {
        repository.deleteEmailById(userId);
    }

    public void updateEmailById(String userId, String email) {
        repository.updateEmailById(userId, email);
    }

    public boolean isUserAdmin(String userId) {
        return findByUserId(userId).isAdmin();
    }

    @Transactional
    public boolean updateUserToPremiumByUserId(String userId) {
        UserEntity updatedUser;
        try {
            updatedUser = findByUserId(userId);
        } catch (Exception ex) {
            return false;
        }
        updatedUser.setPremium(true);
        logger.info("User '{}' updated to Premium.", userId);
        return true;
    }

    @Transactional
    public boolean updateUserToStandardByUserId(String userId) {
        UserEntity updatedUser;
        try {
            updatedUser = findByUserId(userId);
        } catch (Exception ex) {
            return false;
        }
        updatedUser.setPremium(false);
        logger.info("User '{}' updated to Standard.", userId);
        return true;
    }

    @Transactional
    public boolean updateUserToAdminByUserId(String userId) {
        UserEntity updatedUser;
        try {
            updatedUser = findByUserId(userId);
        } catch (Exception ex) {
            return false;
        }
        updatedUser.setAdmin(true);
        logger.info("User '{}' updated to Admin.", userId);
        return true;
    }


    @Transactional
    public boolean updateUserToNonAdminByUserId(String userId) {
        UserEntity updatedUser;
        try {
            updatedUser = findByUserId(userId);
        } catch (Exception ex) {
            return false;
        }
        updatedUser.setAdmin(false);
        logger.info("User '{}' updated to Non-Admin.", userId);
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
        updatedUser.setAdmin(true);
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
        updatedUser.setAdmin(false);
        logger.info("User '{}' updated to Non-Admin.", nick);
        return true;
    }

    public void setVerifiedEmail(String userId, boolean verified) {
        repository.setVerifiedEmail(userId, verified);
    }

    @Transactional
    public void updateUserNick(String userId, String nick) {
        nick = nick.toLowerCase();
        validateNick(nick);
        UserEntity user = findByUserIdOptional(userId).orElseThrow(() -> new IllegalInputException("User not found.", "User to nick change not found"));
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

    public void deleteUser(String userId) {
        repository.deleteById(userId);
    }
}
