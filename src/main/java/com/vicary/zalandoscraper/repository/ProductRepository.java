package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.ProductEntity;
import com.vicary.zalandoscraper.entity.UserEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findAllByUser(UserEntity userEntity, Sort sort);

    @Transactional
    @Modifying
    @Query(value = """
            update public.products
            set price = :price
            where product_id = :productId""", nativeQuery = true)
    void updatePrice(@Param("productId") Long productId, @Param("price") double price);


    @Transactional
    @Modifying
    @Query(value = """
            update products
            set price = :price, price_alert = :priceAlert
            where product_id = :productId""", nativeQuery = true)
    void updatePriceAndPriceAlert(@Param("productId") Long productId, @Param("price") double price, @Param("priceAlert") String priceAlert);

    @Transactional
    @Modifying
    @Query(value = """
            update products
            set price_alert = :priceAlert
            where product_id = :productId""", nativeQuery = true)
    void updatePriceAlert(@Param("productId") Long productId, @Param("priceAlert") String priceAlert);


    @Transactional
    @Modifying
    @Query(value = """
            delete from products
            where user_id = :userId""", nativeQuery = true)
    void deleteAllByUserId(@Param("userId") long userId);


    @Query(value = """
            select count(1)
            from products
            where user_id = :userId and link = :link and variant = :variant""", nativeQuery = true)
    int existByTelegramIdLinkAndVariant(@Param("userId") long userId, @Param("link") String link, @Param("variant") String variant);

    @Query(value = """
            select count(product_id)
            from products
            where user_id = :userId""", nativeQuery = true)
    int countByTelegramId(long userId);
}
