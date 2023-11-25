package com.vicary.zalandoscraper.service.map;

import com.vicary.zalandoscraper.entity.MessageEntity;
import com.vicary.zalandoscraper.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessageMapper {

    public MessageEntity map(UserEntity user, String text) {
        return MessageEntity.builder()
                .user(user)
                .message(text)
                .sentDate(LocalDateTime.now())
                .build();
    }
}
