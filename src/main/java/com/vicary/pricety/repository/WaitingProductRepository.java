package com.vicary.pricety.repository;

import com.vicary.pricety.entity.WaitingProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingProductRepository extends JpaRepository<WaitingProductEntity, Long> {
}
