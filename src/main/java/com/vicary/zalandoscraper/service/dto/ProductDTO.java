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

    private String link;

    private String variant;

    private double price;

    private String priceAlert;
}
