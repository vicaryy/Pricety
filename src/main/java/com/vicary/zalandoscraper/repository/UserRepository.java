package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByTelegramId(String userId);

    Optional<UserEntity> findByNick(String nick);

    boolean existsByTelegramId(String userId);

    boolean existsByNick(String nick);

    @Modifying
    @Transactional
    @Query(value = """
            update users
            set email_notifications = :emailNotifications
            where telegram_id = :telegramId""", nativeQuery = true)
    void updateEmailNotificationsByTelegramId(@Param("telegramId") String telegramId, @Param("emailNotifications") boolean emailNotifications);


    @Modifying
    @Transactional
    @Query(value = """
            update users
            set email = :email, email_notifications = false, verified_email = false
            where telegram_id = :telegramId""", nativeQuery = true)
    void updateEmailByTelegramId(@Param("telegramId") String telegramId, @Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = """
            update users
            set email = null, email_notifications = false, verified_email = false
            where telegram_id = :telegramId""", nativeQuery = true)
    void deleteEmailByTelegramId(@Param("telegramId") String telegramId);

    @Transactional
    @Modifying
    @Query(value = """
            update users
            set verified_email = :verified
            where telegram_id = :telegramId""", nativeQuery = true)
    void setVerifiedEmail(@Param("telegramId") String telegramId, @Param("verified") boolean verified);

    @Transactional
    @Modifying
    @Query(value = """
            update users
            set nationality = :language
            where telegram_id = :telegramId""", nativeQuery = true)
    void updateLanguage(@Param("telegramId") String telegramId, @Param("language") String language);
}
