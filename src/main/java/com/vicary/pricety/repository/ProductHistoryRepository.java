package com.vicary.pricety.repository;

import com.vicary.pricety.entity.ProductHistoryEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductHistoryRepository extends JpaRepository<ProductHistoryEntity, Long> {

    @Query(value = """
            select * from updates_history
            order by id desc
            limit 1""", nativeQuery = true)
    ProductHistoryEntity getLatestUpdate();

    List<ProductHistoryEntity> findAllByProductId(long productId);

    List<ProductHistoryEntity> findAllByProductId(long productId, Sort sort);
}
