package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.AwaitedMessageEntity;
import org.hibernate.annotations.OnDelete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.service.annotation.DeleteExchange;

import java.util.Optional;

public interface AwaitedMessageRepository extends JpaRepository<AwaitedMessageEntity, Long> {


    @Modifying
    @Transactional
    @Query(value = """
            delete from awaited_messages
            where user_id = :userId""", nativeQuery = true)
    void deleteAllByUserId(@Param("userId") String userId);

    Optional<AwaitedMessageEntity> findByUserId(String userId);

    boolean existsByUserId(String userId);
}
