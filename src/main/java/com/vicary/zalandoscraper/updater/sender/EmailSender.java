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

import java.util.List;


@Component
@RequiredArgsConstructor
public class EmailSender {

    private final static Logger logger = LoggerFactory.getLogger(EmailSender.class);

    private final JavaMailSender mailSender;

    public void sendAsSimple(Email email) throws Exception {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(email.getTitle());
        mailMessage.setText(email.getMessage());
        mailMessage.setTo(email.getTo());

        mailSender.send(mailMessage);
    }

    public void sendAsMime(Email email) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        messageHelper.setTo(email.getTo());
        messageHelper.setSubject(email.getTitle());
        messageHelper.setText(email.getMessage(), true);

        mailSender.send(message);
    }
}
