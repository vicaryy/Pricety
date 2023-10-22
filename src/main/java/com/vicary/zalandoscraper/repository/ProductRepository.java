package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

}
