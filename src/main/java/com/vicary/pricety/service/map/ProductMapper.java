package com.vicary.pricety.service.map;

import com.vicary.pricety.entity.ProductEntity;
import com.vicary.pricety.entity.UserEntity;
import com.vicary.pricety.model.ProductTemplate;
import com.vicary.pricety.model.UserDTO;
import com.vicary.pricety.entity.ProductHistoryEntity;
import com.vicary.pricety.model.Product;
import com.vicary.pricety.service.dto.ProductDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDTO mapToDTO(ProductEntity p) {
        return ProductDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .photoUrl(p.getPhotoUrl())
                .price(p.getPrice())
                .variant(p.getVariant())
                .link(p.getLink())
                .priceAlert(p.getPriceAlert())
                .serviceName(p.getServiceName())
                .currency(p.getCurrency())
                .notifyWhenAvailable(p.isNotifyWhenAvailable())
                .userId(p.getUser().getUserId())
                .build();
    }

    public List<ProductDTO> mapToDTO(List<ProductEntity> p) {
        return p.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public ProductEntity mapFromDTO(ProductDTO product, UserEntity user) {
        return ProductEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .photoUrl(product.getPhotoUrl())
                .price(product.getPrice())
                .variant(product.getVariant())
                .priceAlert(product.getPriceAlert())
                .link(product.getLink())
                .currency(product.getCurrency())
                .serviceName(product.getServiceName())
                .notifyWhenAvailable(product.isNotifyWhenAvailable())
                .user(user)
                .build();
    }

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
                .userDTO(UserDTO.builder()
                        .userId(product.getUser().getUserId())
                        .telegramId(product.getUser().getTelegramId())
                        .email(product.getUser().getEmail())
                        .nick(product.getUser().getNick())
                        .nationality(product.getUser().getNationality())
                        .premium(product.getUser().isPremium())
                        .admin(product.getUser().isAdmin())
                        .emailNotifications(product.getUser().isEmailNotifications())
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
                .userDTO(UserDTO.builder()
                        .userId(product.getUserDTO().getUserId())
                        .email(product.getUserDTO().getEmail())
                        .nick(product.getUserDTO().getNick())
                        .nationality(product.getUserDTO().getNationality())
                        .premium(product.getUserDTO().isPremium())
                        .admin(product.getUserDTO().isAdmin())
                        .emailNotifications(product.getUserDTO().isEmailNotifications())
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














