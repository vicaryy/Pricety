package com.vicary.zalandoscraper.updater;

import com.vicary.zalandoscraper.BrowserType;
import com.vicary.zalandoscraper.TerminalExecutor;
import com.vicary.zalandoscraper.exception.TimeoutException;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
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
public class AutoUpdater implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(AutoUpdater.class);
    private final ProductUpdater productUpdater = ProductUpdater.getInstance();

    private final ProductService productService;

    private final UpdatesHistoryService updatesHistoryService;

    private final ProductMapper productMapper;

    private final NotificationManager notificationManager;
    private final Thread updaterThread = new Thread(this);
    private final static int DELAY_BEFORE_START = 5000;   // 5 seconds
    private final static int DELAY_BETWEEN_UPDATES = 1000 * 60 * 60; // 1 hour
    private final static int TEN_MINUTES = 1000 * 60;


    @PostConstruct
    private void starter() {
//        updaterThread.start();
    }

    @Override
    public void run() {
        try {
            sleep(DELAY_BEFORE_START);

            while (!Thread.currentThread().isInterrupted()) {
                update();
                sleep(DELAY_BETWEEN_UPDATES);
            }
        } catch (Exception ex) {
            logger.error("[Auto Updater] Error: " + ex.getMessage());
            logger.error("[Auto Updater] Interrupting thread...");
            updaterThread.interrupt();
            ex.printStackTrace();
        }
    }

    private void update() {
        List<ProductDTO> products = productService.getAllProductsDto();

        if (products.isEmpty()) {
            logger.info("[Auto Updater] Tried to update but there is no products!");
            return;
        }

        updateProducts(products);

        updateProductsPriceInRepository(products);

        saveToUpdatesHistoryRepository(products);

        sendNotificationsToUsers(products);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateProducts(List<ProductDTO> productDTOS) {
        try {
            productUpdater.update(productDTOS);
        } catch (TimeoutException ex) {
            logger.error("[Auto Updater] Timeout in updating products but continuing auto update process.");
            logger.error("[Auto Updater] Set Scraper as bugged - headless mode OFF.");
            TerminalExecutor.shutdownBrowser(BrowserType.Chromium);
        }
    }

    private void saveToUpdatesHistoryRepository(List<ProductDTO> updatedDTOs) {
        updatesHistoryService.saveUpdates(productMapper.mapToHistoryEntityList(updatedDTOs));
    }

    private void sendNotificationsToUsers(List<ProductDTO> DTOs) {
        notificationManager.send(DTOs);
    }

    private void updateProductsPriceInRepository(List<ProductDTO> updatedDTOs) {
        productService.updateProductPrices(updatedDTOs);
    }
}
























