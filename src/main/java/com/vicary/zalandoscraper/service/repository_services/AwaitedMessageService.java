package com.vicary.zalandoscraper.service.repository_services;

import com.vicary.zalandoscraper.entity.AwaitedMessageEntity;
import com.vicary.zalandoscraper.repository.AwaitedMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AwaitedMessageService {

    private final AwaitedMessageRepository repository;


    public void saveAwaitedMessage(AwaitedMessageEntity awaitedMessageEntity) {
        repository.save(awaitedMessageEntity);
    }

    public boolean existsByUserId(String userId) {
        return repository.existsByUserId(userId);
    }

    @Transactional
    public String getRequestAndDeleteMessage(String userId) {
        AwaitedMessageEntity entity = findAwaitedMessageByUserId(userId);
        String request = entity.getRequest();
        deleteAllByUserId(entity.getUserId());
        return request;
    }

    public void deleteAllByUserId(String userId) {
        repository.deleteAllByUserId(userId);
    }


    public AwaitedMessageEntity findAwaitedMessageByUserId(String userId) {
        return repository.findByUserId(userId).get();
    }

    public void deleteAwaitedMessageById(Long id) {
        repository.deleteById(id);
    }
}
