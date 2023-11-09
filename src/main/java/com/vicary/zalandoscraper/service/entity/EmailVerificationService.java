package com.vicary.zalandoscraper.service.entity;

import com.vicary.zalandoscraper.entity.EmailVerificationEntity;
import com.vicary.zalandoscraper.model.Email;
import com.vicary.zalandoscraper.repository.EmailVerificationRepository;
import com.vicary.zalandoscraper.updater.sender.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationRepository repository;

    private final EmailSender emailSender;

    public EmailVerificationEntity createVerification(String userId) {
        var entity = EmailVerificationEntity.builder()
                .userId(userId)
                .token(generateToken())
                .build();

        saveToRepository(entity);

        return entity;
    }

    public EmailVerificationEntity findByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    public boolean existsByUserIdAndToken(String userId, String token) {
        return repository.existsByUserIdAndToken(userId, token);
    }

    public void deleteByToken(String token) {
        repository.deleteByToken(token);
    }

    public void deleteAllByUserId(String userId) {
        repository.deleteAllByUserId(userId);
    }

    public boolean existsByUserId(String userId) {
        return repository.existsByUserId(userId) == 1;
    }

    private void saveToRepository(EmailVerificationEntity e) {
        repository.save(e);
    }

    private String generateToken() {
        StringBuilder sb = new StringBuilder();
        IntStream intStream = ThreadLocalRandom.current().ints(10, 0, 10);
        intStream.forEach(sb::append);
        return sb.toString();
    }

    public void sendTokenToUser(EmailVerificationEntity verification, String email) {
        Email emailBody = Email.builder()
                .to(email)
                .title("[Verification] Email verification code")
                .message(getMessage(verification.getToken()))
                .build();

        emailSender.sendAsMime(emailBody);
    }

    private String getMessage(String token) {
        return """
                <html>
                <body>
                <font size=3>Here is your verification code.</font><br><br>
                <font size=3>Paste it into the chat with the bot:</font><br>
                <font size=4><b>v-%s</b></font><br><br>
                <font size=1><i>If you don't recognize this message, please ignore it.</i></font>"""
                .formatted(token);
    }
}

















