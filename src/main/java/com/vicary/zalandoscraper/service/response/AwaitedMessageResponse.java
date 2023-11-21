package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.pattern.Pattern;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;

public class AwaitedMessageResponse implements Responser {
    private final ResponseFacade responseFacade;
    private final ActiveUser user;

    public AwaitedMessageResponse(ResponseFacade responseFacade, ActiveUser user) {
        this.responseFacade = responseFacade;
        this.user = user;
    }

    @Override
    public void response() {
        String request = responseFacade.getAwaitedMessageRequestByUserIdAndDelete(user.getUserId());

        if (request.startsWith("-edit"))
            updateProductPriceAlert(request);

        else if (request.startsWith("-setEmail"))
            updateUserEmail();
    }

    public void updateProductPriceAlert(String request) {
        Long productId = Long.parseLong(request.split(" ")[1]);
        String priceAlert = getPriceAlertFromText(user.getText());

        ProductDTO dto = responseFacade.getProductDTOById(productId);

        if (isPriceAlertHigherThanPrice(priceAlert, dto.getPrice()))
            throw new IllegalInputException(Messages.other("priceAlertHigher"), "User '%s' specify price alert higher than actual price".formatted(user.getUserId()));

        responseFacade.updateProductPriceAlert(productId, priceAlert);

        popupMessage(Messages.other("priceAlertUpdated"));
        displayMenu();
    }

    public void updateUserEmail() {
        String email = user.getText();

        if (!Pattern.isEmail(email))
            throw new IllegalInputException(Messages.other("invalidEmail"), "User '%s' typed invalid email '%s'".formatted(user.getUserId(), user.getText()));

        if (email.equals(user.getEmail()))
            throw new IllegalInputException(Messages.other("differentEmail"), "User '%s' typed the same email '%s'".formatted(user.getUserId(), user.getText()));

        if (email.equalsIgnoreCase("DELETE")) {
            responseFacade.deleteEmailById(user.getUserId());
            popupMessage(Messages.other("emailDeleted"));
            displayMenu();
            return;
        }

        responseFacade.updateEmailAndSendToken(user.getUserId(), email);

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
            throw new IllegalInputException(Messages.other("invalidPriceAlert"), "User '%s' typed invalid message '%s'".formatted(user.getUserId(), text));
        }

        return String.format("%.2f", priceAlert).replaceFirst(",", ".");
    }

    public boolean isPriceAlertHigherThanPrice(String priceAlert, double price) {
        if (priceAlert.equalsIgnoreCase("AUTO") || priceAlert.equalsIgnoreCase("OFF") || price == 0)
            return false;


        return Double.parseDouble(priceAlert.replaceFirst(" zł", "")) >= price;
    }

    public void popupMessage(String message) {
        QuickSender.popupMessage(user.getChatId(), message);
    }

    public void displayMenu() {
        QuickSender.message(InlineBlock.getMenu());
    }
}




















