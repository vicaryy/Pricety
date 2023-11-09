package com.vicary.zalandoscraper.updater.sender;

import com.vicary.zalandoscraper.model.ChatNotification;
import com.vicary.zalandoscraper.service.quick_sender.QuickSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ChatNotificationSender {

    private final QuickSender quickSender;

    private int messagesSent;

    public void send(ChatNotification notification) {
        quickSender.message(
                notification.getChatId(),
                notification.getMessage(),
                notification.isMarkdownV2());

        messagesSent++;
    }

    public int getSentAmountAndReset() {
        int amount = messagesSent;
        messagesSent = 0;
        return amount;
    }
}
