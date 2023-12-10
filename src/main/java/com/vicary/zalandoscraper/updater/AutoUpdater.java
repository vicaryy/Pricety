package com.vicary.zalandoscraper.updater;

import com.vicary.zalandoscraper.exception.ZalandoScraperBotException;
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
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class AutoUpdater {
    private final static Logger logger = LoggerFactory.getLogger(AutoUpdater.class);
//    private final static int DELAY_BETWEEN_UPDATES = 2000; // 2 sec
    private final static int DELAY_BETWEEN_UPDATES = 1000 * 60 * 60 * 2; // 2 hour
    private final ProductService productService;
    private final UpdatesHistoryService updatesHistoryService;
    private final ProductMapper productMapper;
    private final NotificationManager notificationManager;
    private final WaitingUserService waitingUserService;
    private final Map<String, Scraper> scraperMap = new HashMap<>();
    private Thread runningThread;
    private UpdaterState state;

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

        scraperMap.put("zalando", zalandoScraper);
        scraperMap.put("hebe", hebeScraper);
        scraperMap.put("nike", nikeScraper);
        state = new StopState(this);
    }

    public void start() {
        state.start();
    }

    public void startOnce() {
        state.startOnce();
    }

    public void stop() {
        state.stop();
    }

    public boolean isRunning() {
        return state.isRunning();
    }

    public String getCurrentState() {
        return state.getState();
    }

    void run() {
        logger.info("[Auto Updater] Auto Updater started successfully.");
        try {
            while (!Thread.currentThread().isInterrupted()) {
                state = new UpdatingState(this);
                update();
                state = new RunningState(this);
                sleep(DELAY_BETWEEN_UPDATES);
                System.out.println("Jestem aktywny");
            }
        } catch (InterruptedException ex) {
            logger.info("[Auto Updater] Auto Updater stopped.");
        } catch (Exception ex) {
            String message = "Auto Updater stopped due to error: " + ex.getMessage();
            throw new ZalandoScraperBotException(message, message);
        }
    }

    void runOnce() {
        logger.info("[Auto Updater] Auto Updater Once started successfully.");
        try {
            update();
        } catch (Exception ex) {
            String message = "Auto Updater Once stopped due to error: " + ex.getMessage();
            throw new ZalandoScraperBotException(message, message);
        }
    }


    private void update() {
        List<ProductDTO> products = productService.getAllProductsDtoSortById();

        if (products.isEmpty()) {
            logger.info("[Auto Updater] Tried to update but there is no products!");
            return;
        }

        List<List<ProductDTO>> splittedListIntoScrapers = divideListIntoServices(products);

        updateProducts(splittedListIntoScrapers);

        updateProductsPriceInRepository(products);

        saveToUpdatesHistoryRepository(products);

        sendNotificationsToUsers(products);
    }

    List<List<ProductDTO>> divideListIntoServices(List<ProductDTO> DTOs) {
        Map<String, List<ProductDTO>> serviceMap = new LinkedHashMap<>();

        for (ProductDTO dto : DTOs) {
            String service = dto.getServiceName().split("\\.")[0];

            if (!serviceMap.containsKey(service))
                serviceMap.put(service, new ArrayList<>());

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

    private void sleep(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }

    void startRunningThread(Runnable task) {
        runningThread = new Thread(task);
        runningThread.start();
    }

    Thread getRunningThread() {
        return runningThread;
    }

    void setState(UpdaterState state) {
        this.state = state;
    }
}