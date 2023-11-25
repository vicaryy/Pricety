package com.vicary.zalandoscraper.service.repository_services;

import com.vicary.zalandoscraper.entity.MessageEntity;
import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.repository.MessageRepository;
import com.vicary.zalandoscraper.service.map.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository repository;

    private final MessageMapper mapper;


    public void saveEntity(MessageEntity message) {
        repository.save(message);
    }

    public void saveEntity(UserEntity user, String text) {
        saveEntity(mapper.map(user, text));
    }
}
