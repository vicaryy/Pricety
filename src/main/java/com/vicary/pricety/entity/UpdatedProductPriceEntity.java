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
@Table(name = "updated_products_price")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdatedProductPriceEntity {
    @Id
    @Column(name = "product_id")
    private long productId;

    @Column(name = "new_price")
    private double newPrice;
}
