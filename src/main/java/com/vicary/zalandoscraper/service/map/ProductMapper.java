package com.vicary.zalandoscraper.service.map;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.entity.NotificationEntity;
import com.vicary.zalandoscraper.entity.ProductEntity;
import com.vicary.zalandoscraper.entity.UpdateHistoryEntity;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.entity.UserService;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
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
                .user(userService.findByUserId(ActiveUser.get().getChatId())
                        .orElseThrow(() -> new NoSuchElementException("Error in mapping user")))
                .build();
    }

    public ProductDTO map(ProductEntity product) {
        return ProductDTO.builder()
                .productId(product.getId())
                .userId(product.getUser().getUserId())
                .name(product.getName())
                .description(product.getDescription())
                .link(product.getLink())
                .variant(product.getVariant())
                .price(product.getPrice())
                .newPrice(product.getPrice())
                .priceAlert(product.getPriceAlert())
                .email(product.getUser().getEmail())
                .notifyByEmail(product.getUser().isNotifyByEmail() && product.getUser().isVerifiedEmail())
                .build();
    }

    public List<ProductDTO> map(List<ProductEntity> products) {
        return products.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    private UpdateHistoryEntity map(ProductDTO productDTO, LocalDateTime localDateTime) {
        return UpdateHistoryEntity.builder()
                .productId(productDTO.getProductId())
                .price(productDTO.getNewPrice())
                .lastUpdate(localDateTime)
                .build();
    }

    public List<UpdateHistoryEntity> mapToHistoryEntityList(List<ProductDTO> productDTOs) {
        LocalDateTime localDateTime = LocalDateTime.now();

        return productDTOs.stream()
                .map(productDTO -> map(productDTO, localDateTime))
                .collect(Collectors.toList());
    }

    private NotificationEntity mapToNotificationEntity(ProductDTO productDTO){
        return NotificationEntity.builder()
                .userId(productDTO.getUserId())
                .email(productDTO.getEmail())
                .productName(productDTO.getName())
                .description(productDTO.getDescription())
                .variant(productDTO.getVariant())
                .newPrice(productDTO.getNewPrice())
                .oldPrice(productDTO.getPrice())
                .link(productDTO.getLink())
                .priceAlert(productDTO.getPriceAlert())
                .notifyByEmail(productDTO.isNotifyByEmail())
                .build();
    }

    public List<NotificationEntity> mapToNotificationEntity(List<ProductDTO> productDTOs) {
        return productDTOs.stream()
                .map(this::mapToNotificationEntity)
                .collect(Collectors.toList());
    }
}














