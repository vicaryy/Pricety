package com.vicary.zalandoscraper.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    void updateProduct_expectEquals_whatever() {
        System.out.println(repository.existByLinkAndVariant("https://www.zalando.pl/nike-performance-utility-elite-unisex-plecak-blackenigma-stone-n1242l05c-q11.html", "-oneVariant One Size"));
    }
}