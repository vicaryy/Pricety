package com.vicary.zalandoscraper.service.repository_services;

import com.vicary.zalandoscraper.entity.EmailVerificationEntity;
import com.vicary.zalandoscraper.model.Email;
import com.vicary.zalandoscraper.repository.EmailVerificationRepository;
import com.vicary.zalandoscraper.sender.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final static Logger logger = LoggerFactory.getLogger(EmailVerificationService.class);

    private final EmailVerificationRepository repository;

    private final EmailSenderService emailSenderService;


    public EmailVerificationEntity createVerification(long userId) {
        var entity = EmailVerificationEntity.builder()
                .userId(userId)
                .token(generateToken())
                .build();

        saveToRepository(entity);

        return entity;
    }

    public EmailVerificationEntity findByUserId(long userId) {
        return repository.findByUserId(userId);
    }

    public EmailVerificationEntity findByToken(String token) {
        return repository.findByToken(token).orElseThrow();
    }

    @Transactional
    public Optional<EmailVerificationEntity> findByTokenAndDelete(String token) {
        Optional<EmailVerificationEntity> entity = repository.findByToken(token);
        if (entity.isPresent())
            deleteByToken(token);
        return entity;
    }


    public boolean existsByUserIdAndToken(long userId, String token) {
        return repository.existsByUserIdAndToken(userId, token);
    }

    public boolean existsByToken(String token) {
        return repository.existsByToken(token) == 1;
    }

    public void deleteByToken(String token) {
        repository.deleteByToken(token);
    }

    public void deleteAllByUserId(long userId) {
        repository.deleteAllByUserId(userId);
    }

    public boolean existsByUserId(long userId) {
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
            emailSenderService.send(emailBody);
        } catch (Exception e) {
            logger.warn("[Email Verification Service] Failed sent email verification to {}", email);
        }
    }
}

















