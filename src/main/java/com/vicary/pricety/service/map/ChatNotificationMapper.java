package com.vicary.pricety.service.map;

import com.vicary.pricety.entity.NotificationChatEntity;
import com.vicary.pricety.model.ChatNotification;
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
