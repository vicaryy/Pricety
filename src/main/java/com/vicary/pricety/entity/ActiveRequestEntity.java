package com.vicary.pricety.entity;

import com.vicary.pricety.model.Identifiable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
@Table(name = "active_requests")
public class ActiveRequestEntity implements Identifiable {
    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator",
            strategy = "com.vicary.pricety.configuration.IdGenerator")
    @Column(name = "ID")
    private Long id;

    @NonNull
    @Column(name = "USER_ID")
    private String userId;
}
