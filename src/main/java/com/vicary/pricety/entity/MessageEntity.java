package com.vicary.pricety.entity;

import com.vicary.pricety.model.Identifiable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "messages_history", schema = "public")
public class MessageEntity implements Identifiable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator",
            strategy = "com.vicary.pricety.configuration.IdGenerator")    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "message")
    private String message;

    @Column(name = "sent_date")
    private LocalDateTime sentDate;
}
