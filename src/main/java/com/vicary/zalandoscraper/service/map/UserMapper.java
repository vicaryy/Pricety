package com.vicary.zalandoscraper.service.map;

import com.vicary.zalandoscraper.api_object.User;
import com.vicary.zalandoscraper.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity map(User user) {
        return UserEntity.builder()
                .userId(user.getId().toString())
                .nick(user.getUsername())
                .nationality(user.getLanguageCode())
                .premium(false)
                .admin(false)
                .build();
    }
}
