package com.vicary.pricety.service.repository_services;

import com.vicary.pricety.entity.WaitingProductPriceEntity;
import com.vicary.pricety.repository.WaitingProductPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingProductPriceService {

    private final WaitingProductPriceRepository repository;

    public List<WaitingProductPriceEntity> getAllAndDelete() {
        List<WaitingProductPriceEntity> w = repository.findAll();
        if (!w.isEmpty())
            deleteAll();
        return w;
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void saveAll(List<WaitingProductPriceEntity> w) {
        repository.saveAll(w);
    }
}
