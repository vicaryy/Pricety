package com.vicary.pricety.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "waiting_products_price")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaitingProductPriceEntity {
    @Id
    @Column(name = "product_id")
    private long productId;

    @Column(name = "new_price")
    private double newPrice;

    @Column(name = "link")
    private String link;

    @Column(name = "variant")
    private String variant;

    @Column(name = "service_name")
    private String serviceName;
}
