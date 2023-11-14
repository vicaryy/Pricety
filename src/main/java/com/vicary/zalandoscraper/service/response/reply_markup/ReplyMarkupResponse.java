package com.vicary.zalandoscraper.service.response.reply_markup;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.api_telegram.api_object.Action;
import com.vicary.zalandoscraper.entity.AwaitedMessageEntity;
import com.vicary.zalandoscraper.entity.LinkRequestEntity;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.Scraper;
import com.vicary.zalandoscraper.service.entity.*;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.service.response.InlineBlock;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyMarkupResponse {

    private final Scraper scraper = Scraper.getInstance();

    private final LinkRequestService linkRequestService;

    private final ProductService productService;

    private final AwaitedMessageService awaitedMessageService;

    private final UserService userService;

    private final UpdatesHistoryService updatesHistoryService;


    public void response(ActiveUser user) {

        String text = user.getText();

        if (text.startsWith("-l "))
            addProduct(text);

        else if (text.equals("-allProducts"))
            displayAllProducts();

        else if (text.equals("-addProduct"))
            displayAddProduct();

        else if (text.equals("-editPriceAlert"))
            displayEditPriceAlert();

        else if (text.startsWith("-edit "))
            displayEditPriceAlertMessage(text);

        else if (text.equals("-deleteProduct"))
            displayDeleteProduct();

        else if (text.startsWith("-delete "))
            deleteProduct(text);

        else if (text.equals("-deleteAll"))
            displayDeleteYesOrNo(true);

        else if (text.equals("-deleteAllYes"))
            deleteAllProducts();

        else if (text.equals("-deleteAllNo"))
            backToMenu(true);

        else if (text.equals("-exit"))
            deletePreviousMessage(2000);

        else if (text.equals("-back"))
            backToMenu(true);

        else if (text.equals("-notification"))
            displayNotification(true);

        else if (text.equals("-enableEmail") || text.equals("-disableEmail"))
            updateNotifyByEmail(text);

        else if (text.equals("-setEmail"))
            displaySetEmailMessage(text);
    }

    @SneakyThrows
    public void displaySetEmailMessage(String text) {
        deletePreviousMessage(1500);

        var awaitedMessageEntity = AwaitedMessageEntity.builder()
                .userId(ActiveUser.get().getUserId())
                .request(text)
                .build();

        awaitedMessageService.saveAwaitedMessage(awaitedMessageEntity);


        String message = """
                Okay\\, send me a new email address\\.
                                
                Type *delete* if you want to delete your email address\\.""";

        QuickSender.message(ActiveUser.get().getUserId(), message, true);
    }


    @SneakyThrows
    public void updateNotifyByEmail(String text) {
        ActiveUser user = ActiveUser.get();

        deletePreviousMessage();

        if (user.getEmail() == null) {
            popupMessage("You have to set up an email address.");
            displayNotification();
            return;
        }

        if (!user.isVerifiedEmail()) {
            popupMessage("You have to verify your email.");
            displayNotification();
            return;
        }


        boolean notifyByEmail = text.equals("-enableEmail");
        userService.updateNotifyByEmailById(user.getUserId(), notifyByEmail);
        user.setNotifyByEmail(notifyByEmail);
        Thread.sleep(1000);
        displayNotification();
    }


    public void deleteAllProducts() {
        ActiveUser user = ActiveUser.get();

        productService.deleteAllProductsByUserId(user.getUserId());

        popupMessage("All products deleted successfully.", true);

        displayMenu();
    }


    public void deleteProduct(String text) {
        String[] textArray = text.split(" ");
        long productId = Long.parseLong(textArray[1]);

        productService.deleteProductById(productId);

        popupMessage("Product deleted successfully.", true);

        displayMenu();
    }


    public void displayDeleteProduct() {
        ActiveUser user = ActiveUser.get();
        List<ProductDTO> productDTOList = productService.getAllProductsDtoByUserId(user.getUserId());

        deletePreviousMessage();

        if (productDTOList.isEmpty()) {
            popupMessage("You don't have any products.");
            displayMenu();
            return;
        }

        ProductDisplay displayer = new ProductDisplay(productDTOList, Type.DELETE, user.getChatId());
        displayer.display();
    }


    public void displayEditPriceAlertMessage(String text) {
        var awaitedMessageEntity = AwaitedMessageEntity.builder()
                .userId(ActiveUser.get().getUserId())
                .request(text)
                .build();
        awaitedMessageService.saveAwaitedMessage(awaitedMessageEntity);

        deletePreviousMessage(1500);

        //                ‎s
        String message = """            
                *Send me a new price alert\\.*
                                
                Type *auto* for automatic notification\\.
                Type *off* if you don't want notification at all\\.""";

        QuickSender.message(ActiveUser.get().getUserId(), message, true);
    }

    @SneakyThrows
    public void displayEditPriceAlert() {
        ActiveUser user = ActiveUser.get();

        deletePreviousMessage();

        List<ProductDTO> productDTOList = productService.getAllProductsDtoByUserId(user.getUserId());

        if (productDTOList.isEmpty()) {
            popupMessage("You don't have any products.");
            displayMenu();
            return;
        }

        ProductDisplay displayer = new ProductDisplay(productDTOList, Type.EDIT, user.getChatId());
        displayer.display();
    }


    public void displayAddProduct() {
        popupMessage("Just paste Zalando URL down below 👇", 3000, true);
        displayMenu();
    }


    public void displayAllProducts() {
        ActiveUser user = ActiveUser.get();
        List<ProductDTO> productDTOList = productService.getAllProductsDtoByUserId(user.getUserId());

        deletePreviousMessage();

        if (productDTOList.isEmpty()) {
            popupMessage("You don't have any products.");
            displayMenu();
            return;
        }

        ProductDisplay displayer = new ProductDisplay(productDTOList, Type.ALL, user.getChatId());
        displayer.display();
    }

    public void addProduct(String text) {
        String chatId = ActiveUser.get().getChatId();
        String[] arrayText = text.split(" ");
        String requestId = arrayText[1];
        String link = getLink(requestId);
        StringBuilder variant = new StringBuilder();
        for (int i = 2; i < arrayText.length; i++)
            variant.append(arrayText[i]).append(" ");

        deletePreviousMessage();
        int messageId = QuickSender.messageWithReturn(chatId, "Adding product...", false).getMessageId();
        QuickSender.chatAction(chatId, Action.TYPING);

        Product product = scraper.getProduct(link, variant.toString().trim());

        QuickSender.deleteMessage(chatId, messageId);

        if (productService.existsByUserIdAndLinkAndVariant(chatId, product.getLink(), product.getVariant()))
            throw new InvalidLinkException("You already have this product in your watchlist.", "User try to add same product.");

        if (productService.countByUserId(ActiveUser.get().getUserId()) > 9)
            throw new InvalidLinkException("You cannot add more than 10 products.", "User try to add more than 10 products.");

        productService.saveProduct(product);
        QuickSender.messageWithReturn(ActiveUser.get().getChatId(), "Product added successfully.", false);
    }


    private String getLink(String requestId) {
        LinkRequestEntity linkRequest = linkRequestService.findByRequestIdAndDelete(requestId);
        if (linkRequest == null)
            throw new InvalidLinkException("This session expired.", "User '%s' session expired".formatted(ActiveUser.get().getUserId()));

        checkExpiration(linkRequest.getExpiration());
        return linkRequest.getLink();
    }

    private void checkExpiration(long expiration) {
        if (System.currentTimeMillis() > expiration)
            throw new InvalidLinkException("This session expired.", "User '%s' session expired".formatted(ActiveUser.get().getUserId()));
    }


    public void displayMenu() {
        QuickSender.message(InlineBlock.getMenu());
    }

    public void displayMenu(boolean deletePreviousMessage) {
        if (deletePreviousMessage)
            deletePreviousMessage();

        displayMenu();
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

    @SneakyThrows
    public void displayDeleteYesOrNo() {
        QuickSender.message(InlineBlock.getDeleteYesOrNo());
        Thread.sleep(1500);
    }

    public void displayDeleteYesOrNo(boolean deletePreviousMessage) {
        if (deletePreviousMessage)
            QuickSender.deleteMessage(ActiveUser.get().getChatId(), ActiveUser.get().getMessageId());

        displayDeleteYesOrNo();
    }

    @SneakyThrows
    public void backToMenu() {
        QuickSender.message(InlineBlock.getMenu());
        Thread.sleep(1500);
    }

    public void backToMenu(boolean deletePreviousMessage) {
        if (deletePreviousMessage)
            QuickSender.deleteMessage(ActiveUser.get().getChatId(), ActiveUser.get().getMessageId());

        backToMenu();
    }

    public void displayNoProducts() {
        QuickSender.popupMessage(ActiveUser.get().getChatId(), "You don't have any products.");
    }

    public void displayNoProducts(boolean deletePreviousMessage) {
        if (deletePreviousMessage)
            QuickSender.deleteMessage(ActiveUser.get().getChatId(), ActiveUser.get().getMessageId());

        displayNoProducts();
    }

    public void popupMessage(String message) {
        QuickSender.popupMessage(ActiveUser.get().getChatId(), message);
    }

    public void popupMessage(String message, boolean deletePreviousMessage) {
        if (deletePreviousMessage)
            deletePreviousMessage();

        popupMessage(message);
    }

    public void popupMessage(String message, long popupTime, boolean deletePreviousMessage) {
        if (deletePreviousMessage)
            deletePreviousMessage();

        popupMessage(message, popupTime);
    }

    public void popupMessage(String message, long popupTime) {
        QuickSender.popupMessage(ActiveUser.get().getChatId(), message, popupTime);
    }
}
