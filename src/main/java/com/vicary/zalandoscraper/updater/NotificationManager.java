package com.vicary.zalandoscraper.updater;

import com.vicary.zalandoscraper.model.Email;
import com.vicary.zalandoscraper.model.ChatNotification;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.service.entity.ProductService;
import com.vicary.zalandoscraper.updater.sender.EmailSender;
import com.vicary.zalandoscraper.updater.sender.ChatNotificationSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationManager {

    private final static Logger logger = LoggerFactory.getLogger(NotificationManager.class);

    private final ProductService productService;

    private final ChatNotificationSender chatSender;

    private final EmailSender emailSender;

    public void send(List<ProductDTO> DTOs) {
        List<ProductDTO> listThatNeedsToBeSend = DTOs.stream()
                .filter(this::isUserNeedsNotify)
                .toList();
        listThatNeedsToBeSend.forEach(this::updatePriceAlertInRepository);

        List<ChatNotification> chatNotifications = getChatNotifications(listThatNeedsToBeSend);
        List<Email> emails = getEmailNotifications(listThatNeedsToBeSend);

        logger.info("[Notification Manager] Sending {} chat notifications.", chatNotifications.size());
        logger.info("[Notification Manager] Sending {} email notifications.", emails.size());

        chatNotifications.forEach(this::sendOnChat);
        emails.forEach(this::sendOnEmail);

        logger.info("[Notification Manager] Successfully sent {} chat notifications.", chatSender.getSentAmountAndReset());
        logger.info("[Notification Manager] Successfully sent {} email notifications.", emailSender.getSentAmountAndReset());
    }

    private boolean isUserNeedsNotify(ProductDTO p) {
        if (p.getPriceAlert().equals("0") || p.getNewPrice() == 0)
            return false;

        if (p.getPriceAlert().equals("AUTO"))
            return p.getNewPrice() < p.getPrice();

        double priceAlert = Double.parseDouble(p.getPriceAlert());

        return p.getNewPrice() <= priceAlert;
    }

    private void updatePriceAlertInRepository(ProductDTO p) {
        if (p.getPriceAlert().equals("0") || p.getPriceAlert().equals("AUTO"))
            return;

        double priceAlert = Double.parseDouble(p.getPriceAlert());
        if (p.getNewPrice() <= priceAlert)
            productService.updateProductPriceAlert(p.getProductId(), "0");
    }

    private List<ChatNotification> getChatNotifications(List<ProductDTO> DTOs) {
        List<ChatNotification> chatNotifications = new ArrayList<>();
        for (ProductDTO p : DTOs) {
            ChatNotification notification = new ChatNotification();
            notification.setChatId(p.getUserId());
            notification.setMarkdownV2(true);
            notification.setDefaultMessageNotification(p);
            chatNotifications.add(notification);
        }
        return chatNotifications.isEmpty() ? Collections.emptyList() : chatNotifications;
    }

    private List<Email> getEmailNotifications(List<ProductDTO> DTOs) {
        List<Email> emails = new ArrayList<>();
        for (ProductDTO p : DTOs) {
            if (p.isNotifyByEmail()) {
                Email notification = new Email();
                notification.setTo(p.getEmail());
                notification.setPriceAlertMessage(p);
                notification.setPriceAlertTitle();
                emails.add(notification);
            }
        }
        return emails.isEmpty() ? Collections.emptyList() : emails;
    }

    private void sendOnChat(ChatNotification notification) {
        chatSender.send(notification);
    }

    private void sendOnEmail(Email notification) {
        emailSender.send(notification);
    }
}
