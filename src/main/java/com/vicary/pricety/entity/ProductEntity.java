package com.vicary.pricety.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products_test", schema = "public")
public class ProductEntity implements Identifiable {
    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator",
            strategy = "com.vicary.pricety.configuration.IdGenerator")
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "price")
    private double price;

    @Column(name = "variant")
    private String variant;

    @Column(name = "link")
    private String link;

    @Column(name = "price_alert")
    private String priceAlert;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "currency")
    private String currency;

    @Column(name = "notify_when_available")
    private boolean notifyWhenAvailable;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
