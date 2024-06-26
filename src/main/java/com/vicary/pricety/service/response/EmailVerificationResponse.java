package com.vicary.pricety.service.response;

import com.vicary.pricety.messages.Messages;
import com.vicary.pricety.thread_local.ActiveUser;
import com.vicary.pricety.exception.IllegalInputException;
import com.vicary.pricety.api_telegram.service.QuickSender;

public class EmailVerificationResponse implements Responser {
    private final ResponseFacade responseFacade;
    private final ActiveUser user;
    private final QuickSender quickSender;

    public EmailVerificationResponse(ResponseFacade responseFacade, ActiveUser user) {
        this.responseFacade = responseFacade;
        this.user = user;
        this.quickSender = new QuickSender();
    }

    public EmailVerificationResponse(ResponseFacade responseFacade, ActiveUser user, QuickSender quickSender) {
        this.responseFacade = responseFacade;
        this.user = user;
        this.quickSender = quickSender;
    }

    @Override
    public void response() {
        String token = user.getText().substring(2);

        if (user.getEmail() == null || user.isVerifiedEmail())
            return;

        if (!responseFacade.emailVerExistsByUserIdAndToken(user.getUserId(), token))
            throw new IllegalInputException(Messages.other("invalidEmailVerificationCode"), "User '%s' type wrong email verification code.".formatted(user.getTelegramId()));

        responseFacade.setUserVerifiedEmail(user.getUserId(), true);
        responseFacade.deleteEmailVerByToken(token);

        quickSender.message(user.getChatId(), Messages.other("emailVerifiedSuccessfully"), false);
    }
}
