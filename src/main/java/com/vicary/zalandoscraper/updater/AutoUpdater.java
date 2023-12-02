package com.vicary.zalandoscraper.updater;

import com.vicary.zalandoscraper.scraper.*;
import com.vicary.zalandoscraper.service.repository_services.WaitingUserService;
import com.vicary.zalandoscraper.updater.sender.NotificationManager;
import com.vicary.zalandoscraper.utils.TerminalExecutor;
import com.vicary.zalandoscraper.exception.TimeoutException;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.service.repository_services.ProductService;
import com.vicary.zalandoscraper.service.repository_services.UpdatesHistoryService;
import com.vicary.zalandoscraper.service.map.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AutoUpdater implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(AutoUpdater.class);
    private final static int DELAY_BEFORE_START = 5000;   // 5 seconds
    private final static int DELAY_BETWEEN_UPDATES = 1000 * 60 * 60 * 2; // 2 hour
    private static boolean isActive;
    private final ProductService productService;
    private final UpdatesHistoryService updatesHistoryService;
    private final ProductMapper productMapper;
    private final NotificationManager notificationManager;
    private final WaitingUserService waitingUserService;
    private final Thread updaterThread = new Thread(this);
    private final Map<String, Scraper> scraperMap = new HashMap<>();

    @Autowired
    public AutoUpdater(ProductService productService,
                       UpdatesHistoryService updatesHistoryService,
                       ProductMapper productMapper,
                       NotificationManager notificationManager,
                       WaitingUserService waitingUserService,
                       ZalandoScraper zalandoScraper,
                       HebeScraper hebeScraper,
                       NikeScraper nikeScraper) {
        this.productService = productService;
        this.updatesHistoryService = updatesHistoryService;
        this.productMapper = productMapper;
        this.waitingUserService = waitingUserService;
        this.notificationManager = notificationManager;

        scraperMap.put("zalando.pl", zalandoScraper);
        scraperMap.put("hebe.pl", hebeScraper);
        scraperMap.put("nike.pl", nikeScraper);

        updaterThread.start();
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
        List<ProductDTO> products = productService.getAllProductsDtoSortById();

        if (products.isEmpty()) {
            logger.info("[Auto Updater] Tried to update but there is no products!");
            return;
        }

        List<List<ProductDTO>> splittedListIntoScrapers = divideListIntoServices(products);
        isActive = true;
        updateProducts(splittedListIntoScrapers);
        isActive = false;

        updateProductsPriceInRepository(products);

        saveToUpdatesHistoryRepository(products);

        sendNotificationsToUsers(products);
    }

    List<List<ProductDTO>> divideListIntoServices(List<ProductDTO> DTOs) {
        Map<String, List<ProductDTO>> serviceMap = new LinkedHashMap<>();

        for (ProductDTO dto : DTOs) {
            String service = dto.getServiceName();

            if (!serviceMap.containsKey(service)) {
                serviceMap.put(service, new ArrayList<>());
            }

            serviceMap.get(service).add(dto);
        }

        return new ArrayList<>(serviceMap.values());
    }

    private void updateProducts(List<List<ProductDTO>> productDTOS) {
        for (List<ProductDTO> DTOs : productDTOS) {
            String serviceName = DTOs.get(0).getServiceName();
            Scraper scraper = scraperMap.get(serviceName);

            long startingTime = System.currentTimeMillis();
            logger.info("[Product Updater] Starting updating '{}' products, service {}", DTOs.size(), serviceName);
            ProductUpdater updater = new ProductUpdater(scraper, DTOs);
            try {
                updater.update();
            } catch (TimeoutException ex) {
                logger.error("[Auto Updater] Timeout in updating products but continuing auto update process.");
                logger.error("[Auto Updater] Set Scraper as bugged - headless mode OFF.");
                scraper.setBugged(true);
                TerminalExecutor.shutdownBrowser(BrowserType.Chromium);
            }
            logger.info("[Product Updater] Products updated successfully, it takes {} seconds", (System.currentTimeMillis() - startingTime) / 1000);
        }
    }

    private void saveToUpdatesHistoryRepository(List<ProductDTO> updatedDTOs) {
        updatesHistoryService.saveUpdates(productMapper.mapToHistoryEntityList(updatedDTOs));
    }

    private void sendNotificationsToUsers(List<ProductDTO> DTOs) {
        notificationManager.sendPriceNotifications(DTOs);
        notificationManager.sendWaitingUserNotifications(waitingUserService.getAllAndDeleteWaitingUsers());
    }

    private void updateProductsPriceInRepository(List<ProductDTO> updatedDTOs) {
        productService.updateProductPrices(updatedDTOs);
    }

    public static boolean isActive() {
        return isActive;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
























