package com.vicary.zalandoscraper.service.repository_services;

import com.vicary.zalandoscraper.api_telegram.api_object.User;
import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.model.LogInModel;
import com.vicary.zalandoscraper.model.RegisterModel;
import com.vicary.zalandoscraper.repository.UserRepository;
import com.vicary.zalandoscraper.service.map.UserMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
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


    public void updateLanguage(long userId, String language) {
        repository.updateLanguage(userId, language);
    }

    public UserEntity saveUser(UserEntity userEntity) {
        if (userEntity.isTelegram())
            logger.info("[User Service] Added new user to database via telegram, telegramId '{}'", userEntity.getTelegramId());
        if (userEntity.isWebsite())
            logger.info("[User Service] Added new user to database via website, email '{}'", userEntity.getEmail());

        return repository.save(userEntity);
    }

    public UserEntity saveUser(User user) {
        UserEntity userEntity = mapper.map(user);
        saveUser(userEntity);
        return userEntity;
    }

    public boolean existsByTelegramId(String telegramId) {
        return repository.existsByTelegramId(telegramId);
    }

    public boolean existsByUserId(long userId) {
        return repository.existsByUserId(userId);
    }

    public UserEntity findByUserId(long userId) {
        return repository.findByUserId(userId).orElseThrow(() -> new NoSuchElementException("User '%s' not found".formatted(userId)));
    }

    public UserEntity findByTelegramId(String telegramId) {
        return repository.findByTelegramId(telegramId).orElseThrow(() -> new NoSuchElementException("User '%s' not found".formatted(telegramId)));
    }

    public UserEntity findWebUserByEmail(String email) {
        return repository.findByEmailAndWebsite(email, true).orElseThrow(() -> new NoSuchElementException("User '%s' not found".formatted(email)));
    }

    public UserEntity findTelegramUserByEmail(String email) {
        return repository.findByEmailAndTelegram(email, true).orElseThrow(() -> new NoSuchElementException("User '%s' not found".formatted(email)));
    }

    public Optional<UserEntity> findByEmailOptional(String email) {
        return repository.findByEmail(email);
    }

    public Optional<UserEntity> findWebUserByEmailOptional(String email) {
        return repository.findByEmailAndWebsite(email, true);
    }

    public Optional<UserEntity> findByUserIdOptional(long userId) {
        return repository.findByUserId(userId);
    }

    public List<UserEntity> findAllUsers() {
        return repository.findAll(Sort.by("telegramId"));
    }

    public UserEntity findByUserNick(String nick) {
        return repository.findByNick(nick).orElseThrow(() -> new NoSuchElementException("User '%s' not found".formatted(nick)));
    }

    public void updateNotifyByEmailByUserId(long userId, boolean notifyByEmail) {
        repository.updateEmailNotificationsByUserId(userId, notifyByEmail);
    }

    public void deleteEmailByUserId(long userId) {
        repository.deleteEmailByUserId(userId);
    }

    public void updateEmailByUserId(long userId, String email) {
        repository.updateEmailByUserId(userId, email);
    }

    public boolean isUserAdmin(long userId) {
        return findByUserId(userId).isAdmin();
    }

    @Transactional
    public boolean updateUserToPremiumByUserId(long userId) {
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
    public boolean updateUserToStandardByUserId(long userId) {
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
    public boolean updateUserToAdminByUserId(long userId) {
        UserEntity updatedUser;
        try {
            updatedUser = findByUserId(userId);
        } catch (Exception ex) {
            return false;
        }
        updatedUser.setRoleAdmin();
        logger.info("User '{}' updated to Admin.", userId);
        return true;
    }


    @Transactional
    public boolean updateUserToNonAdminByUserId(long userId) {
        UserEntity updatedUser;
        try {
            updatedUser = findByUserId(userId);
        } catch (Exception ex) {
            return false;
        }
        updatedUser.setRoleUser();
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

    public void setVerifiedEmail(long userId, boolean verified) {
        repository.setVerifiedEmail(userId, verified);
    }

    @Transactional
    public void updateUserNick(long userId, String nick) {
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

    public void deleteUser(long userId) {
        repository.deleteById(userId);
    }

    public UserEntity registerUser(RegisterModel model, PasswordEncoder encoder) {
        return saveUser(mapper.map(model, encoder));
    }

    public void checkRegisterModelValidation(RegisterModel model) throws IllegalArgumentException {
        if (!isEmailValid(model.getEmail()) || !isPasswordValid(model.getPassword()))
            throw new IllegalArgumentException("Invalid data.");

        if (isEmailTaken(model.getEmail()))
            throw new IllegalArgumentException("Email is already taken.");
    }

    public void checkLogInModelValidation(LogInModel model, PasswordEncoder encoder) throws IllegalArgumentException {
        UserEntity user = findWebUserByEmailOptional(model.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid data."));
        if (!user.isVerifiedEmail())
            throw new IllegalArgumentException("User not activated.");

        if (!encoder.matches(model.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("Invalid data.");
    }

    private boolean isEmailValid(String email) {
        return com.vicary.zalandoscraper.pattern.Pattern.isEmail(email);
    }

    private boolean isEmailTaken(String email) {
        return findWebUserByEmailOptional(email).isPresent();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 7 && isPasswordContainsSpecialChar(password);
    }

    private boolean isPasswordContainsSpecialChar(String password) {
        for (char c : password.toCharArray())
            if (!Character.isLetterOrDigit(c))
                return true;
        return false;
    }

    public boolean existsByVerifiedEmailAndTelegram(String email, boolean telegram) {
        return repository.existsByEmailAndVerifiedEmailAndTelegram(email, true, telegram);
    }

    public UserEntity updateWebUserByTelegram(String email) {
        UserEntity webUser = findWebUserByEmail(email);
        UserEntity telegramUser = findTelegramUserByEmail(email);
        webUser = mapper.mapTelegramToWebsite(telegramUser, webUser);
        repository.save(webUser);
        repository.save(telegramUser);
        repository.deleteById(telegramUser.getUserId());
        logger.info("Updated webUser by telegramUser, user email: '{}'", webUser.getEmail());
        return webUser;
    }

    public UserEntity updateWebUserByTelegram(String email, String telegramId) {
        UserEntity webUser = findWebUserByEmail(email);
        UserEntity telegramUser = findByTelegramId(telegramId);
        webUser = mapper.mapTelegramToWebsite(telegramUser, webUser);
        repository.save(webUser);
        repository.save(telegramUser);
        repository.deleteById(telegramUser.getUserId());
        logger.info("Updated webUser by telegramUser, user email: '{}'", webUser.getEmail());
        return webUser;
    }

    public UserEntity updateTelegramByWebUser(String email) {
        UserEntity webUser = findWebUserByEmail(email);
        UserEntity telegramUser = findTelegramUserByEmail(email);
        telegramUser = mapper.mapWebsiteToTelegram(webUser, telegramUser);
        repository.save(telegramUser);
        repository.save(webUser);
        repository.deleteById(webUser.getUserId());
        logger.info("Updated telegramUser by webUser, user email: '{}'", webUser.getEmail());
        return webUser;
    }

    public UserEntity updateTelegramByWebUser(String email, String telegramId) {
        UserEntity webUser = findWebUserByEmail(email);
        UserEntity telegramUser = findByTelegramId(email);
        telegramUser = mapper.mapWebsiteToTelegram(webUser, telegramUser);
        repository.save(telegramUser);
        repository.save(webUser);
        repository.deleteById(webUser.getUserId());
        logger.info("Updated telegramUser by webUser, user email: '{}'", webUser.getEmail());
        return webUser;
    }
}





















