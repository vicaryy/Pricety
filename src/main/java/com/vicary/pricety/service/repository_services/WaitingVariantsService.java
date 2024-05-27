package com.vicary.pricety.service.repository_services;

import com.vicary.pricety.entity.WaitingVariantsEntity;
import com.vicary.pricety.repository.WaitingVariantsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingVariantsService {

    private final WaitingVariantsRepository repository;

    public WaitingVariantsEntity getFirst() {
        List<WaitingVariantsEntity> waitingVariantsEntities = repository.findAll(Sort.by("id"));
        if (waitingVariantsEntities.isEmpty())
            return WaitingVariantsEntity.emptyEntity();

        return waitingVariantsEntities.getFirst();
    }

    public WaitingVariantsEntity save(WaitingVariantsEntity entity) {
        return repository.save(entity);
    }

    public List<WaitingVariantsEntity> getAll() {
        return repository.findAll();
    }

    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
