package com.vicary.zalandoscraper.service.map;

import com.vicary.zalandoscraper.api_telegram.api_object.User;
import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.model.RegisterModel;
import com.vicary.zalandoscraper.model.UserDTO;
import com.vicary.zalandoscraper.security.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    public UserEntity map(User user) {
        String nationality = "en";
        if (user.getLanguageCode() != null)
            nationality = user.getLanguageCode().equals("pl") ? "pl" : "en";

        return UserEntity.builder()
                .telegramId(user.getId().toString())
                .nick(user.getUsername() == null ? "" : user.getUsername().toLowerCase())
                .nationality(nationality)
                .premium(false)
                .role(Role.USER)
                .emailNotifications(false)
                .role(Role.USER)
                .telegram(true)
                .verifiedEmail(false)
                .build();
    }

    public UserDTO mapToDTO(UserEntity userEntity) {
        return UserDTO.builder()
                .userId(userEntity.getUserId())
                .telegramId(userEntity.getTelegramId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .nick(userEntity.getNick())
                .nationality(userEntity.getNationality())
                .role(userEntity.getRoleEnum())
                .emailNotifications(userEntity.isEmailNotifications())
                .verifiedEmail(userEntity.isVerifiedEmail())
                .telegram(userEntity.isTelegram())
                .website(userEntity.isWebsite())
                .build();
    }

    public List<UserDTO> mapToDTO(List<UserEntity> userEntity) {
        return userEntity.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public UserEntity map(RegisterModel model, PasswordEncoder encoder) {
        return UserEntity.builder()
                .email(model.getEmail())
                .password(encoder.encode(model.getPassword()))
                .nick(model.getEmail().split("@")[0])
                .nationality("en")
                .role(Role.USER)
                .emailNotifications(true)
                .website(true)
                .build();
    }

    public UserEntity mapTelegramToWebsite(UserEntity telegramUser, UserEntity webUser) {
        telegramUser.getProducts().forEach(e -> e.setUser(webUser));
        telegramUser.setProducts(new ArrayList<>());
        webUser.setNick(telegramUser.getNick());
        webUser.setTelegramId(telegramUser.getTelegramId());
        webUser.setTelegram(true);
        webUser.setNationality(telegramUser.getNationality());
        webUser.setPremium(telegramUser.isPremium());
        webUser.setRole(telegramUser.getRoleEnum());
        webUser.setEmailNotifications(telegramUser.isEmailNotifications());
        return webUser;
    }

    public UserEntity mapWebsiteToTelegram(UserEntity webUser, UserEntity telegramUser) {
        webUser.getProducts().forEach(e -> e.setUser(telegramUser));
        webUser.setProducts(new ArrayList<>());
        telegramUser.setPassword(webUser.getPassword());
        telegramUser.setEmail(webUser.getEmail());
        telegramUser.setNick(webUser.getNick());
        telegramUser.setWebsite(true);
        telegramUser.setNationality(webUser.getNationality());
        telegramUser.setPremium(webUser.isPremium());
        telegramUser.setRole(webUser.getRoleEnum());
        telegramUser.setEmailNotifications(webUser.isEmailNotifications());
        telegramUser.setVerifiedEmail(true);
        return telegramUser;
    }
}













