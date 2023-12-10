package com.vicary.zalandoscraper.updater;

import com.vicary.zalandoscraper.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AutoUpdaterTest {

    @Autowired
    private AutoUpdater autoUpdater;

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
        List<Product> products = new ArrayList<>(Arrays.asList(
                getProduct("zalando1", "https://www.zalando.pl/something123"),
                getProduct("nike1", "https://www.nike.pl/something123"),
                getProduct("hebe1", "https://www.hebe.pl/something123"),
                getProduct("nike3", "https://www.nike.pl/something123"),
                getProduct("nike2", "https://www.nike.pl/something123"),
                getProduct("allegro1", "https://www.allegro.pl/something123"),
                getProduct("olx1", "https://www.olx.pl/something123"),
                getProduct("zalando2", "https://www.zalando.pl/something123"),
                getProduct("zalando3", "https://www.zalando.pl/something123"),
                getProduct("zalando4", "https://www.zalando.pl/something123"),
                getProduct("zalando5", "https://www.zalando.pl/something123"),
                getProduct("zalando7", "https://www.zalando.pl/something123"),
                getProduct("nike4", "https://www.nike.pl/something123"),
                getProduct("nike5", "https://www.nike.pl/something123"),
                getProduct("nike6", "https://www.nike.pl/something123"),
                getProduct("nike7", "https://www.nike.pl/something123"),
                getProduct("nike8", "https://www.nike.pl/something123"),
                getProduct("nike9", "https://www.nike.pl/something123"),
                getProduct("hebe2", "https://www.hebe.pl/something123"),
                getProduct("hebe3", "https://www.hebe.pl/something123"),
                getProduct("hebe4", "https://www.hebe.pl/something123"),
                getProduct("hebe5", "https://www.hebe.pl/something123"),
                getProduct("allegro2", "https://www.allegro.pl/something123"),
                getProduct("allegro3", "https://www.allegro.pl/something123"),
                getProduct("allegro4", "https://www.allegro.pl/something123"),
                getProduct("zalando6", "https://www.zalando.pl/something123"),
                getProduct("allegro5", "https://www.allegro.pl/something123"),
                getProduct("allegro7", "https://www.allegro.pl/something123"),
                getProduct("allegro8", "https://www.allegro.pl/something123"),
                getProduct("olx2", "https://www.olx.pl/something123"),
                getProduct("olx3", "https://www.olx.pl/something123"),
                getProduct("olx4", "https://www.olx.pl/something123"),
                getProduct("olx5", "https://www.olx.pl/something123"),
                getProduct("olx6", "https://www.olx.pl/something123"),
                getProduct("allegro6", "https://www.allegro.pl/something123")
                ));

        return products;
    }

    private List<List<Product>> getExpectedListOfProducts() {
        List<Product> zalandoList = Arrays.asList(
                getProduct("zalando1", "https://www.zalando.pl/something123"),
                getProduct("zalando2", "https://www.zalando.pl/something123"),
                getProduct("zalando3", "https://www.zalando.pl/something123"),
                getProduct("zalando4", "https://www.zalando.pl/something123"),
                getProduct("zalando5", "https://www.zalando.pl/something123"),
                getProduct("zalando7", "https://www.zalando.pl/something123"),
                getProduct("zalando6", "https://www.zalando.pl/something123")
        );

        List<Product> nikeList = Arrays.asList(
                getProduct("nike1", "https://www.nike.pl/something123"),
                getProduct("nike3", "https://www.nike.pl/something123"),
                getProduct("nike2", "https://www.nike.pl/something123"),
                getProduct("nike4", "https://www.nike.pl/something123"),
                getProduct("nike5", "https://www.nike.pl/something123"),
                getProduct("nike6", "https://www.nike.pl/something123"),
                getProduct("nike7", "https://www.nike.pl/something123"),
                getProduct("nike8", "https://www.nike.pl/something123"),
                getProduct("nike9", "https://www.nike.pl/something123")
        );

        List<Product> hebeList = Arrays.asList(
                getProduct("hebe1", "https://www.hebe.pl/something123"),
                getProduct("hebe2", "https://www.hebe.pl/something123"),
                getProduct("hebe3", "https://www.hebe.pl/something123"),
                getProduct("hebe4", "https://www.hebe.pl/something123"),
                getProduct("hebe5", "https://www.hebe.pl/something123")
        );

        List<Product> allegroList = Arrays.asList(
                getProduct("allegro1", "https://www.allegro.pl/something123"),
                getProduct("allegro2", "https://www.allegro.pl/something123"),
                getProduct("allegro3", "https://www.allegro.pl/something123"),
                getProduct("allegro4", "https://www.allegro.pl/something123"),
                getProduct("allegro5", "https://www.allegro.pl/something123"),
                getProduct("allegro7", "https://www.allegro.pl/something123"),
                getProduct("allegro8", "https://www.allegro.pl/something123"),
                getProduct("allegro6", "https://www.allegro.pl/something123")
                );

        List<Product> olxList = Arrays.asList(
                getProduct("olx1", "https://www.olx.pl/something123"),
                getProduct("olx2", "https://www.olx.pl/something123"),
                getProduct("olx3", "https://www.olx.pl/something123"),
                getProduct("olx4", "https://www.olx.pl/something123"),
                getProduct("olx5", "https://www.olx.pl/something123"),
                getProduct("olx6", "https://www.olx.pl/something123")
        );

        return Arrays.asList(zalandoList, nikeList, hebeList, allegroList, olxList);
    }

    private Product getProduct(String name, String link) {
        return Product.builder()
                .name(name)
                .link(link)
                .build();
    }
}