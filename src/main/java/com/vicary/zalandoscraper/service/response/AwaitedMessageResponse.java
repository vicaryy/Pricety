package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.pattern.Pattern;
import com.vicary.zalandoscraper.service.entity.AwaitedMessageService;
import com.vicary.zalandoscraper.service.entity.ProductService;
import com.vicary.zalandoscraper.service.entity.UserService;
import com.vicary.zalandoscraper.service.quick_sender.QuickSender;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AwaitedMessageResponse {

    private final AwaitedMessageService awaitedMessageService;

    private final ProductService productService;

    private final QuickSender quickSender;

    private final UserService userService;


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

        int messageId = quickSender.messageWithReturn(user.getUserId(), "Price Alert updated successfully.", false).getMessageId();

        Thread.sleep(2000);
        quickSender.deleteMessage(user.getChatId(), messageId);
        quickSender.message(InlineBlock.getMenu());
    }

    @SneakyThrows
    public void updateUserEmail() {
        ActiveUser user = ActiveUser.get();

        if (user.getText().equalsIgnoreCase("DELETE")) {
            user.setText(null);
            userService.updateNotifyByEmailById(user.getUserId(), false);
        } else if (!Pattern.isEmailAddressValid(user.getText()))
            throw new IllegalInputException("Invalid email.", "User '%s' typed invalid email '%s'".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

        userService.updateEmailById(user.getUserId(), user.getText());

        int messageId = quickSender.messageWithReturn(ActiveUser.get().getChatId(), "Email updated successfully.", false).getMessageId();

        Thread.sleep(2000);
        quickSender.deleteMessage(user.getChatId(), messageId);
        quickSender.message(InlineBlock.getMenu());
    }


    public String getPriceAlertFromText(String text) {
        double priceAlert = 0;
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
}




















