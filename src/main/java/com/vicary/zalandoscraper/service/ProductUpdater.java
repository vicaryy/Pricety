package com.vicary.zalandoscraper.service;

import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.service.entity.NotificationService;
import com.vicary.zalandoscraper.service.entity.ProductService;
import com.vicary.zalandoscraper.service.entity.UpdatesHistoryService;
import com.vicary.zalandoscraper.service.map.ProductMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductUpdater implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(ProductUpdater.class);

    private final Scraper scraper;

    private final ProductService productService;

    private final UpdatesHistoryService updatesHistoryService;

    private final ProductMapper productMapper;

    private final NotificationService notificationService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    private Thread updateListThread1;

    private Thread updateListThread2;

    private Thread updateListThread3;

    private Thread updateListThread4;

    private final Thread productUpdaterThread = new Thread(this);

    private final static int DELAY_BEFORE_START = 5000;   // 5 seconds

    private final static int DELAY_BETWEEN_UPDATES = 10000; // 5 minutes


    @PostConstruct
    private void starter() {
//        productUpdaterThread.start();
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

    private void update() {
        logger.info("[Product Updater] Starting auto updating products...");
        List<ProductDTO> updatedDTOs = productService.getAllProductsDto();

        if (updatedDTOs.isEmpty()) {
            logger.info("Tried to update but there is no products!");
            return;
        }

        updateProducts(updatedDTOs);

        updateProductsPriceInRepository(updatedDTOs);

        saveToUpdatesHistoryRepository(updatedDTOs);

        saveToNotificationsRepository(updatedDTOs);

        sendNotificationsToUsers();
    }

    @SneakyThrows
    private void updateProducts(List<ProductDTO> updatedDTOs) {
        List<ProductDTO> firstHalf = updatedDTOs.subList(0, updatedDTOs.size() / 2);
        List<ProductDTO> secondHalf = updatedDTOs.subList(updatedDTOs.size() / 2, updatedDTOs.size());
//        List<ProductDTO> firstHalf = updatedDTOs.subList(0, updatedDTOs.size() / 4);
//        List<ProductDTO> secondHalf = updatedDTOs.subList(updatedDTOs.size() / 4, (updatedDTOs.size() / 4) * 2);
//        List<ProductDTO> thirdHalf = updatedDTOs.subList((updatedDTOs.size() / 4) * 2, (updatedDTOs.size() / 4) * 3);
//        List<ProductDTO> fourthHalf = updatedDTOs.subList((updatedDTOs.size() / 4) * 3, updatedDTOs.size());
//        System.out.println(updatedDTOs.stream()
//                        .map(e -> e.getProductId())
//                                .collect(Collectors.toList()));
//
//        System.out.println(firstHalf.stream()
//                .map(e -> e.getProductId())
//                .collect(Collectors.toList()));
//
//        System.out.println(secondHalf.stream()
//                .map(e -> e.getProductId())
//                .collect(Collectors.toList()));
//
//        System.out.println(thirdHalf.stream()
//                .map(e -> e.getProductId())
//                .collect(Collectors.toList()));
//
//        System.out.println(fourthHalf.stream()
//                .map(e -> e.getProductId())
//                .collect(Collectors.toList()));
//        System.out.println(firstHalf);
//        System.out.println(secondHalf);
//        System.out.println(thirdHalf);
//        System.out.println(fourthHalf);

//        Thread.sleep(1000000);
//        updateListThread1 = new Thread(() -> scraper.updateProducts(firstHalf));
//        updateListThread2 = new Thread(() -> scraper.updateProducts(secondHalf));
//        updateListThread3 = new Thread(() -> scraper.updateProducts(thirdHalf));
//        updateListThread4 = new Thread(() -> scraper.updateProducts(fourthHalf));
//        updateListThread1.start();
//        updateListThread2.start();
//        updateListThread3.start();
//        updateListThread4.start();
//
//        while (updateListThread1.isAlive() || updateListThread2.isAlive()
//                || updateListThread3.isAlive() || updateListThread4.isAlive()) {
//            try {
//                Thread.sleep(400);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }


        ExecutorService executorService1 = Executors.newFixedThreadPool(2);
        executorService1.execute(() -> scraper.updateProducts(firstHalf));
        executorService1.execute(() -> scraper.updateProducts(secondHalf));
//        executorService1.execute(() -> scraper.updateProducts(thirdHalf));
//        executorService1.execute(() -> scraper.updateProducts(fourthHalf));

        while (!executorService1.isTerminated()) {
            Thread.sleep(500);
        }

//        scraper.updateProducts(updatedDTOs);

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
























