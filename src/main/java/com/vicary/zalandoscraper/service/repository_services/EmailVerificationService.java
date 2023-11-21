package com.vicary.zalandoscraper.service.repository_services;

import com.vicary.zalandoscraper.entity.EmailVerificationEntity;
import com.vicary.zalandoscraper.model.Email;
import com.vicary.zalandoscraper.repository.EmailVerificationRepository;
import com.vicary.zalandoscraper.sender.EmailSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final static Logger logger = LoggerFactory.getLogger(EmailVerificationService.class);

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
        Email emailBody = new Email(email);
        emailBody.setVerificationMessageAndTitle(verification.getToken());

        try {
            emailSender.send(emailBody);
        } catch (Exception e) {
            logger.warn("[Email Verification Service] Failed sent email verification to {}", email);
        }
    }
}

















