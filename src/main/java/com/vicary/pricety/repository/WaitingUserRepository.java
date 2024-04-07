package com.vicary.pricety.repository;

import com.vicary.pricety.entity.UserEntity;
import com.vicary.pricety.entity.WaitingUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingUserRepository extends JpaRepository<WaitingUserEntity, Long> {

    boolean existsByUser(UserEntity user);
}
