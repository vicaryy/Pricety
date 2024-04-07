package com.vicary.pricety.service.repository_services;

import com.vicary.pricety.entity.UserEntity;
import com.vicary.pricety.entity.WaitingUserEntity;
import com.vicary.pricety.repository.WaitingUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingUserService {

    private final WaitingUserRepository repository;

    public void saveWaitingUser(WaitingUserEntity entity) {
        repository.save(entity);
    }

    @Transactional
    public List<WaitingUserEntity> getAllAndDeleteWaitingUsers() {
        List<WaitingUserEntity> waitingUserEntities = repository.findAll();
        repository.deleteAll();
        return waitingUserEntities;
    }

    public boolean existsByUserId(UserEntity user) {
        return repository.existsByUser(user);
    }
}



