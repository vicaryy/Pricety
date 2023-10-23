package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Modifying
    @Query(value = """
            update public.products
            set price = :price
            where product_id = :productId""", nativeQuery = true)
    void updatePrice(@Param("productId") Long productId, @Param("price") double price);

    @Modifying
    @Query(value = """
            update products
            set price = :price, price_alert = :priceAlert
            where product_id = :productId""", nativeQuery = true)
    void updatePrice(@Param("productId") Long productId, @Param("price") double price, @Param("priceAlert") String priceAlert);
}
