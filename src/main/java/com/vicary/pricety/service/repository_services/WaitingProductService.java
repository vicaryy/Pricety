package com.vicary.pricety.service.repository_services;

import com.vicary.pricety.entity.WaitingProductEntity;
import com.vicary.pricety.repository.WaitingProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingProductService {

    private final WaitingProductRepository repository;

    public WaitingProductEntity getFirstAndDelete() {
        List<WaitingProductEntity> waitingProductEntities = repository.findAll(Sort.by("productId"));
        if (waitingProductEntities.isEmpty())
            return WaitingProductEntity.emptyEntity();

        deleteById(waitingProductEntities.getFirst().getProductId());
        return waitingProductEntities.getFirst();
    }

    public WaitingProductEntity save(WaitingProductEntity entity) {
        return repository.save(entity);
    }

    public List<WaitingProductEntity> getAll() {
        return repository.findAll();
    }

    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
