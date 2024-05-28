package com.vicary.pricety.entity;

import com.vicary.pricety.model.Identifiable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Builder
@Table(name = "waiting_users")
public class WaitingUserEntity implements Identifiable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator",
            strategy = "com.vicary.pricety.configuration.IdGenerator")
    @Column(name = "id")
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
