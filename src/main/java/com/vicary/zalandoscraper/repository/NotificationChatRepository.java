package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.NotificationChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationChatRepository extends JpaRepository<NotificationChatEntity, Long> {
}
