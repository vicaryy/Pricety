package com.vicary.zalandoscraper.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "updates_history", schema = "public")
public class ProductHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "price")
    private double price;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
}



