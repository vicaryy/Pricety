package com.vicary.pricety.repository;

import com.vicary.pricety.entity.ProductEntity;
import com.vicary.pricety.entity.UserEntity;
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
            update public.products_test
            set price = :price
            where product_id = :productId""", nativeQuery = true)
    void updatePrice(@Param("productId") Long productId, @Param("price") double price);


    @Transactional
    @Modifying
    @Query(value = """
            update products_test
            set price = :price, price_alert = :priceAlert
            where product_id = :productId""", nativeQuery = true)
    void updatePriceAndPriceAlert(@Param("productId") Long productId, @Param("price") double price, @Param("priceAlert") String priceAlert);

    @Transactional
    @Modifying
    @Query(value = """
            update products_test
            set product_name = :title, description = :description, price_alert = :alert
            where product_id = :productId""", nativeQuery = true)
    void updateTitleDescriptionAlert(
            @Param("productId") Long productId,
            @Param("title") String title,
            @Param("description") String description,
            @Param("alert") String alert);

    @Transactional
    @Modifying
    @Query(value = """
            update products_test
            set price_alert = :priceAlert
            where product_id = :productId""", nativeQuery = true)
    void updatePriceAlert(@Param("productId") Long productId, @Param("priceAlert") String priceAlert);

    @Transactional
    @Modifying
    @Query(value = """
            update products_test
            set notify_when_available = :notify
            where product_id = :productId""", nativeQuery = true)
    void updateNotifyWhenAvailable(@Param("productId") long productId, @Param("notify") boolean notify);


    @Transactional
    @Modifying
    @Query(value = """
            delete from products_test
            where user_id = :userId""", nativeQuery = true)
    void deleteAllByUserId(@Param("userId") long userId);


    @Query(value = """
            select count(1)
            from products_test
            where user_id = :userId and link = :link and variant = :variant""", nativeQuery = true)
    int existByUserIdLinkAndVariant(@Param("userId") long userId, @Param("link") String link, @Param("variant") String variant);

    @Query(value = """
            select count(product_id)
            from products_test
            where user_id = :userId""", nativeQuery = true)
    int countByUserId(long userId);

    @Transactional
    @Modifying
    @Query(value = """
            delete from products_test
            where product_id = :productId""", nativeQuery = true)
    void deleteByProductId(@Param("productId") long productId);

}
