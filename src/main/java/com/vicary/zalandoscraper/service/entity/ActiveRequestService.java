package com.vicary.zalandoscraper.service.entity;

import com.vicary.zalandoscraper.entity.ActiveRequestEntity;
import com.vicary.zalandoscraper.repository.ActiveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActiveRequestService {

    private final ActiveRequestRepository repository;

    public ActiveRequestEntity saveActiveUser(ActiveRequestEntity activeRequestEntity) {
        return repository.save(activeRequestEntity);
    }

    public boolean existsByUserId(String userId) {
        return repository.existsByUserId(userId);
    }

    @Transactional
    public void deleteByUserId(String userId) {
        repository.deleteByUserId(userId);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void deleteAllActiveUsers() {
        repository.deleteAll();
    }

    public int countActiveUsers() {
        return (int) repository.count();
    }
}
