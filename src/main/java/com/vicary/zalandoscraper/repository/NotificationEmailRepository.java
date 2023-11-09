package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.NotificationEmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationEmailRepository extends JpaRepository<NotificationEmailEntity, Long> {
}
