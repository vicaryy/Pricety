package com.vicary.pricety.service.response;

import com.vicary.pricety.messages.Messages;
import com.vicary.pricety.model.Product;
import com.vicary.pricety.service.response.inline_markup.InlineKeyboardMarkupFactory;
import com.vicary.pricety.thread_local.ActiveUser;
import com.vicary.pricety.exception.IllegalInputException;
import com.vicary.pricety.pattern.Pattern;
import com.vicary.pricety.api_telegram.service.QuickSender;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AwaitedMessageResponse implements Responser {
    private final ResponseFacade responseFacade;
    private final ActiveUser user;
    private final QuickSender quickSender;


    public AwaitedMessageResponse(ResponseFacade responseFacade, ActiveUser user) {
        this.responseFacade = responseFacade;
        this.user = user;
        this.quickSender = new QuickSender();
    }

    public AwaitedMessageResponse(ResponseFacade responseFacade, ActiveUser user, QuickSender quickSender) {
        this.responseFacade = responseFacade;
        this.user = user;
        this.quickSender = quickSender;
    }

    @Override
    public void response() {
        String request = responseFacade.getAwaitedMessageRequest(user.getTelegramId());

        if (request.startsWith("-edit"))
            updateProductPriceAlert(request);

        else if (request.startsWith("-setEmail"))
            updateUserEmail();

        else if (request.startsWith("-setNick"))
            setNick();
    }

    private void setNick() {
        String nick = user.getText();

        responseFacade.updateUserNick(user.getUserId(), nick);
        responseFacade.deleteAwaitedMessage(user.getTelegramId());
        quickSender.popupMessage(user.getChatId(), Messages.other("setNickSuccess"));
        quickSender.message(user.getChatId(), Messages.command("start").formatted(" " + user.getText()), true);
    }

    private void updateProductPriceAlert(String request) {
        responseFacade.deleteAwaitedMessage(user.getTelegramId());
        Long productId = Long.parseLong(request.split(" ")[1]);
        String priceAlert = getPriceAlertFromText(user.getText());

        Product product = responseFacade.getProductById(productId);

        if (isPriceAlertHigherThanPrice(priceAlert, product.getPrice()))
            throw new IllegalInputException(Messages.other("priceAlertHigher"), "User '%s' specify price alert higher than actual price".formatted(user.getTelegramId()));

        log.info("Product id {}", productId);
        log.info("Price Alert {}", priceAlert);
        responseFacade.updateProductPriceAlert(productId, priceAlert);

        popupMessage(Messages.other("priceAlertUpdated"));
        displayMenu();
    }

    private void updateUserEmail() {
        responseFacade.deleteAwaitedMessage(user.getTelegramId());
        String email = user.getText();

        if (!Pattern.isEmail(email))
            throw new IllegalInputException(Messages.other("invalidEmail"), "User '%s' typed invalid email '%s'".formatted(user.getTelegramId(), user.getText()));

        if (email.equals(user.getEmail()))
            throw new IllegalInputException(Messages.other("differentEmail"), "User '%s' typed the same email '%s'".formatted(user.getTelegramId(), user.getText()));

        if (email.equalsIgnoreCase("DELETE")) {
            responseFacade.deleteEmailById(user.getUserId());
            popupMessage(Messages.other("emailDeleted"));
            displayMenu();
            return;
        }

        responseFacade.updateEmailAndSendToken(user.getUserId(), email);

        quickSender.message(user.getChatId(), Messages.other("verificationCodeMessage"), true);
    }


    String getPriceAlertFromText(String text) {
        double priceAlert;
        if (text.equalsIgnoreCase("AUTO") || text.equalsIgnoreCase("OFF"))
            return text.toUpperCase();

        try {
            if (text.length() > 20)
                throw new NumberFormatException();

            StringBuilder sb = new StringBuilder();
            for (char ch : text.toCharArray())
                if (Character.isDigit(ch) || ch == '.' || ch == ',' || ch == '-')
                    sb.append(ch);

            String price = sb.toString();

            if (price.contains(","))
                price = price.replaceFirst(",", ".");

            priceAlert = Double.parseDouble(price);

            if (priceAlert <= 0)
                throw new NumberFormatException();

        } catch (NumberFormatException ex) {
            throw new IllegalInputException(Messages.other("invalidPriceAlert"), "User '%s' typed invalid message '%s'".formatted(user.getTelegramId(), text));
        }

        return String.format("%.2f", priceAlert).replaceFirst(",", ".");
    }

    boolean isPriceAlertHigherThanPrice(String priceAlert, double price) {
        if (priceAlert.equalsIgnoreCase("AUTO") || priceAlert.equalsIgnoreCase("OFF") || price == 0)
            return false;

        return Double.parseDouble(priceAlert) >= price;
    }

    private void popupMessage(String message) {
        quickSender.popupMessage(user.getChatId(), message);
    }

    private void displayMenu() {
        quickSender.inlineMarkup(
                user.getChatId(),
                Messages.menu("welcome"),
                InlineKeyboardMarkupFactory.getMenu(),
                true);
    }
}




















