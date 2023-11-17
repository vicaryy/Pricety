package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.pattern.Pattern;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.service.entity.AwaitedMessageService;
import com.vicary.zalandoscraper.service.entity.EmailVerificationService;
import com.vicary.zalandoscraper.service.entity.ProductService;
import com.vicary.zalandoscraper.service.entity.UserService;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
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

    public void updateProductPriceAlert(String request) {
        ActiveUser user = ActiveUser.get();

        String[] requestArray = request.split(" ");

        Long productId = Long.parseLong(requestArray[1]);
        String priceAlert = getPriceAlertFromText(user.getText());

        ProductDTO dto = productService.getProductDTOById(productId);

        if (isPriceAlertHigherThanPrice(priceAlert, dto.getPrice()))
            throw new IllegalInputException(Messages.other("priceAlertHigher"), "User '%s' specify price alert higher than actual price".formatted(user.getUserId()));

        productService.updateProductPriceAlert(productId, priceAlert);

        popupMessage(Messages.other("priceAlertUpdated"));
        displayMenu();
    }

    public void updateUserEmail() {
        ActiveUser user = ActiveUser.get();
        String email = user.getText();

        if (!Pattern.isEmail(email))
            throw new IllegalInputException(Messages.other("invalidEmail"), "User '%s' typed invalid email '%s'".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

        if (email.equals(user.getEmail()))
            throw new IllegalInputException(Messages.other("differentEmail"), "User '%s' typed the same email '%s'".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

        if (email.equalsIgnoreCase("DELETE")) {
            userService.deleteEmailById(user.getUserId());
            popupMessage(Messages.other("emailDeleted"));
            displayMenu();
            return;
        }

        userService.updateEmailById(user.getUserId(), email);

        if (emailVerificationService.existsByUserId(user.getUserId()))
            emailVerificationService.deleteAllByUserId(user.getUserId());

        var verification = emailVerificationService.createVerification(user.getUserId());

        emailVerificationService.sendTokenToUser(verification, email);

        String verificationMessage = Messages.other("verificationCodeMessage");
        QuickSender.message(user.getChatId(), verificationMessage, true);
    }


    public String getPriceAlertFromText(String text) {
        double priceAlert;
        if (text.equalsIgnoreCase("AUTO") || text.equalsIgnoreCase("OFF"))
            return text.toUpperCase();

        try {
            if (text.contains("zł"))
                text = text.replaceFirst("zł", "");

            else if (text.contains("zl"))
                text = text.replaceFirst("zl", "");

            if (text.contains(","))
                text = text.replaceFirst(",", ".");

            priceAlert = Double.parseDouble(text);

            if (priceAlert <= 0)
                throw new NumberFormatException();

        } catch (NumberFormatException ex) {
            throw new IllegalInputException(Messages.other("invalidPriceAlert"), "User '%s' typed invalid message '%s'".formatted(ActiveUser.get().getUserId(), text));
        }

        return String.format("%.2f", priceAlert).replaceFirst(",", ".");
    }

    public boolean isPriceAlertHigherThanPrice(String priceAlert, double price) {
        if (priceAlert.equalsIgnoreCase("AUTO") || priceAlert.equalsIgnoreCase("OFF") || price == 0)
            return false;


        return Double.parseDouble(priceAlert.replaceFirst(" zł", "")) >= price;
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




















