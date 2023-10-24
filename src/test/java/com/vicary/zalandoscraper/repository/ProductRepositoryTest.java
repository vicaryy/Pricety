package com.vicary.zalandoscraper.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    void updateProduct_expectEquals_whatever() {
    }
}