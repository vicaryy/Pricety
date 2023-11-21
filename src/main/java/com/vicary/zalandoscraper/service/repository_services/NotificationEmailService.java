package com.vicary.zalandoscraper.service.repository_services;

import com.vicary.zalandoscraper.entity.NotificationEmailEntity;
import com.vicary.zalandoscraper.model.Email;
import com.vicary.zalandoscraper.repository.NotificationEmailRepository;
import com.vicary.zalandoscraper.service.map.EmailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationEmailService {

    private final NotificationEmailRepository repository;

    private final EmailMapper mapper;

    public void saveEntity(NotificationEmailEntity entity) {
        repository.save(entity);
    }

    public void saveEntity(Email email) {
        saveEntity(mapper.map(email));
    }
}
