package com.vicary.zalandoscraper.service.entity;

import com.vicary.zalandoscraper.entity.NotificationEmailEntity;
import com.vicary.zalandoscraper.repository.NotificationEmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationEmailService {

    private final NotificationEmailRepository repository;

    public void saveEntity(NotificationEmailEntity entity) {
        repository.save(entity);
    }
}
