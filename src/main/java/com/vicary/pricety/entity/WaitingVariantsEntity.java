package com.vicary.pricety.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "waiting_variants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaitingVariantsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "url")
    private String url;

    @Column(name = "variants")
    private List<String> variants;

    @Column(name = "error")
    private boolean error;

    @Column(name = "error_message")
    private String errorMessage;

    public static WaitingVariantsEntity requestEntity(String url) {
        return WaitingVariantsEntity.builder()
                .url(url)
                .variants(new ArrayList<>())
                .error(false)
                .errorMessage("")
                .build();
    }

    public static WaitingVariantsEntity emptyEntity() {
        return WaitingVariantsEntity.builder()
                .url("")
                .build();
    }
}
