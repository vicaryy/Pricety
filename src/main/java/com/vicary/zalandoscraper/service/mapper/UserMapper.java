package com.vicary.zalandoscraper.service.mapper;

import com.vicary.zalandoscraper.api_object.User;
import com.vicary.zalandoscraper.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity map(User user) {
        return UserEntity.builder()
                .nick(user.getUsername())
                .nationality(user.getLanguageCode())
                .premium(false)
                .admin(false)
                .userId(user.getId().toString())
                .build();
    }
}
