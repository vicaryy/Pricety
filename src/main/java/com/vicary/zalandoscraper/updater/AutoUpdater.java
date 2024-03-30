package com.vicary.zalandoscraper.updater;

import com.vicary.zalandoscraper.exception.ScraperBotException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.scraper.*;
import com.vicary.zalandoscraper.scraper.config.BrowserType;
import com.vicary.zalandoscraper.service.repository_services.WaitingUserService;
import com.vicary.zalandoscraper.updater.sender.NotificationManager;
import com.vicary.zalandoscraper.utils.TerminalExecutor;
import com.vicary.zalandoscraper.exception.TimeoutException;
import com.vicary.zalandoscraper.service.repository_services.ProductService;
import com.vicary.zalandoscraper.service.repository_services.ProductHistoryService;
import com.vicary.zalandoscraper.service.map.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AutoUpdater {
    private final static Logger logger = LoggerFactory.getLogger(AutoUpdater.class);
    //    private final static int DELAY_BETWEEN_UPDATES = 2000; // 2 sec
    private final static int DELAY_BETWEEN_UPDATES = 1000 * 60 * 60 * 2; // 2 hour
    private final ProductService productService;
    private final ProductHistoryService productHistoryService;
    private final ProductMapper productMapper;
    private final NotificationManager notificationManager;
    private final WaitingUserService waitingUserService;
    private final Map<String, Scraper> scraperMap = new HashMap<>();
    private Thread runningThread;
    private UpdaterState state;

    @Autowired
    public AutoUpdater(ProductService productService,
                       ProductHistoryService productHistoryService,
                       ProductMapper productMapper,
                       NotificationManager notificationManager,
                       WaitingUserService waitingUserService,
                       ZalandoScraper zalandoScraper,
                       HebeScraper hebeScraper,
                       NikeScraper nikeScraper,
                       HouseScraper houseScraper,
                       ZaraScraper zaraScraper) {
        this.productService = productService;
        this.productHistoryService = productHistoryService;
        this.productMapper = productMapper;
        this.waitingUserService = waitingUserService;
        this.notificationManager = notificationManager;

        scraperMap.put("zalando", zalandoScraper);
        scraperMap.put("hebe", hebeScraper);
        scraperMap.put("nike", nikeScraper);
        scraperMap.put("house", houseScraper);
        scraperMap.put("zara", zaraScraper);
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

    public boolean isUpdating() {
        return state.isUpdating();
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
            }
        } catch (InterruptedException ex) {
            logger.info("[Auto Updater] Auto Updater stopped.");
        } catch (Exception ex) {
            String message = "Auto Updater stopped due to error: " + ex.getMessage();
            throw new ScraperBotException(message, message);
        }
    }

    void runOnce() {
        logger.info("[Auto Updater] Auto Updater Once started successfully.");
        try {
            update();
        } catch (Exception ex) {
            String message = "Auto Updater Once stopped due to error: " + ex.getMessage();
            throw new ScraperBotException(message, message);
        }
    }


    private void update() {
        List<Product> products = productService.getAllProductsSortById();

        if (products.isEmpty()) {
            logger.info("[Auto Updater] Tried to update but there is no products!");
            return;
        }

        List<List<Product>> splittedListIntoScrapers = divideListIntoServices(products);

        updateProducts(splittedListIntoScrapers);

        saveToUpdatesHistoryRepository(products);

        sendNotificationsToUsers(products);

        updateProductsAfterUpdateInRepository(products);
    }

    List<List<Product>> divideListIntoServices(List<Product> products) {
        Map<String, List<Product>> serviceMap = new LinkedHashMap<>();

        for (Product p : products) {
            String service = p.getServiceName();

            if (!serviceMap.containsKey(service))
                serviceMap.put(service, new ArrayList<>());

            serviceMap.get(service).add(p);
        }

        return new ArrayList<>(serviceMap.values());
    }

    private void updateProducts(List<List<Product>> listOfLists) {
        for (List<Product> products : listOfLists) {
            String serviceNameWithoutCountry = getServiceNameWithoutCountry(products.get(0).getServiceName());
            Scraper scraper = getScraperFromMap(serviceNameWithoutCountry);

            long startingTime = System.currentTimeMillis();
            logger.info("[Product Updater] Starting updating '{}' products, service {}", products.size(), products.get(0).getServiceName());
            ProductUpdater updater = new ProductUpdater(scraper, products);
            try {
                updater.update();
            } catch (TimeoutException ex) {
                logger.error("[Auto Updater] Timeout in updating products but continuing auto update process.");
                logger.error("[Auto Updater] Set Scraper as bugged - headless mode OFF.");
                scraper.setBugged(true);
                TerminalExecutor.shutdownBrowser(BrowserType.Chromium);
            }
            logger.info("[Product Updater] Products updated successfully, it takes {} seconds\n", (System.currentTimeMillis() - startingTime) / 1000);
        }
    }

    private Scraper getScraperFromMap(String serviceName) {
        for (Map.Entry<String, Scraper> entry : scraperMap.entrySet())
            if (serviceName.startsWith(entry.getKey()))
                return entry.getValue();
        throw new ScraperBotException("Can't find Scraper for service '%s'".formatted(serviceName));
    }

    private String getServiceNameWithoutCountry(String serviceName) {
        return serviceName.split("\\.")[0];
    }

    private void saveToUpdatesHistoryRepository(List<Product> updatedProducts) {
        productHistoryService.saveUpdates(productMapper.mapToHistoryEntityList(updatedProducts));
    }

    private void sendNotificationsToUsers(List<Product> products) {
        notificationManager.sendPriceNotifications(products);
        notificationManager.sendWaitingUserNotifications(waitingUserService.getAllAndDeleteWaitingUsers());
    }

    private void updateProductsAfterUpdateInRepository(List<Product> updatedProducts) {
        productService.updateProductAfterUpdate(updatedProducts);
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