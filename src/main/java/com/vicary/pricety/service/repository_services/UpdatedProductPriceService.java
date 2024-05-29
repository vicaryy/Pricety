package com.vicary.pricety.service.repository_services;

import com.vicary.pricety.entity.UpdatedProductPriceEntity;
import com.vicary.pricety.repository.UpdatedProductPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdatedProductPriceService {

    private final UpdatedProductPriceRepository repository;

    public List<UpdatedProductPriceEntity> getAll() {
        return repository.findAll();
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void saveAll(List<UpdatedProductPriceEntity> u) {
        repository.saveAll(u);
    }
}
