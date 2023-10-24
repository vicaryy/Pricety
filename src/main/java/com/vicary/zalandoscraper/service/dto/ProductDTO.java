package com.vicary.zalandoscraper.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private Long productId;

    private String name;

    private String description;

    private String link;

    private String variant;

    private double price;

    private double newPrice;

    private String priceAlert;

    private LocalDateTime lastUpdate;
}
