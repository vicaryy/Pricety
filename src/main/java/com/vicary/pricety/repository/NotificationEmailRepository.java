package com.vicary.pricety.repository;

import com.vicary.pricety.entity.NotificationEmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationEmailRepository extends JpaRepository<NotificationEmailEntity, Long> {
}
