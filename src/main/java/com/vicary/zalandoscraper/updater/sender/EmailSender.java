package com.vicary.zalandoscraper.updater.sender;

import com.vicary.zalandoscraper.model.Email;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class EmailSender {

    private final static Logger logger = LoggerFactory.getLogger(EmailSender.class);

    private final JavaMailSender mailSender;
    private int emailsSent;

    public void sendAsNotification(Email notification) {
        if (notification.isMime())
            sendAsMime(notification);
        else
            sendAsSimple(notification);
    }


    private void sendAsSimple(Email notification) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(notification.getTitle());
        mailMessage.setText(notification.getMessage());
        mailMessage.setTo(notification.getTo());

        try {
            mailSender.send(mailMessage);
            emailsSent++;
        } catch (Exception e) {
            logger.warn("[Email Notification Sender] Failed in sending email as mime to '{}'", notification.getTo());
            logger.warn("[Email Notification Sender] {}", e.getMessage());
        }
    }

    private void sendAsMime(Email email) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        try {
            messageHelper.setTo(email.getTo());
            messageHelper.setSubject(email.getTitle());
            messageHelper.setText(email.getMessage(), true);

            mailSender.send(message);
            emailsSent++;
        } catch (Exception e) {
            logger.warn("[Email Notification Sender] Failed in sending email as mime to '{}'", email.getTo());
            logger.warn("[Email Notification Sender] {}", e.getMessage());
        }
    }

    public void sendAsNotification(Email notification) {
        if (notification.isMime())
            sendAsMime(notification);
        else
            sendAsSimple(notification);
    }


    private void sendAsSimple(Email notification) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(notification.getTitle());
        mailMessage.setText(notification.getMessage());
        mailMessage.setTo(notification.getTo());

        try {
            mailSender.send(mailMessage);
            emailsSent++;
        } catch (Exception e) {
            logger.warn("[Email Notification Sender] Failed in sending email as mime to '{}'", notification.getTo());
            logger.warn("[Email Notification Sender] {}", e.getMessage());
        }
    }

    private void sendAsMime(Email email) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        try {
            messageHelper.setTo(email.getTo());
            messageHelper.setSubject(email.getTitle());
            messageHelper.setText(email.getMessage(), true);

            mailSender.send(message);
            emailsSent++;
        } catch (Exception e) {
            logger.warn("[Email Notification Sender] Failed in sending email as mime to '{}'", email.getTo());
            logger.warn("[Email Notification Sender] {}", e.getMessage());
        }
    }

    public int getSentAmountAndReset() {
        int amount = emailsSent;
        resetSentAmount();
        return amount;
    }

    private int resetSentAmount() {
        emailsSent = 0;
    }
}
