package com.vicary.zalandoscraper.service.map;

import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private final ProductMapper mapper = new ProductMapper();

    @Test
    void mapToTemplate() {
        mapper.mapToTemplate(getProduct());
    }

    private Product getProduct() {
        return Product.builder()
                .productId(1L)
                .name("name")
                .description("description")
                .photoUrl("url")
                .link("link")
                .variant("variant")
                .price(20)
                .newPrice(12.10)
                .priceAlert("150.00")
                .serviceName("zalando.pl")
                .currency("zł")
                .user(User.builder()
                        .build())
                .build();
    }
}