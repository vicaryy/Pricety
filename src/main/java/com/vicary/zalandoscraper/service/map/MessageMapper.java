package com.vicary.zalandoscraper.service.map;

import com.vicary.zalandoscraper.api_telegram.api_object.message.Message;
import com.vicary.zalandoscraper.entity.MessageEntity;
import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.service.dto.MessageDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessageMapper {

    public MessageEntity mapFromDTO(MessageDTO m, UserEntity user) {
        return MessageEntity.builder()
                .id(m.getId())
                .message(m.getMessage())
                .sentDate(m.getSentDate())
                .user(user)
                .build();
    }

    public MessageEntity map(UserEntity user, String text) {
        return MessageEntity.builder()
                .user(user)
                .message(text)
                .sentDate(LocalDateTime.now())
                .build();
    }
}
