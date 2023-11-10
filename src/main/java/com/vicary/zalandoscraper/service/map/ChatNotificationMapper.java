package com.vicary.zalandoscraper.service.map;

import com.vicary.zalandoscraper.entity.NotificationChatEntity;
import com.vicary.zalandoscraper.entity.NotificationEmailEntity;
import com.vicary.zalandoscraper.model.ChatNotification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ChatNotificationMapper {

    public NotificationChatEntity map(ChatNotification notification) {
        return NotificationChatEntity.builder()
                .toUserId(notification.getChatId())
                .message(notification.getMessage())
                .sentDate(LocalDateTime.now())
                .build();
    }
}
