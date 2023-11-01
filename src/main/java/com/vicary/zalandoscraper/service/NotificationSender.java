package com.vicary.zalandoscraper.service;

import com.vicary.zalandoscraper.entity.NotificationEntity;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.service.quick_sender.QuickSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationSender {

    private final QuickSender quickSender;

    public void send(List<NotificationEntity> notifications) {
        for (NotificationEntity n : notifications) {

            sendOnTelegram(n);

            if (n.isNotifyByEmail())
                sendOnEmail(n);
        }
    }

    private void sendOnTelegram(NotificationEntity n) {
        quickSender.message(n.getUserId(), getMessage(n), false);
    }

    private void sendOnEmail(NotificationEntity n) {

    }

    private String getMessage(NotificationEntity n) {
        return """
                Notification
                                
                Hello, the product you have watched became cheaper by %.2f zł!
                                
                Name: %s
                Description: %s
                Link: %s
                                
                Old Price: %.2f zł
                New Price: %.2f zł
                                
                Have a nice shopping!
                """.formatted(
                n.getOldPrice() - n.getNewPrice(),
                n.getProductName(),
                n.getDescription(),
                n.getLink(),
                n.getOldPrice(),
                n.getNewPrice()
        );
    }

}
