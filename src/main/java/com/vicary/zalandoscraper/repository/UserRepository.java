package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUserId(String userId);
    Optional<UserEntity> findByNick(String nick);
    boolean existsByUserId(String userId);

    @Modifying
    @Transactional
    @Query(value = """
            update users
            set notify_by_email = :notifyByEmail
            where user_id = :userId""",nativeQuery = true)
    void updateNotifyByEmailById(@Param("userId") String userId, @Param("notifyByEmail") boolean notifyByEmail);


    @Modifying
    @Transactional
    @Query(value = """
            update users
            set email = :email
            where user_id = :userId""", nativeQuery = true)
    void updateEmailById(@Param("userId") String userId, @Param("email") String email);
}
