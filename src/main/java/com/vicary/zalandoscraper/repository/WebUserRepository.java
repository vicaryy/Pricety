package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.WebUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebUserRepository extends JpaRepository<WebUserEntity, Long> {

    Optional<WebUserEntity> findByEmail(String email);
}
