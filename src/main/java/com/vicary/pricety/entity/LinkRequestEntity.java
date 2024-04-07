package com.vicary.pricety.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "link_requests")
public class LinkRequestEntity implements Identifiable {
    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator",
            strategy = "com.vicary.pricety.configuration.IdGenerator")    @Column(name = "id")
    private Long id;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "link")
    private String link;

    @Column(name = "expiration")
    private long expiration;
}
