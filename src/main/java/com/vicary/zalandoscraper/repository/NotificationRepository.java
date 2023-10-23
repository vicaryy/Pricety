package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
}
