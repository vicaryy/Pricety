package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.LinkRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LinkRequestRepository extends JpaRepository<LinkRequestEntity, Long> {
    Optional<LinkRequestEntity> findByRequestId(String requestId);
}
