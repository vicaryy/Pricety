package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUserId(String userId);
    Optional<UserEntity> findByNick(String nick);
    boolean existsByUserId(String userId);
}
