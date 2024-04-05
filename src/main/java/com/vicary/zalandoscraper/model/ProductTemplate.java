package com.vicary.zalandoscraper.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTemplate {
    private Long productId;

    private String photoUrl;

    private String name;

    private String description;

    private String price;

    private String currency;

    private String variant;

    private String priceAlert;

    private String link;

    private String serviceName;

    private boolean notifyWhenAvailable;

    private UserDTO userDTO;
}