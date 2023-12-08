package com.vicary.zalandoscraper.factory;

import com.vicary.zalandoscraper.model.ChatNotification;
import com.vicary.zalandoscraper.service.dto.ProductDTO;

public class ChatNotificationFactory {
    private ChatNotificationFactory() {
    }

    public static ChatNotification getPriceAlertNotification(ProductDTO dto) {
        ChatNotification notification = new ChatNotification();
        notification.setChatId(dto.getUserId());
        notification.setMarkdownV2(true);
        notification.setDefaultPriceAlertMessage(dto);
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
