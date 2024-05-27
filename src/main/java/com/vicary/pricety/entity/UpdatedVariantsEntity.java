package com.vicary.pricety.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "updated_variants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedVariantsEntity {
    @Id
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

    public static UpdatedVariantsEntity errorEntity(String errorMessage) {
        return UpdatedVariantsEntity.builder()
                .error(true)
                .errorMessage(errorMessage)
                .build();
    }
}
