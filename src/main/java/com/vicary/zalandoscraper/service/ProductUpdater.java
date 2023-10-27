package com.vicary.zalandoscraper.service;

import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.service.entity.NotificationService;
import com.vicary.zalandoscraper.service.entity.ProductService;
import com.vicary.zalandoscraper.service.entity.UpdatesHistoryService;
import com.vicary.zalandoscraper.service.map.ProductMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductUpdater implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(ProductUpdater.class);

    private final Scraper scraper;

    private final ProductService productService;

    private final UpdatesHistoryService updatesHistoryService;

    private final ProductMapper productMapper;

    private final NotificationService notificationService;

    private final Thread productUpdaterThread = new Thread(this);

    private final static int DELAY_BEFORE_START = 5000;   // 5 seconds

    private final static int DELAY_BETWEEN_UPDATES = 10000; // 5 minutes


    @PostConstruct
    private void starter() {
        productUpdaterThread.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(DELAY_BEFORE_START);

            while (!Thread.currentThread().isInterrupted()) {
                update();
                Thread.sleep(DELAY_BETWEEN_UPDATES);
            }

        } catch (Exception ex) {
            logger.error("[Product Updater] Error: " + ex.getMessage());
            logger.error("[Product Updater] Thread interrupted.");
            ex.printStackTrace();
        } finally {
            productUpdaterThread.interrupt();
        }
    }

    private void update() throws InterruptedException {
        List<ProductDTO> updatedDTOs = productService.getAllProductsDto();

        if (updatedDTOs.isEmpty()) {
            logger.info("Tried to update but there is no products!");
            return;
        }

        scraper.updateProducts(updatedDTOs);
        
        updateProductsPriceInRepository(updatedDTOs);

        saveToUpdatesHistoryRepository(updatedDTOs);

        saveToNotificationsRepository(updatedDTOs);

        sendNotificationsToUsers();
    }


    private void saveToNotificationsRepository(List<ProductDTO> updatedDTOs) {
        notificationService.saveNotifications(productMapper.mapToNotificationEntity(updatedDTOs));
    }


    private void saveToUpdatesHistoryRepository(List<ProductDTO> updatedDTOs) {
        updatesHistoryService.saveUpdates(productMapper.mapToHistoryEntityList(updatedDTOs));
    }

    private void sendNotificationsToUsers() {
        notificationService.sendNotificationsToUsers();
    }
    
    private void updateProductsPriceInRepository(List<ProductDTO> updatedDTOs) {
        productService.updateProductPrice(updatedDTOs);
    }
}
























