package com.vicary.zalandoscraper.service.repository_services;

import com.vicary.zalandoscraper.entity.ProductHistoryEntity;
import com.vicary.zalandoscraper.repository.ProductHistoryRepository;
import com.vicary.zalandoscraper.service.dto.ProductHistoryDTO;
import com.vicary.zalandoscraper.service.map.ProductHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductHistoryService {

    private final static Logger logger = LoggerFactory.getLogger(ProductHistoryService.class);

    private final ProductHistoryRepository repository;

    private final ProductHistoryMapper mapper;


    public void saveUpdate(ProductHistoryEntity productHistoryEntity) {
        repository.save(productHistoryEntity);
    }

    public void saveUpdates(List<ProductHistoryEntity> productHistoryEntityList) {
        for (ProductHistoryEntity u : productHistoryEntityList)
            saveUpdate(u);

        logger.info("[Product Updater] Added {} product history to database.", productHistoryEntityList.size());
    }

    public LocalDateTime getLastUpdateTime() {
        return repository.getLatestUpdate().getLastUpdate();
    }

    public List<ProductHistoryDTO> getProductHistorySortedByIdReverse(long productId) {
        List<ProductHistoryEntity> entities = repository.findAllByProductId(productId, Sort.by(Sort.Direction.DESC, "id"));
        return mapper.map((entities));
    }

    public List<ProductHistoryDTO> getProductHistorySortedById(long productId) {
        List<ProductHistoryEntity> entities = repository.findAllByProductId(productId, Sort.by(Sort.Direction.ASC, "id"));
        return mapper.map((entities));
    }


    public double getLastPositivePrice(long productId) {
        List<ProductHistoryDTO> productHistory = getProductHistorySortedByIdReverse(productId);
        for (ProductHistoryDTO d : productHistory)
            if (d.getPrice() != 0)
                return d.getPrice();
        return 0;
    }

    public List<ProductHistoryDTO> getReducedProductHistory(long productId) {
        List<ProductHistoryEntity> entities = repository.findAllByProductId(productId, Sort.by(Sort.Direction.ASC, "id"));
        return mapper.map(getReducedListToOneDay(entities));
    }

    List<ProductHistoryEntity> getReducedListToOneDay(List<ProductHistoryEntity> entities) {
        List<ProductHistoryEntity> reduced = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            ProductHistoryEntity p = entities.get(i);
            int day = p.getLastUpdate().getDayOfYear();
            double price = p.getPrice();
            ProductHistoryEntity lowestEntity = p;

            for (int k = i + 1; k < entities.size(); k++) {
                ProductHistoryEntity pp = entities.get(k);
                int dayK = pp.getLastUpdate().getDayOfYear();
                double priceK = pp.getPrice();
                if (day == dayK) {
                    if (priceK < price) {
                        price = priceK;
                        lowestEntity = pp;
                    }
                } else {
                    i = k - 1;
                    break;
                }
            }
            reduced.add(lowestEntity);
        }
        return reduced;
    }
}











