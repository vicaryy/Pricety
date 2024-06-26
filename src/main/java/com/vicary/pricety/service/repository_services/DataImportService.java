package com.vicary.pricety.service.repository_services;

import com.vicary.pricety.entity.DataImportEntity;
import com.vicary.pricety.repository.DataImportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DataImportService {

    private final DataImportRepository repository;

    public DataImportEntity save(DataImportEntity dataImportEntity) {
        return repository.save(dataImportEntity);
    }

    public Optional<DataImportEntity> findByRequest(String request) {
        return repository.findByRequest(request);
    }

    public Optional<DataImportEntity> findByEmailAndMethod(String email, String method) {
        return repository.findByEmailAndMethod(email, method);
    }

    @Transactional
    public void deleteAllByEmail(String email) {
        repository.deleteAllByEmail(email);
    }

    public DataImportEntity generateAndSave(String email, String method) {
        return save(DataImportEntity.builder()
                .email(email)
                .request(generateRequest(method))
                .method(method)
                .build());
    }

    private String generateRequest(String method) {
        StringBuilder sb = new StringBuilder();
        IntStream intStream = ThreadLocalRandom.current().ints(6, 0, 10);
        intStream.forEach(sb::append);
        return method + "-" + sb;
    }
}
