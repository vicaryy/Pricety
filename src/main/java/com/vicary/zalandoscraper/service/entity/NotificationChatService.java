package com.vicary.zalandoscraper.service.entity;

import com.vicary.zalandoscraper.entity.NotificationChatEntity;
import com.vicary.zalandoscraper.repository.NotificationChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationChatService {

    private final NotificationChatRepository repository;

    public void saveEntity(NotificationChatEntity entity) {
        repository.save(entity);
    }
}
