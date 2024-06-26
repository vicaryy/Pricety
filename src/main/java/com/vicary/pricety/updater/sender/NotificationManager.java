package com.vicary.pricety.updater.sender;

import com.vicary.pricety.entity.WaitingUserEntity;
import com.vicary.pricety.factory.ChatNotificationFactory;
import com.vicary.pricety.model.Email;
import com.vicary.pricety.model.ChatNotification;
import com.vicary.pricety.factory.EmailNotificationFactory;
import com.vicary.pricety.model.Product;
import com.vicary.pricety.service.repository_services.ProductHistoryService;
import com.vicary.pricety.service.repository_services.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationManager {

    private final static Logger logger = LoggerFactory.getLogger(NotificationManager.class);

    private final ProductService productService;

    private final ChatNotificationSender chatSender;

    private final EmailNotificationSender emailSender;

    private final ProductHistoryService productHistoryService;

    public void sendPriceNotifications(List<Product> products) {
        List<Product> listThatNeedsToBeSend = products.stream()
                .filter(this::isUserNeedsNotify)
                .toList();
        if (listThatNeedsToBeSend.isEmpty()) {
            logger.info("[Notification Manager] No price notifications to send.");
            return;
        }
//        listThatNeedsToBeSend.forEach(this::updatePriceAlertInRepository); // probably don't need?

        List<ChatNotification> chatNotifications = getChatNotifications(listThatNeedsToBeSend);
        List<Email> emails = getEmailNotifications(listThatNeedsToBeSend);

        displayLogsBeforeSendPriceNotifications(chatNotifications.size(), emails.size());

        chatSender.sendAndSave(chatNotifications);
        emailSender.sendAndSave(emails);

        displayLogsAfterSendPriceNotifications();
    }

    public void sendWaitingUserNotifications(List<WaitingUserEntity> waitingUsers) {
        if (waitingUsers.isEmpty()) {
            logger.info("[Notification Manager] No waiting user notifications to send.");
            return;
        }
        List<ChatNotification> waitingUserNotifications = getWaitingUserNotifications(waitingUsers);

        displayLogsBeforeSendWaitingUser(waitingUserNotifications.size());
        chatSender.send(waitingUserNotifications);
        displayLogsAfterSendWaitingUser();
    }


    boolean isUserNeedsNotify(Product p) {
        if (isAvailabilityNotification(p.isNotifyWhenAvailable(), p.getPrice(), p.getNewPrice()))
            return true;

        if (isAutoNotificationWhenOldPriceIsZero(p.getPriceAlert(), p.getPrice(), p.getNewPrice())) {
            p.setPrice(productHistoryService.getLastPositivePrice(p.getProductId()));
            return p.getNewPrice() < p.getPrice();
        }

        if (p.getPriceAlert().equals("OFF") || p.getNewPrice() == 0)
            return false;

        if (p.getPriceAlert().equals("AUTO"))
            return p.getNewPrice() < p.getPrice();

        double priceAlert = Double.parseDouble(p.getPriceAlert());

        return p.getNewPrice() <= priceAlert;
    }


    private boolean isAutoNotificationWhenOldPriceIsZero(String priceAlert, double price, double newPrice) {
        return priceAlert.equals("AUTO") && newPrice != 0 && price == 0;
    }

    private boolean isAvailabilityNotification(boolean notifyWhenAvailable, double price, double newPrice) {
        return notifyWhenAvailable && price == 0 && newPrice != 0;
    }

    void updatePriceAlertInRepository(Product p) {
        if (p.getPriceAlert().equals("OFF") || p.getPriceAlert().equals("AUTO"))
            return;

        double priceAlert = Double.parseDouble(p.getPriceAlert());
        if (p.getNewPrice() <= priceAlert)
            productService.updateProductPriceAlert(p.getProductId(), "OFF");
    }

    private List<ChatNotification> getChatNotifications(List<Product> products) {
        List<Product> listWithEnabledChatNotifies = products.stream()
                .filter(p -> p.getUserDTO().isTelegram())
                .toList();
        if (listWithEnabledChatNotifies.isEmpty())
            return Collections.emptyList();

        return listWithEnabledChatNotifies.stream()
                .map(ChatNotificationFactory::getPriceAlertNotification)
                .toList();
    }

    private List<Email> getEmailNotifications(List<Product> products) {
        List<Product> listWithEnabledEmailNotifies = products.stream()
                .filter(p -> p.getUserDTO().isEmailNotifications())
                .toList();
        if (listWithEnabledEmailNotifies.isEmpty())
            return Collections.emptyList();

        return listWithEnabledEmailNotifies.stream()
                .map(EmailNotificationFactory::getPriceAlertNotification)
                .toList();
    }

    private List<ChatNotification> getWaitingUserNotifications(List<WaitingUserEntity> waitingUsers) {
        return waitingUsers.stream()
                .map(w -> ChatNotificationFactory.getWaitingUserNotification(w.getUser().getTelegramId(), w.getUser().getNationality()))
                .toList();
    }

    private void displayLogsBeforeSendWaitingUser(int waitingUserSize) {
        logger.info("[Notification Manager] Sending {} waiting user notifications.", waitingUserSize);
    }

    private void displayLogsAfterSendWaitingUser() {
        logger.info("[Notification Manager] Successfully sent {} waiting user notifications.", chatSender.getSentAmountAndReset());

        if (chatSender.getFailedAmount() != 0)
            logger.info("[Notification Manager] Failed {} in sending waiting user notifications.", chatSender.getFailedAmountAndReset());
    }

    private void displayLogsBeforeSendPriceNotifications(int chatNotifiesSize, int emailNotifiesSize) {
        logger.info("[Notification Manager] Sending {} chat notifications.", chatNotifiesSize);
        logger.info("[Notification Manager] Sending {} email notifications.", emailNotifiesSize);
    }

    private void displayLogsAfterSendPriceNotifications() {
        logger.info("[Notification Manager] Successfully sent {} chat notifications.", chatSender.getSentAmountAndReset());
        logger.info("[Notification Manager] Successfully sent {} email notifications.", emailSender.getSentAmountAndReset());

        if (chatSender.getFailedAmount() != 0)
            logger.info("[Notification Manager] Failed {} in sending chat notifications.", chatSender.getFailedAmountAndReset());

        if (emailSender.getFailedAmount() != 0)
            logger.info("[Notification Manager] Failed {} in sending email notifications.", emailSender.getFailedAmountAndReset());
    }
}
