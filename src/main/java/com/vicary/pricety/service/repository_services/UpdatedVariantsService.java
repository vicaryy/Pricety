package com.vicary.pricety.service.repository_services;

import com.vicary.pricety.entity.UpdatedVariantsEntity;
import com.vicary.pricety.repository.UpdatedVariantsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdatedVariantsService {

    private final UpdatedVariantsRepository repository;

    public void save(UpdatedVariantsEntity entity) {
        repository.save(entity);
    }

    public List<UpdatedVariantsEntity> getAll() {
        return repository.findAll();
    }

    public void deleteById(long id) {
        repository.deleteById(id);
    }

    public UpdatedVariantsEntity findById(long id) {
        return repository.findById(id).orElse(null);
    }
}
