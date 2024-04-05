package com.vicary.zalandoscraper.factory;

import com.vicary.zalandoscraper.model.ChatNotification;
import com.vicary.zalandoscraper.model.Product;

public class ChatNotificationFactory {
    private ChatNotificationFactory() {
    }

    public static ChatNotification getPriceAlertNotification(Product product) {
        ChatNotification notification = new ChatNotification();
        notification.setChatId(product.getUserDTO().getTelegramId());
        notification.setMarkdownV2(true);
        notification.setDefaultPriceAlertMessage(product);
        return notification;
    }

    public static ChatNotification getWaitingUserNotification(String chatId, String language) {
        ChatNotification notification = new ChatNotification();
        notification.setChatId(chatId);
        notification.setWaitingUserMessage(language);
        notification.setMarkdownV2(true);
        return notification;
    }
}
