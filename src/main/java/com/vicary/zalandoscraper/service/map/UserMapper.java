package com.vicary.zalandoscraper.service.map;

import com.vicary.zalandoscraper.api_telegram.api_object.User;
import com.vicary.zalandoscraper.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity map(User user) {
        String nationality = "en";
        if (user.getLanguageCode() != null)
            nationality = user.getLanguageCode().equals("pl") ? "pl" : "en";

        return UserEntity.builder()
                .userId(user.getId().toString())
                .nick(user.getUsername() == null ? "" : user.getUsername())
                .nationality(nationality)
                .premium(false)
                .admin(false)
                .notifyByEmail(false)
                .verifiedEmail(false)
                .build();
    }
}
