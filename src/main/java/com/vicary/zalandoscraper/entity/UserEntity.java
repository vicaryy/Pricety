package com.vicary.zalandoscraper.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
@Table(name = "USERS")
public class UserEntity {
    @Id
    @Column(name = "USER_ID")
    @NonNull
    private String userId;

    @Column(name = "NICK")
    private String nick;

    @Column(name = "NATIONALITY")
    private String nationality;

    @Column(name = "PREMIUM")
    private boolean premium;

    @Column(name = "ADMIN")
    private boolean admin;

    @OneToMany(mappedBy = "user")
    Set<ProductEntity> products = new HashSet<>();
}
