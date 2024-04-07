package com.vicary.pricety.service.map;

import com.vicary.pricety.entity.NotificationEmailEntity;
import com.vicary.pricety.model.Email;
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
