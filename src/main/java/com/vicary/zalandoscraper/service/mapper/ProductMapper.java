package com.vicary.zalandoscraper.service.mapper;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.entity.ProductEntity;
import com.vicary.zalandoscraper.exception.ZalandoScraperBotException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final UserService userService;

    public ProductEntity map(Product product) {
        return ProductEntity.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .variant(product.getVariant())
                .priceAlert("AUTO")
                .user(userService.findByUserId(ActiveUser.get().getChatId())
                        .orElseThrow(() -> new NoSuchElementException("Error in mapping user")))
                .build();
    }
}
