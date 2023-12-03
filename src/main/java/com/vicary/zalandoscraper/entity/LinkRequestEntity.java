package com.vicary.zalandoscraper.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "link_requests")
public class LinkRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "link")
    private String link;

    @Column(name = "expiration")
    private long expiration;
}
