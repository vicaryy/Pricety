package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.service.entity.EmailVerificationService;
import com.vicary.zalandoscraper.service.entity.UserService;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationResponse {

    private final EmailVerificationService emailVerificationService;

    private final UserService userService;

    public void response(String text) {
        ActiveUser user = ActiveUser.get();

        String token = text.substring(2);

        if (user.getEmail() == null || user.isVerifiedEmail())
            return;

        if (!emailVerificationService.existsByUserIdAndToken(user.getUserId(), token))
            throw new IllegalInputException(Messages.other("invalidEmailVerificationCode"), "User '%s' type wrong email verification code.".formatted(user.getUserId()));

        userService.setVerifiedEmail(user.getUserId(), true);
        emailVerificationService.deleteByToken(token);

        QuickSender.message(user.getChatId(), Messages.other("emailVerifiedSuccessfully"), false);
    }
}
