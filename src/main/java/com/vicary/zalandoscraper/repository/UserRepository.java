package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserId(long userId);
    Optional<UserEntity> findByTelegramId(String telegramId);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByEmailAndWebsite(String email, boolean website);
    Optional<UserEntity> findByEmailAndTelegram(String email, boolean telegram);
    Optional<UserEntity> findByNick(String nick);

    boolean existsByTelegramId(String telegramId);

    boolean existsByUserId(long userId);

    boolean existsByNick(String nick);

    boolean existsByEmailAndVerifiedEmailAndTelegram(String email, boolean verifiedEmail, boolean telegram);

    @Modifying
    @Transactional
    @Query(value = """
            update users_test
            set email_notifications = :emailNotifications
            where user_id = :userId""", nativeQuery = true)
    void updateEmailNotificationsByUserId(@Param("userId") long userId, @Param("emailNotifications") boolean emailNotifications);

    @Modifying
    @Transactional
    @Query(value = """
            update users_test
            set email_notifications = :emailNotifications
            where email = :email""", nativeQuery = true)
    void updateEmailNotificationsByEmail(@Param("email") String email, @Param("emailNotifications") boolean emailNotifications);


    @Modifying
    @Transactional
    @Query(value = """
            update users_test
            set email = :email, email_notifications = false, verified_email = false
            where user_id = :userId""", nativeQuery = true)
    void updateEmailByUserId(@Param("userId") long userId, @Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = """
            update users_test
            set email = null, email_notifications = false, verified_email = false
            where user_id = :userId""", nativeQuery = true)
    void deleteEmailByUserId(@Param("userId") long userId);

    @Transactional
    @Modifying
    @Query(value = """
            update users_test
            set verified_email = :verified
            where user_id = :userId""", nativeQuery = true)
    void setVerifiedEmail(@Param("userId") long userId, @Param("verified") boolean verified);

    @Transactional
    @Modifying
    @Query(value = """
            update users_test
            set nationality = :language
            where user_id = :userId""", nativeQuery = true)
    void updateLanguage(@Param("userId") long userId, @Param("language") String language);

}
