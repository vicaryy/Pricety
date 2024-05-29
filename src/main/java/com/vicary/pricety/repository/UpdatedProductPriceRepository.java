package com.vicary.pricety.repository;

import com.vicary.pricety.entity.UpdatedProductPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpdatedProductPriceRepository extends JpaRepository<UpdatedProductPriceEntity, Long> {
}
