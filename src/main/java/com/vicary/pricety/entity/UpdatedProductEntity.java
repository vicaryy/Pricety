package com.vicary.pricety.entity;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "updated_products")
public class UpdatedProductEntity {
    @Id
    @Column(name = "id")
    private Long productId;

    @Column(name = "product_name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "price")
    private double price;

    @Column(name = "variant")
    private String variant;

    @Column(name = "link")
    private String link;

    @Column(name = "price_alert")
    private String priceAlert;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "currency")
    private String currency;

    @Column(name = "error")
    private boolean error;

    @Column(name = "error_message")
    private String errorMessage;
}

