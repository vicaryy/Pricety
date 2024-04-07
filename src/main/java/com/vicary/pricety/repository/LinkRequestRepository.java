package com.vicary.pricety.repository;

import com.vicary.pricety.entity.LinkRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LinkRequestRepository extends JpaRepository<LinkRequestEntity, Long> {
    Optional<LinkRequestEntity> findByRequestId(String requestId);
    void deleteByRequestId(String requestId);
}
