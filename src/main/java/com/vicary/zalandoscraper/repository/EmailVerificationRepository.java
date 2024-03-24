package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.EmailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity, Long> {

    @Query(value = """
            select count(1) from email_verifications
            where user_id = :userId""", nativeQuery = true)
    int existsByUserId(@Param("userId") long userId);


    @Transactional
    @Modifying
    @Query(value = """
            delete from email_verifications
            where user_id = :userId""", nativeQuery = true)
    void deleteAllByUserId(@Param("userId") long userId);

    EmailVerificationEntity findByUserId(long userId);

    boolean existsByUserIdAndToken(long userId, String token);

    @Transactional
    @Modifying
    void deleteByToken(String token);
}
