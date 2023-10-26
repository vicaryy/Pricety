package com.vicary.zalandoscraper.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products", schema = "public")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "variant")
    private String variant;

    @Column(name = "link")
    private String link;

    @Column(name = "price_alert")
    private String priceAlert;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
