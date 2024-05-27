package com.vicary.pricety.service;

import com.vicary.pricety.entity.UpdatedVariantsEntity;
import com.vicary.pricety.entity.WaitingVariantsEntity;
import com.vicary.pricety.exception.ScraperBotException;
import com.vicary.pricety.model.Product;
import com.vicary.pricety.scraper.*;
import com.vicary.pricety.service.repository_services.UpdatedVariantsService;
import com.vicary.pricety.service.repository_services.WaitingVariantsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScraperService {

    private final WaitingVariantsService waitingVariantsService;
    private final UpdatedVariantsService updatedVariantsService;


    public UpdatedVariantsEntity scrapVariants(String url) {
        WaitingVariantsEntity waitingVariantsEntity = WaitingVariantsEntity.requestEntity(url);
        waitingVariantsEntity = waitingVariantsService.save(waitingVariantsEntity);

        UpdatedVariantsEntity updatedVariantsEntity = null;
        long time = System.currentTimeMillis();
        boolean loop = true;
        while (loop) {
            updatedVariantsEntity = updatedVariantsService.findById(waitingVariantsEntity.getId());

            if (updatedVariantsEntity != null) {
                loop = false;
                deleteEntitiesFromDatabase(waitingVariantsEntity.getId());
            }

            if (isTimeout(time)) {
                updatedVariantsEntity = UpdatedVariantsEntity.errorEntity("Something goes wrong, try again later.");
                loop = false;
            }
        }
        return updatedVariantsEntity;
    }

    private void deleteEntitiesFromDatabase(long id) {
        waitingVariantsService.deleteById(id);
        updatedVariantsService.deleteById(id);
    }

    private boolean isTimeout(long time) {
        return System.currentTimeMillis() - time > 10000;
    }

    public Product scrapProduct(String url, String variant) {
        Scraper scraper = ScraperFactory.getScraperFromLink(url).orElseThrow(() -> new ScraperBotException("Invalid link."));
        return scraper.getProduct(url, variant);
    }
}