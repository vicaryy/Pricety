package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.AwaitedMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AwaitedMessageRepository extends JpaRepository<AwaitedMessageEntity, Long> {

    Optional<AwaitedMessageEntity> findByUserId(String userId);

    boolean existsByUserId(String userId);
}
