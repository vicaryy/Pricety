package com.vicary.pricety.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "updates_history", schema = "public")
public class ProductHistoryEntity implements Identifiable {
    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator",
            strategy = "com.vicary.pricety.configuration.IdGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "price")
    private double price;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
}



