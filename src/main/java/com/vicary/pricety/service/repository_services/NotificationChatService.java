package com.vicary.pricety.service.repository_services;

import com.vicary.pricety.entity.NotificationChatEntity;
import com.vicary.pricety.model.ChatNotification;
import com.vicary.pricety.repository.NotificationChatRepository;
import com.vicary.pricety.service.map.ChatNotificationMapper;
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
