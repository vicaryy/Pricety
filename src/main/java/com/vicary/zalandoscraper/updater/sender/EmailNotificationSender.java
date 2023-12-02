package com.vicary.zalandoscraper.updater.sender;

import com.vicary.zalandoscraper.sender.EmailSender;
import com.vicary.zalandoscraper.model.Email;
import com.vicary.zalandoscraper.service.repository_services.NotificationEmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
class EmailNotificationSender {

    private final static Logger logger = LoggerFactory.getLogger(EmailNotificationSender.class);
    private int emailsSent;
    private int emailsFailed;

    private final EmailSender emailSender;

    private final NotificationEmailService notificationEmailService;

    public void send(List<Email> emails) {
        emailsSent = 0;
        emailsFailed = 0;
        emails.forEach(e -> send(e, false));
    }

    public void sendAndSave(List<Email> emails) {
        emailsSent = 0;
        emailsFailed = 0;
        emails.forEach(e -> send(e, true));
    }

    private void send(Email email, boolean save) {
        try {
            emailSender.send(email);
            emailsSent++;
            if (save)
                saveToRepository(email);
        } catch (Exception e) {
            logger.warn("[Email Notification Sender] Failed in sending email notification to '{}'", email.getTo());
            logger.warn("[Email Notification Sender] {}", e.getMessage());
            emailsFailed++;
        }
    }

    private void saveToRepository(Email email) {
        notificationEmailService.saveEntity(email);
    }

    public int getSentAmountAndReset() {
        int amount = emailsSent;
        emailsSent = 0;
        return amount;
    }

    public int getFailedAmountAndReset() {
        int amount = emailsFailed;
        emailsFailed = 0;
        return amount;
    }

    public int getFailedAmount() {
        return emailsFailed;
    }
}

