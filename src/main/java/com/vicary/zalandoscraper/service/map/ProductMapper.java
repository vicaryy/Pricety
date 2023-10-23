package com.vicary.zalandoscraper.service.map;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.entity.ProductEntity;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.UserService;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
                .link(product.getLink())
                .lastUpdate(product.getLastUpdate())
                .user(userService.findByUserId(ActiveUser.get().getChatId())
                        .orElseThrow(() -> new NoSuchElementException("Error in mapping user")))
                .build();
    }

    public ProductDTO map(ProductEntity product) {
        return ProductDTO.builder()
                .productId(product.getId())
                .link(product.getLink())
                .variant(product.getVariant())
                .price(product.getPrice())
                .priceAlert(product.getPriceAlert())
                .build();
    }

    public List<ProductDTO> map(List<ProductEntity> products) {
        return products.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}














