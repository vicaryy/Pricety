package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.ActiveRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ActiveRequestRepository extends JpaRepository<ActiveRequestEntity, Long> {
    @Query(countQuery = "COUNT(1) FROM ACTIVE_REQUESTS WHERE USER_ID = :userId")
    boolean existsByUserId(String userId);

    @Query(name = "DELETE FROM ACTIVE_REQUESTS WHERE USER_ID = :userId")
    void deleteByUserId(String userId);
}
