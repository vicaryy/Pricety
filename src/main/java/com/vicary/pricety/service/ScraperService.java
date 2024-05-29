package com.vicary.pricety.service;

import com.vicary.pricety.entity.*;
import com.vicary.pricety.exception.IllegalInputException;
import com.vicary.pricety.exception.ScraperBotException;
import com.vicary.pricety.model.Product;
import com.vicary.pricety.scraper.*;
import com.vicary.pricety.service.map.ProductMapper;
import com.vicary.pricety.service.repository_services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScraperService {

    private final static long TIMEOUT = 15000;

    private final WaitingVariantsService waitingVariantsService;
    private final UpdatedVariantsService updatedVariantsService;
    private final WaitingProductService waitingProductService;
    private final UpdatedProductService updatedProductService;
    private final WaitingProductPriceService waitingProductPriceService;
    private final UpdatedProductPriceService updatedProductPriceService;
    private final ProductMapper productMapper;


    public List<String> scrapVariants(String url) {
        WaitingVariantsEntity waitingVariantsEntity = WaitingVariantsEntity.requestEntity(url);
        waitingVariantsEntity = waitingVariantsService.save(waitingVariantsEntity);

        UpdatedVariantsEntity updatedVariantsEntity = null;
        long time = System.currentTimeMillis();
        boolean loop = true;
        while (loop) {
            sleep(200);
            updatedVariantsEntity = updatedVariantsService.findById(waitingVariantsEntity.getId());

            if (updatedVariantsEntity != null) {
                loop = false;
                deleteVariantsEntitiesFromDatabase(waitingVariantsEntity.getId());
            }

            if (isTimeout(time))
                throw new IllegalInputException("Something goes wrong, try again later.", "Timeout in scraping variants.");
        }

        if (updatedVariantsEntity.isError())
            throw new IllegalInputException(updatedVariantsEntity.getErrorMessage(), updatedVariantsEntity.getErrorMessage());

        return updatedVariantsEntity.getVariants();
    }

    public Product scrapProduct(String url, String variant) {
        WaitingProductEntity waitingProductEntity = WaitingProductEntity.requestEntity(url, variant);
        waitingProductEntity = waitingProductService.save(waitingProductEntity);

        UpdatedProductEntity updatedProductEntity = null;
        long time = System.currentTimeMillis();
        boolean loop = true;
        while (loop) {
            sleep(200);
            updatedProductEntity = updatedProductService.findById(waitingProductEntity.getProductId());

            if (updatedProductEntity != null) {
                loop = false;
                deleteProductEntitiesFromDatabase(waitingProductEntity.getProductId());
            }

            if (isTimeout(time))
                throw new IllegalInputException("Something goes wrong, try again later.", "Timeout in scraping product.");
        }

        if (updatedProductEntity.isError())
            throw new IllegalInputException(updatedProductEntity.getErrorMessage(), updatedProductEntity.getErrorMessage());

        return productMapper.mapToProduct(updatedProductEntity);
    }


    public void updateProductsPrice(List<Product> products) {
        List<WaitingProductPriceEntity> waits = productMapper.mapToWaiting(products);
        waitingProductPriceService.saveAll(waits);

        List<UpdatedProductPriceEntity> updated = new ArrayList<>();
        long time = System.currentTimeMillis();
        boolean loop = true;
        while (loop) {
            sleep(200);
            updated = updatedProductPriceService.getAll();

            if (!updated.isEmpty()) {
                loop = false;
                updatedProductPriceService.deleteAll();
            }

            if (System.currentTimeMillis() - time > 1000000)
                throw new IllegalInputException("Something goes wrong, try again later.", "Timeout in updating products.");
        }
        updateProductsNewPrice(products, updated);
    }

    private void updateProductsNewPrice(List<Product> products, List<UpdatedProductPriceEntity> finalUpdated) {
        products.forEach(e -> e.setNewPrice(finalUpdated.stream().filter(ee -> ee.getProductId() == e.getProductId()).map(UpdatedProductPriceEntity::getNewPrice).findAny().orElse(0.0)));
    }

    private void deleteVariantsEntitiesFromDatabase(long id) {
        updatedVariantsService.deleteById(id);
    }

    private void deleteProductEntitiesFromDatabase(long id) {
        updatedProductService.deleteById(id);
    }

    private boolean isTimeout(long time) {
        return System.currentTimeMillis() - time > TIMEOUT;
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {
        }
    }

}