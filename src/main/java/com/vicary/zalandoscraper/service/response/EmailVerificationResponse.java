package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.service.entity.EmailVerificationService;
import com.vicary.zalandoscraper.service.entity.UserService;
import com.vicary.zalandoscraper.service.quick_sender.QuickSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationResponse {

    private final EmailVerificationService emailVerificationService;

    private final UserService userService;

    private final QuickSender quickSender;

    public void response(String text) {
        ActiveUser user = ActiveUser.get();

        String token = text.substring(2);

        if (user.getEmail() == null || user.isVerifiedEmail())
            return;

        if (!emailVerificationService.existsByUserIdAndToken(user.getUserId(), token))
            throw new IllegalInputException("Invalid email verification code.", "User '%s' type wrong email verification code.".formatted(user.getUserId()));

        userService.setVerifiedEmail(user.getUserId(), true);
        emailVerificationService.deleteByToken(token);

        quickSender.message(user.getChatId(), "Email verified successfully.", false);
    }
}
