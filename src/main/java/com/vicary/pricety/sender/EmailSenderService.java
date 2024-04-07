package com.vicary.pricety.sender;

import com.vicary.pricety.model.Email;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
public class EmailSenderService {

    private final static Logger logger = LoggerFactory.getLogger(EmailSenderService.class);

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    public synchronized void send(Email email) throws Exception {
        email.checkValidation();
        if (email.isMime())
            sendAsMime(email);
        else
            sendAsSimple(email);
    }

    private void sendAsSimple(Email email) throws Exception {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(email.getTitle());
        mailMessage.setText(email.getMessage());
        mailMessage.setTo(email.getTo());

        mailSender.send(mailMessage);
    }

    private void sendAsMime(Email email) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        messageHelper.setTo(email.getTo());
        messageHelper.setSubject(email.getTitle());
        messageHelper.setText(email.getMessage(), true);

        mailSender.send(message);
    }

    public void sendLinkActivation(String to, String link) throws Exception {
        Email email = new Email();
        email.setTo(to);
        email.setTitle("[Verification] Your verification link!");
        email.setMime(true);
        Context context = new Context();
        context.setVariable("time", LocalDateTime.now());
        context.setVariable("link", link);
        String html = templateEngine.process("/emails/activation-link", context);
        email.setMessage(html);
        send(email);
    }
}
