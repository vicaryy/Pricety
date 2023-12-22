package com.vicary.zalandoscraper.updater;

import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.UpdateReceiverService;
import com.vicary.zalandoscraper.service.map.ProductMapper;
import com.vicary.zalandoscraper.service.repository_services.ProductHistoryService;
import com.vicary.zalandoscraper.service.repository_services.ProductService;
import com.vicary.zalandoscraper.service.repository_services.WaitingUserService;
import com.vicary.zalandoscraper.updater.sender.NotificationManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AutoUpdaterTest {

    @Autowired
    private AutoUpdater autoUpdater;

    @MockBean
    private ProductService productService;
    @MockBean
    private ProductHistoryService productHistoryService;
    @MockBean
    private ProductMapper productMapper;
    @MockBean
    private NotificationManager notificationManager;
    @MockBean
    private WaitingUserService waitingUserService;
    @MockBean
    private UpdateReceiverService updateReceiverService;

    @Test
    void divideListIntoServices_expectEquals_MultiServicesList() {
        //given
        List<Product> givenList = getListOfProductsMultiServices();
        List<List<Product>> expectedList = getExpectedListOfProducts();
        //when
        List<List<Product>> actualList = autoUpdater.divideListIntoServices(givenList);
        //then
        assertEquals(expectedList, actualList);
    }


    private List<Product> getListOfProductsMultiServices() {

        return new ArrayList<>(Arrays.asList(
                getProduct("zalando1", "https://www.zalando.pl/something123", "zalando.pl"),
                getProduct("nike1", "https://www.nike.pl/something123", "nike.pl"),
                getProduct("hebe1", "https://www.hebe.pl/something123", "hebe.pl"),
                getProduct("nike3", "https://www.nike.pl/something123", "nike.pl"),
                getProduct("nike2", "https://www.nike.pl/something123", "nike.pl"),
                getProduct("allegro1", "https://www.allegro.pl/something123", "allegro.pl"),
                getProduct("olx1", "https://www.olx.pl/something123", "olx.pl"),
                getProduct("zalando2", "https://www.zalando.pl/something123", "zalando.pl"),
                getProduct("zalando3", "https://www.zalando.pl/something123", "zalando.pl"),
                getProduct("zalando4", "https://www.zalando.pl/something123", "zalando.pl"),
                getProduct("zalando5", "https://www.zalando.pl/something123", "zalando.pl"),
                getProduct("zalando7", "https://www.zalando.pl/something123", "zalando.pl"),
                getProduct("nike4", "https://www.nike.pl/something123", "nike.pl"),
                getProduct("nike5", "https://www.nike.pl/something123", "nike.pl"),
                getProduct("nike6", "https://www.nike.pl/something123", "nike.pl"),
                getProduct("nike7", "https://www.nike.pl/something123", "nike.pl"),
                getProduct("nike8", "https://www.nike.pl/something123", "nike.pl"),
                getProduct("nike9", "https://www.nike.pl/something123", "nike.pl"),
                getProduct("hebe2", "https://www.hebe.pl/something123", "hebe.pl"),
                getProduct("hebe3", "https://www.hebe.pl/something123", "hebe.pl"),
                getProduct("hebe4", "https://www.hebe.pl/something123", "hebe.pl"),
                getProduct("hebe5", "https://www.hebe.pl/something123", "hebe.pl"),
                getProduct("allegro2", "https://www.allegro.pl/something123", "allegro.pl"),
                getProduct("allegro3", "https://www.allegro.pl/something123", "allegro.pl"),
                getProduct("allegro4", "https://www.allegro.pl/something123", "allegro.pl"),
                getProduct("zalando6", "https://www.zalando.pl/something123", "zalando.pl"),
                getProduct("allegro5", "https://www.allegro.pl/something123", "allegro.pl"),
                getProduct("allegro7", "https://www.allegro.pl/something123", "allegro.pl"),
                getProduct("allegro8", "https://www.allegro.pl/something123", "allegro.pl"),
                getProduct("olx2", "https://www.olx.pl/something123", "olx.pl"),
                getProduct("olx3", "https://www.olx.pl/something123", "olx.pl"),
                getProduct("olx4", "https://www.olx.pl/something123", "olx.pl"),
                getProduct("olx5", "https://www.olx.pl/something123", "olx.pl"),
                getProduct("olx6", "https://www.olx.pl/something123", "olx.pl"),
                getProduct("allegro6", "https://www.allegro.pl/something123", "allegro.pl")
        ));
    }

    private List<List<Product>> getExpectedListOfProducts() {
        List<Product> zalandoList = Arrays.asList(
                getProduct("zalando1", "https://www.zalando.pl/something123", "zalando.pl"),
                getProduct("zalando2", "https://www.zalando.pl/something123", "zalando.pl"),
                getProduct("zalando3", "https://www.zalando.pl/something123", "zalando.pl"),
                getProduct("zalando4", "https://www.zalando.pl/something123", "zalando.pl"),
                getProduct("zalando5", "https://www.zalando.pl/something123", "zalando.pl"),
                getProduct("zalando7", "https://www.zalando.pl/something123", "zalando.pl"),
                getProduct("zalando6", "https://www.zalando.pl/something123", "zalando.pl")
        );

        List<Product> nikeList = Arrays.asList(
                getProduct("nike1", "https://www.nike.pl/something123", "nike.pl"),
                getProduct("nike3", "https://www.nike.pl/something123", "nike.pl"),
                getProduct("nike2", "https://www.nike.pl/something123", "nike.pl"),
                getProduct("nike4", "https://www.nike.pl/something123", "nike.pl"),
                getProduct("nike5", "https://www.nike.pl/something123", "nike.pl"),
                getProduct("nike6", "https://www.nike.pl/something123", "nike.pl"),
                getProduct("nike7", "https://www.nike.pl/something123", "nike.pl"),
                getProduct("nike8", "https://www.nike.pl/something123", "nike.pl"),
                getProduct("nike9", "https://www.nike.pl/something123", "nike.pl")
        );

        List<Product> hebeList = Arrays.asList(
                getProduct("hebe1", "https://www.hebe.pl/something123", "hebe.pl"),
                getProduct("hebe2", "https://www.hebe.pl/something123", "hebe.pl"),
                getProduct("hebe3", "https://www.hebe.pl/something123", "hebe.pl"),
                getProduct("hebe4", "https://www.hebe.pl/something123", "hebe.pl"),
                getProduct("hebe5", "https://www.hebe.pl/something123", "hebe.pl")
        );

        List<Product> allegroList = Arrays.asList(
                getProduct("allegro1", "https://www.allegro.pl/something123", "allegro.pl"),
                getProduct("allegro2", "https://www.allegro.pl/something123", "allegro.pl"),
                getProduct("allegro3", "https://www.allegro.pl/something123", "allegro.pl"),
                getProduct("allegro4", "https://www.allegro.pl/something123", "allegro.pl"),
                getProduct("allegro5", "https://www.allegro.pl/something123", "allegro.pl"),
                getProduct("allegro7", "https://www.allegro.pl/something123", "allegro.pl"),
                getProduct("allegro8", "https://www.allegro.pl/something123", "allegro.pl"),
                getProduct("allegro6", "https://www.allegro.pl/something123", "allegro.pl")
        );

        List<Product> olxList = Arrays.asList(
                getProduct("olx1", "https://www.olx.pl/something123", "olx.pl"),
                getProduct("olx2", "https://www.olx.pl/something123", "olx.pl"),
                getProduct("olx3", "https://www.olx.pl/something123", "olx.pl"),
                getProduct("olx4", "https://www.olx.pl/something123", "olx.pl"),
                getProduct("olx5", "https://www.olx.pl/something123", "olx.pl"),
                getProduct("olx6", "https://www.olx.pl/something123", "olx.pl")
        );

        return Arrays.asList(zalandoList, nikeList, hebeList, allegroList, olxList);
    }

    private Product getProduct(String name, String link, String serviceName) {
        return Product.builder()
                .name(name)
                .link(link)
                .serviceName(serviceName)
                .build();
    }
}