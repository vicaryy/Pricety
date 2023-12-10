package com.vicary.zalandoscraper.updater;

import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.scraper.Scraper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductUpdaterTest {

    private ProductUpdater productUpdater;
    private Scraper scraper;


    @BeforeEach
    void beforeEach() {
        scraper = mock(Scraper.class);
    }


    @Test
    void setUpdatesTimeout_expectEquals_ListSizeIsOne() {
        //given
        List<Product> givenList = getListOfDefaultProduct(1);
        long timeout = 11_000; // 11 sec per DTO

        //when
        productUpdater = new ProductUpdater(scraper, givenList);
        productUpdater.setUpdatesTimeout();
        long expectedTimeout = System.currentTimeMillis() + timeout;
        long actualTimeout = productUpdater.getUpdatesTimeout();

        //then
        assertEquals(expectedTimeout, actualTimeout);
    }

    @Test
    void setUpdatesTimeout_expectEquals_ListSizeIsTwo() {
        //given
        List<Product> givenList = getListOfDefaultProduct(2);
        long timeout = 20_000; // 10 sec per DTO

        //when
        productUpdater = new ProductUpdater(scraper, givenList);
        productUpdater.setUpdatesTimeout();
        long expectedTimeout = System.currentTimeMillis() + timeout;
        long actualTimeout = productUpdater.getUpdatesTimeout();

        //then
        assertEquals(expectedTimeout, actualTimeout);
    }

    @Test
    void setUpdatesTimeout_expectEquals_ListSizeIsThree() {
        //given
        List<Product> givenList = getListOfDefaultProduct(3);
        long timeout = 27_000; // 9 sec per DTO

        //when
        productUpdater = new ProductUpdater(scraper, givenList);
        productUpdater.setUpdatesTimeout();
        long expectedTimeout = System.currentTimeMillis() + timeout;
        long actualTimeout = productUpdater.getUpdatesTimeout();

        //then
        assertEquals(expectedTimeout, actualTimeout);
    }

    @Test
    void setUpdatesTimeout_expectEquals_ListSizeIsFive() {
        //given
        List<Product> givenList = getListOfDefaultProduct(5);
        long timeout = 35_000; // 7 sec per DTO

        //when
        productUpdater = new ProductUpdater(scraper, givenList);
        productUpdater.setUpdatesTimeout();
        long expectedTimeout = System.currentTimeMillis() + timeout;
        long actualTimeout = productUpdater.getUpdatesTimeout();

        //then
        assertEquals(expectedTimeout, actualTimeout);
    }

    @Test
    void setUpdatesTimeout_expectEquals_ListSizeIsAboveSix() {
        //given
        List<Product> givenList = getListOfDefaultProduct(8);
        long timeout = 28_000; // 3,5 sec per DTO

        //when
        productUpdater = new ProductUpdater(scraper, givenList);
        productUpdater.setUpdatesTimeout();
        long expectedTimeout = System.currentTimeMillis() + timeout;
        long actualTimeout = productUpdater.getUpdatesTimeout();

        //then
        assertEquals(expectedTimeout, actualTimeout);
    }

    @Test
    void setUpdatesTimeout_expectEquals_ListSizeIsOneHundred() {
        //given
        List<Product> givenList = getListOfDefaultProduct(100);
        long timeout = 350_000; // 3,5 sec per DTO

        //when
        productUpdater = new ProductUpdater(scraper, givenList);
        productUpdater.setUpdatesTimeout();
        long expectedTimeout = System.currentTimeMillis() + timeout;
        long actualTimeout = productUpdater.getUpdatesTimeout();

        //then
        assertEquals(expectedTimeout, actualTimeout);
    }


    private List<Product> getListOfDefaultProduct(int size) {
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < size; i++)
            list.add(getDefaultDTO());
        return list;
    }

    private Product getDefaultDTO() {
        return Product.builder().build();
    }
}





















