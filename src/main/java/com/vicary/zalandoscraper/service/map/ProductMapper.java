package com.vicary.zalandoscraper.service.map;

import com.vicary.zalandoscraper.model.User;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import com.vicary.zalandoscraper.entity.NotificationEntity;
import com.vicary.zalandoscraper.entity.ProductEntity;
import com.vicary.zalandoscraper.entity.UpdateHistoryEntity;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.repository_services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
                .currency(product.getCurrency())
                .serviceName(product.getServiceName())
                .user(userService.findByUserId(ActiveUser.get().getChatId())
                        .orElseThrow(() -> new NoSuchElementException("Error in mapping user")))
                .build();
    }

    public Product map(ProductEntity product) {
        return Product.builder()
                .productId(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .link(product.getLink())
                .variant(product.getVariant())
                .price(product.getPrice())
                .newPrice(product.getPrice())
                .priceAlert(product.getPriceAlert())
                .serviceName(product.getServiceName())
                .currency(product.getCurrency())
                .user(User.builder()
                        .userId(product.getUser().getUserId())
                        .email(product.getUser().getEmail())
                        .nick(product.getUser().getNick())
                        .language(product.getUser().getNationality())
                        .premium(product.getUser().isPremium())
                        .admin(product.getUser().isAdmin())
                        .notifyByEmail(product.getUser().isNotifyByEmail())
                        .build())
                .build();
    }

    public List<Product> map(List<ProductEntity> products) {
        return products.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    private UpdateHistoryEntity map(Product product, LocalDateTime localDateTime) {
        return UpdateHistoryEntity.builder()
                .productId(product.getProductId())
                .price(product.getNewPrice())
                .lastUpdate(localDateTime)
                .build();
    }

    public List<UpdateHistoryEntity> mapToHistoryEntityList(List<Product> product) {
        LocalDateTime localDateTime = LocalDateTime.now();

        return product.stream()
                .map(productDTO -> map(productDTO, localDateTime))
                .collect(Collectors.toList());
    }

    private NotificationEntity mapToNotificationEntity(Product product) {
        return NotificationEntity.builder()
                .userId(product.getUser().getUserId())
                .email(product.getUser().getEmail())
                .productName(product.getName())
                .description(product.getDescription())
                .variant(product.getVariant())
                .newPrice(product.getNewPrice())
                .oldPrice(product.getPrice())
                .link(product.getLink())
                .priceAlert(product.getPriceAlert())
                .notifyByEmail(product.getUser().isNotifyByEmail())
                .build();
    }

    public List<NotificationEntity> mapToNotificationEntity(List<Product> products) {
        return products.stream()
                .map(this::mapToNotificationEntity)
                .collect(Collectors.toList());
    }
}














