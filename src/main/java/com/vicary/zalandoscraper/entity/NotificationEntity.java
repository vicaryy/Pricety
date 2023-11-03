package com.vicary.zalandoscraper.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.common.value.qual.DoesNotMatchRegex;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notifications")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "email")
    private String email;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "description")
    private String description;

    @Column(name = "variant")
    private String variant;

    @Column(name = "new_price")
    private double newPrice;

    @Column(name = "old_price")
    private double oldPrice;

    @Column(name = "price_alert")
    private String priceAlert;

    @Column(name = "link")
    private String link;

    @Column(name = "notify_by_email")
    private boolean notifyByEmail;
}
