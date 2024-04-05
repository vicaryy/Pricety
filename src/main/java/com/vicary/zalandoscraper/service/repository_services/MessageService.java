package com.vicary.zalandoscraper.service.repository_services;

import com.vicary.zalandoscraper.entity.MessageEntity;
import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.repository.MessageRepository;
import com.vicary.zalandoscraper.service.map.MessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository repository;

    private final MessageMapper mapper;


    public void saveEntity(MessageEntity message) {
        log.info("Save message to repo: {} {}", message.getMessage(), message.getUser().getUserId());
        repository.save(message);
    }

    public void saveEntity(UserEntity user, String text) {
        saveEntity(mapper.map(user, text));
    }
}
