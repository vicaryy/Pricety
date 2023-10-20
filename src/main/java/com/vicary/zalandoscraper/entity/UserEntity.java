package com.vicary.zalandoscraper.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
@Table(name = "USERS")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "NICK")
    private String nick;

    @Column(name = "NATIONALITY")
    private String nationality;

    @Column(name = "PREMIUM")
    private boolean premium;

    @Column(name = "ADMIN")
    private boolean admin;

    @Column(name = "USER_ID")
    @NonNull
    private String userId;
}
