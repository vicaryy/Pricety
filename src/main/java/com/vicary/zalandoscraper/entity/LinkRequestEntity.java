package com.vicary.zalandoscraper.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "LINK_REQUESTS")
public class LinkRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "REQUEST_ID")
    private String requestId;

    @Column(name = "LINK")
    private String link;

    @Column(name = "EXPIRATION")
    private long expiration;
}
