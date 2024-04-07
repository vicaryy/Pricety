package com.vicary.pricety.repository;

import com.vicary.pricety.entity.ActiveRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ActiveRequestRepository extends JpaRepository<ActiveRequestEntity, Long> {
    @Query(countQuery = "COUNT(1) FROM ACTIVE_REQUESTS WHERE USER_ID = :userId")
    boolean existsByUserId(String userId);

    @Modifying
    @Transactional
    @Query(value = """
            delete from active_requests
            where user_id = :userId""", nativeQuery = true)
    void deleteByUserId(String userId);
}
