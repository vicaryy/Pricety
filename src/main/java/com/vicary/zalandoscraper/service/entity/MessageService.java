package com.vicary.zalandoscraper.service.entity;

import com.vicary.zalandoscraper.entity.MessageEntity;
import com.vicary.zalandoscraper.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository repository;


    public void saveEntity(MessageEntity message) {
        repository.save(message);
    }
}
