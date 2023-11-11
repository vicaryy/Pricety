package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.pattern.Pattern;
import com.vicary.zalandoscraper.service.entity.AwaitedMessageService;
import com.vicary.zalandoscraper.service.entity.EmailVerificationService;
import com.vicary.zalandoscraper.service.entity.ProductService;
import com.vicary.zalandoscraper.service.entity.UserService;
import com.vicary.zalandoscraper.api_telegram.QuickSender;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AwaitedMessageResponse {

    private final AwaitedMessageService awaitedMessageService;

    private final ProductService productService;

    private final UserService userService;

    private final EmailVerificationService emailVerificationService;


    public void response() {
        String request = awaitedMessageService.getRequestAndDeleteMessage(ActiveUser.get().getUserId());

        if (request.startsWith("-edit"))
            updateProductPriceAlert(request);

        else if (request.startsWith("-setEmail"))
            updateUserEmail();
    }

    @SneakyThrows
    public void updateProductPriceAlert(String request) {
        ActiveUser user = ActiveUser.get();

        String[] requestArray = request.split(" ");

        Long productId = Long.parseLong(requestArray[1]);
        String priceAlert = getPriceAlertFromText(user.getText());

        productService.updateProductPriceAlert(productId, priceAlert);

        popupMessage("Price Alert updated successfully.");
        displayMenu();
    }

    @SneakyThrows
    public void updateUserEmail() {
        ActiveUser user = ActiveUser.get();
        String email = user.getText();

        if (!Pattern.isEmail(email))
            throw new IllegalInputException("The provided email doesn't look like a valid email address.", "User '%s' typed invalid email '%s'".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

        if (email.equals(user.getEmail()))
            throw new IllegalInputException("The provided email must be different from the one you already have.", "User '%s' typed the same email '%s'".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

        if (email.equalsIgnoreCase("DELETE")) {
            userService.deleteEmailById(user.getUserId());
            popupMessage("Email deleted successfully");
            displayMenu();
            return;
        }

        userService.updateEmailById(user.getUserId(), email);

        if (emailVerificationService.existsByUserId(user.getUserId()))
            emailVerificationService.deleteAllByUserId(user.getUserId());

        var verification = emailVerificationService.createVerification(user.getUserId());

        emailVerificationService.sendTokenToUser(verification, email);

        String verificationMessage = """
                Email updated successfully\\.
                      
                *Important* ⚠️
                Verification code has been sent to the provided email address\\.
                Please paste the received code here in the chat\\.""";
        QuickSender.message(user.getChatId(), verificationMessage, true);
    }


    public String getPriceAlertFromText(String text) {
        double priceAlert;
        if (text.equalsIgnoreCase("AUTO"))
            return text.toUpperCase();

        try {
            if (text.contains("zł"))
                text = text.replaceFirst("zł", "");

            else if (text.contains("zl"))
                text = text.replaceFirst("zl", "");

            if (text.contains(","))
                text = text.replaceFirst(",", ".");

            priceAlert = Double.parseDouble(text);

            if (priceAlert < 0)
                throw new NumberFormatException();

            if (priceAlert == 0)
                return "0";

        } catch (NumberFormatException ex) {
            throw new IllegalInputException("Invalid price alert.", "User '%s' typed invalid message '%s'".formatted(ActiveUser.get().getUserId(), text));
        }

        return String.format("%.2f", priceAlert).replaceFirst(",", ".");
    }


    public void deletePreviousMessage() {
        QuickSender.deleteMessage(ActiveUser.get().getChatId(), ActiveUser.get().getMessageId());
    }

    @SneakyThrows
    public void deletePreviousMessage(long waitAfterDelete) {
        deletePreviousMessage();
        Thread.sleep(waitAfterDelete);
    }

    public void displayNotification(boolean deletePreviousMessage) {
        if (deletePreviousMessage)
            deletePreviousMessage();

        displayNotification();
    }

    public void displayNotification() {
        QuickSender.message(InlineBlock.getNotification(ActiveUser.get().isNotifyByEmail(), ActiveUser.get().isVerifiedEmail(), ActiveUser.get().getEmail()));
    }

    public void popupMessage(String message) {
        QuickSender.popupMessage(ActiveUser.get().getChatId(), message);
    }

    public void popupMessage(String message, boolean deletePreviousMessage) {
        if (deletePreviousMessage)
            deletePreviousMessage();

        popupMessage(message);
    }

    public void displayMenu() {
        QuickSender.message(InlineBlock.getMenu());
    }
}




















