package com.vicary.pricety.repository;

import com.vicary.pricety.entity.UpdatedProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpdatedProductRepository extends JpaRepository<UpdatedProductEntity, Long> {
}
