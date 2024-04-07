package com.vicary.pricety.factory;

import com.vicary.pricety.model.Email;
import com.vicary.pricety.model.Product;

public class EmailNotificationFactory {

    private EmailNotificationFactory() {
    }


    public static Email getPriceAlertNotification(Product product) {
        Email notification = new Email(product.getUserDTO().getEmail());
        notification.setPriceAlertMessageAndTitle(product);
        return notification;
    }
}
