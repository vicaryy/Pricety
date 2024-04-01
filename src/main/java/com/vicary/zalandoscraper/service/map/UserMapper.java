package com.vicary.zalandoscraper.service.map;

import com.vicary.zalandoscraper.api_telegram.api_object.User;
import com.vicary.zalandoscraper.entity.ProductEntity;
import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.entity.WebUserEntity;
import com.vicary.zalandoscraper.model.RegisterModel;
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
}













