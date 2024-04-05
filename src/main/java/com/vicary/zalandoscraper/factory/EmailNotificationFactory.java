package com.vicary.zalandoscraper.factory;

import com.vicary.zalandoscraper.model.Email;
import com.vicary.zalandoscraper.model.Product;

public class EmailNotificationFactory {

    private EmailNotificationFactory() {
    }


    public static Email getPriceAlertNotification(Product product) {
        Email notification = new Email(product.getUserDTO().getEmail());
        notification.setPriceAlertMessageAndTitle(product);
        return notification;
    }
}
