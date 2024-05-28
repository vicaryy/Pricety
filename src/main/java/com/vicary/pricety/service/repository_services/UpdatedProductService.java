package com.vicary.pricety.service.repository_services;

import com.vicary.pricety.entity.UpdatedProductEntity;
import com.vicary.pricety.repository.UpdatedProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdatedProductService {

    private final UpdatedProductRepository repository;

    public void save(UpdatedProductEntity entity) {
        repository.save(entity);
    }

    public List<UpdatedProductEntity> getAll() {
        return repository.findAll();
    }

    public void deleteById(long id) {
        repository.deleteById(id);
    }

    public UpdatedProductEntity findById(long id) {
        return repository.findById(id).orElse(null);
    }
}
