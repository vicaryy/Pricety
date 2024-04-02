package com.vicary.zalandoscraper.repository;

import com.vicary.zalandoscraper.entity.DataImportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DataImportRepository extends JpaRepository<DataImportEntity, Long> {

    Optional<DataImportEntity> findByRequest(String request);

    Optional<DataImportEntity> findByEmailAndMethod(String email, String method);

    void deleteAllByEmail(String email);
}
