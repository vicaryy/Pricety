package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.entity.WaitingUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingUserRepository extends JpaRepository<WaitingUserEntity, Long> {

    boolean existsByUser(UserEntity user);
}
