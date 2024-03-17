package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.WebUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebUserRepository extends JpaRepository<WebUserEntity, Long> {
}
