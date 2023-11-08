package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
}
