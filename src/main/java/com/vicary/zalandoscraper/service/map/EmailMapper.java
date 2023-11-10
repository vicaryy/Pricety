package com.vicary.zalandoscraper.service.map;

import com.vicary.zalandoscraper.entity.NotificationEmailEntity;
import com.vicary.zalandoscraper.model.Email;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmailMapper {

    public NotificationEmailEntity map(Email email) {
        return NotificationEmailEntity.builder()
                .toEmail(email.getTo())
                .message(email.getMessage())
                .sentDate(LocalDateTime.now())
                .build();
    }
}
