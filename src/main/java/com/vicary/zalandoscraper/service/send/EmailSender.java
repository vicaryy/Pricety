package com.vicary.zalandoscraper.service.send;

import com.vicary.zalandoscraper.model.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender mailSender;

    private void send(Email email) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(email.getTitle());
        mailMessage.setText(email.getMessage());
        mailMessage.setTo(email.getTo());

        mailSender.send(mailMessage);
    }

    public void sendAsMime(Email email) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        try {
            messageHelper.setTo(email.getTo());
            messageHelper.setSubject(email.getTitle());
            messageHelper.setText(email.getMessage(), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
