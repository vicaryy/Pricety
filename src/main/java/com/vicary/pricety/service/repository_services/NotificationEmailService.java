package com.vicary.pricety.service.repository_services;

import com.vicary.pricety.entity.NotificationEmailEntity;
import com.vicary.pricety.model.Email;
import com.vicary.pricety.repository.NotificationEmailRepository;
import com.vicary.pricety.service.map.EmailMapper;
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
