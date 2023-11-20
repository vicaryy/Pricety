package com.vicary.zalandoscraper.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private Long productId;

    private String userId;

    private String name;

    private String description;

    private String link;

    private String variant;

    private double price;

    private double newPrice;

    private String priceAlert;

    private String email;

    private String language;

    private boolean notifyByEmail;

    public String getServiceName() {
        return link.split("\\.")[1] + ".pl";
    }
}
