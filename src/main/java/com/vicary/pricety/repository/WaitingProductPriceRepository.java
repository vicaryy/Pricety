package com.vicary.pricety.repository;

import com.vicary.pricety.entity.WaitingProductPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingProductPriceRepository extends JpaRepository<WaitingProductPriceEntity, Long> {
}
