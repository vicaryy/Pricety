package com.vicary.pricety.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;

    private String name;

    private String description;

    private String photoUrl;

    private double price;

    private String variant;

    private String link;

    private String priceAlert;

    private String serviceName;

    private String currency;

    private boolean notifyWhenAvailable;

    private long userId;
}
