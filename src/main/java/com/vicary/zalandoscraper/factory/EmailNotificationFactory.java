package com.vicary.zalandoscraper.factory;

import com.vicary.zalandoscraper.model.Email;
import com.vicary.zalandoscraper.service.dto.ProductDTO;

public class EmailNotificationFactory {

    private EmailNotificationFactory() {
    }


    public static Email getPriceAlertNotification(ProductDTO dto) {
        Email notification = new Email(dto.getEmail());
        notification.setPriceAlertMessageAndTitle(dto);
        return notification;
    }
}
