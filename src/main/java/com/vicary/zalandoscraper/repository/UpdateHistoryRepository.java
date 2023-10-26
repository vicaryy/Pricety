package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.UpdateHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UpdateHistoryRepository extends JpaRepository<UpdateHistoryEntity, Long> {

    @Query(value = """
            select * from updates_history
            order by id desc
            limit 1""", nativeQuery = true)
    UpdateHistoryEntity getLatestUpdate();
}
