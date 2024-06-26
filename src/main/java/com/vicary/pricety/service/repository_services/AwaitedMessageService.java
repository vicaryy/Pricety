package com.vicary.pricety.service.repository_services;

import com.vicary.pricety.entity.AwaitedMessageEntity;
import com.vicary.pricety.repository.AwaitedMessageRepository;
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

    public String getRequest(String userId) {
        AwaitedMessageEntity entity = findAwaitedMessageByUserId(userId);
        return entity.getRequest();
    }

    public void deleteAwaitedMessage(String userId) {
        deleteAllByUserId(userId);
    }

    public void deleteAllByUserId(String userId) {
        repository.deleteAllByUserId(userId);
    }


    public AwaitedMessageEntity findAwaitedMessageByUserId(String userId) {
        return repository.findByUserId(userId).get();
    }
}
