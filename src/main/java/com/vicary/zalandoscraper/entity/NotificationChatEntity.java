package com.vicary.zalandoscraper.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications_chat", schema = "public")
public class NotificationChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "to_user_id")
    private String toUserId;

    @Column(name = "message")
    private String message;

    @Column(name = "sent_date")
    private LocalDateTime sentDate;
}
