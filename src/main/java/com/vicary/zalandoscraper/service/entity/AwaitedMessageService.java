package com.vicary.zalandoscraper.service.entity;

import com.vicary.zalandoscraper.entity.AwaitedMessageEntity;
import com.vicary.zalandoscraper.repository.AwaitedMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AwaitedMessageService {

    private final AwaitedMessageRepository repository;


    public void saveAwaitedMessage(AwaitedMessageEntity awaitedMessageEntity) {
        repository.save(awaitedMessageEntity);
    }

    public boolean existsByUserId(String userId) {
        repository.existsByUserId(userId);
    }


    public AwaitedMessageEntity findAwaitedMessageByUserId(String userId) {
        return repository.findByUserId(userId).get();
    }

    public void deleteAwaitedMessageById(Long id) {
        repository.deleteById(id);
    }
}
