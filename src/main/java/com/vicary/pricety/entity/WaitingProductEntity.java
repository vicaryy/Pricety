package com.vicary.pricety.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "waiting_products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaitingProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long productId;

    @Column(name = "url")
    private String url;

    @Column(name = "variant")
    private String variant;

    public static WaitingProductEntity requestEntity(String url, String variant) {
        return WaitingProductEntity.builder()
                .url(url)
                .variant(variant)
                .build();
    }

    public static WaitingProductEntity emptyEntity() {
        return WaitingProductEntity.builder()
                .productId(0)
                .url("")
                .variant("")
                .build();
    }
}
