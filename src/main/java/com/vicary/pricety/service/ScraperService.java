package com.vicary.pricety.service;

import com.vicary.pricety.entity.UpdatedProductEntity;
import com.vicary.pricety.entity.UpdatedVariantsEntity;
import com.vicary.pricety.entity.WaitingProductEntity;
import com.vicary.pricety.entity.WaitingVariantsEntity;
import com.vicary.pricety.exception.ScraperBotException;
import com.vicary.pricety.model.Product;
import com.vicary.pricety.scraper.*;
import com.vicary.pricety.service.map.ProductMapper;
import com.vicary.pricety.service.repository_services.UpdatedProductService;
import com.vicary.pricety.service.repository_services.UpdatedVariantsService;
import com.vicary.pricety.service.repository_services.WaitingProductService;
import com.vicary.pricety.service.repository_services.WaitingVariantsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScraperService {

    private final WaitingVariantsService waitingVariantsService;
    private final UpdatedVariantsService updatedVariantsService;
    private final WaitingProductService waitingProductService;
    private final UpdatedProductService updatedProductService;
    private final ProductMapper productMapper;


    public List<String> scrapVariants(String url) {
        WaitingVariantsEntity waitingVariantsEntity = WaitingVariantsEntity.requestEntity(url);
        waitingVariantsEntity = waitingVariantsService.save(waitingVariantsEntity);

        UpdatedVariantsEntity updatedVariantsEntity = null;
        long time = System.currentTimeMillis();
        boolean loop = true;
        while (loop) {
            updatedVariantsEntity = updatedVariantsService.findById(waitingVariantsEntity.getId());

            if (updatedVariantsEntity != null) {
                loop = false;
                deleteVariantsEntitiesFromDatabase(waitingVariantsEntity.getId());
            }

            if (isTimeout(time))
                throw new ScraperBotException("Something goes wrong, try again later.");
        }

        if (updatedVariantsEntity.isError())
            throw new ScraperBotException(updatedVariantsEntity.getErrorMessage());

        return updatedVariantsEntity.getVariants();
    }

    public Product scrapProduct(String url, String variant) {
        WaitingProductEntity waitingProductEntity = WaitingProductEntity.requestEntity(url, variant);
        waitingProductEntity = waitingProductService.save(waitingProductEntity);

        UpdatedProductEntity updatedProductEntity = null;
        long time = System.currentTimeMillis();
        boolean loop = true;
        while (loop) {
            updatedProductEntity = updatedProductService.findById(waitingProductEntity.getProductId());

            if (updatedProductEntity != null) {
                loop = false;
                deleteProductEntitiesFromDatabase(waitingProductEntity.getProductId());
            }

            if (isTimeout(time))
                throw new ScraperBotException("Something goes wrong, try again later.");
        }

        if (updatedProductEntity.isError())
            throw new ScraperBotException(updatedProductEntity.getErrorMessage());

        return productMapper.mapToProduct(updatedProductEntity);
    }

    private void deleteVariantsEntitiesFromDatabase(long id) {
        updatedVariantsService.deleteById(id);
    }

    private void deleteProductEntitiesFromDatabase(long id) {
        updatedProductService.deleteById(id);
    }

    private boolean isTimeout(long time) {
        return System.currentTimeMillis() - time > 10000;
    }
}