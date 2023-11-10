package com.vicary.zalandoscraper.service.entity;

import com.vicary.zalandoscraper.entity.NotificationChatEntity;
import com.vicary.zalandoscraper.model.ChatNotification;
import com.vicary.zalandoscraper.repository.NotificationChatRepository;
import com.vicary.zalandoscraper.service.map.ChatNotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationChatService {

    private final NotificationChatRepository repository;

    private final ChatNotificationMapper mapper;

    public void saveEntity(NotificationChatEntity entity) {
        repository.save(entity);
    }

    public void saveEntity(ChatNotification notification) {
        saveEntity(mapper.map(notification));
    }
}
