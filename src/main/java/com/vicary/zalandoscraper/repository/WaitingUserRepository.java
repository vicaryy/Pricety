package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.WaitingUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingUserRepository extends JpaRepository<WaitingUserEntity, Long> {

    boolean existsByUserId(String userId);
}
