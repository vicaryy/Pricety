package com.vicary.zalandoscraper.service.map;

import com.vicary.zalandoscraper.entity.ProductEntity;
import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.model.ProductTemplate;
import com.vicary.zalandoscraper.model.User;
import com.vicary.zalandoscraper.entity.ProductHistoryEntity;
import com.vicary.zalandoscraper.model.Product;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductEntity map(Product product, UserEntity user) {
        return ProductEntity.builder()
                .name(product.getName())
                .description(product.getDescription())
                .photoUrl(product.getPhotoUrl())
                .price(product.getPrice())
                .variant(product.getVariant())
                .priceAlert("AUTO")
                .link(product.getLink())
                .currency(product.getCurrency())
                .serviceName(product.getServiceName())
                .notifyWhenAvailable(product.isNotifyWhenAvailable())
                .user(user)
                .build();
    }

    public Product map(ProductEntity product) {
        return Product.builder()
                .productId(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .photoUrl(product.getPhotoUrl())
                .link(product.getLink())
                .variant(product.getVariant())
                .price(product.getPrice())
                .newPrice(product.getPrice())
                .priceAlert(product.getPriceAlert())
                .serviceName(product.getServiceName())
                .currency(product.getCurrency())
                .notifyWhenAvailable(product.isNotifyWhenAvailable())
                .user(User.builder()
                        .userId(product.getUser().getUserId())
                        .telegramId(product.getUser().getTelegramId())
                        .email(product.getUser().getEmail())
                        .nick(product.getUser().getNick())
                        .language(product.getUser().getNationality())
                        .premium(product.getUser().isPremium())
                        .admin(product.getUser().isAdmin())
                        .notifyByEmail(product.getUser().isEmailNotifications())
                        .build())
                .build();
    }

    public ProductTemplate mapToTemplate(Product product) {
        String price = product.getPrice() == 0 ? "SOLD OUT" : String.format("%.2f", product.getPrice());
        String serviceName = product.getServiceName().split("\\.")[0];
        String priceAlert =
                (product.getPriceAlert().equalsIgnoreCase("auto") || product.getPriceAlert().equalsIgnoreCase("off"))
                        ? product.getPriceAlert().toUpperCase() : String.format("%.2f %s", Double.parseDouble(product.getPriceAlert()), product.getCurrency());
        String variant = product.getVariant().startsWith("-oneVariant") ? "One Variant" : product.getVariant();

        return ProductTemplate.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .photoUrl(product.getPhotoUrl())
                .link(product.getLink())
                .variant(variant)
                .price(price)
                .priceAlert(priceAlert)
                .serviceName(serviceName)
                .currency(product.getCurrency())
                .notifyWhenAvailable(product.isNotifyWhenAvailable())
                .user(User.builder()
                        .userId(product.getUser().getUserId())
                        .email(product.getUser().getEmail())
                        .nick(product.getUser().getNick())
                        .language(product.getUser().getLanguage())
                        .premium(product.getUser().isPremium())
                        .admin(product.getUser().isAdmin())
                        .notifyByEmail(product.getUser().isNotifyByEmail())
                        .build())
                .build();
    }

    public List<ProductTemplate> mapToTemplate(List<Product> products) {
        return products.stream()
                .map(this::mapToTemplate)
                .toList();
    }

    public List<Product> map(List<ProductEntity> products) {
        return products.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    private ProductHistoryEntity map(Product product, LocalDateTime localDateTime) {
        return ProductHistoryEntity.builder()
                .productId(product.getProductId())
                .price(product.getNewPrice())
                .lastUpdate(localDateTime)
                .build();
    }

    public List<ProductHistoryEntity> mapToHistoryEntityList(List<Product> product) {
        LocalDateTime localDateTime = LocalDateTime.now();

        return product.stream()
                .map(productDTO -> map(productDTO, localDateTime))
                .collect(Collectors.toList());
    }
}














