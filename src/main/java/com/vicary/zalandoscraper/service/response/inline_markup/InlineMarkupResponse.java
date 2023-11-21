package com.vicary.zalandoscraper.service.response.inline_markup;

import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.pattern.Pattern;
import com.vicary.zalandoscraper.scraper.HebeScraper;
import com.vicary.zalandoscraper.scraper.Scraper;
import com.vicary.zalandoscraper.service.response.ResponseFacade;
import com.vicary.zalandoscraper.service.response.Responser;
import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import com.vicary.zalandoscraper.api_telegram.api_object.Action;
import com.vicary.zalandoscraper.entity.AwaitedMessageEntity;
import com.vicary.zalandoscraper.entity.LinkRequestEntity;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.scraper.ZalandoScraper;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.service.response.InlineBlock;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class InlineMarkupResponse implements Responser {
    private final static Logger logger = LoggerFactory.getLogger(InlineMarkupResponse.class);
    private final ResponseFacade responseFacade;
    private final ActiveUser user;

    public InlineMarkupResponse(ResponseFacade responseFacade, ActiveUser user) {
        this.responseFacade = responseFacade;
        this.user = user;
    }

    @Override
    public void response() {
        String text = user.getText();

        if (text.startsWith("-l "))
            addProduct();

        else if (text.equals("-allProducts"))
            displayAllProducts();

        else if (text.equals("-addProduct"))
            displayAddProduct();

        else if (text.equals("-editPriceAlert"))
            displayEditPriceAlert();

        else if (text.equals("-notification"))
            displayNotification(true);

        else if (text.equals("-deleteProduct"))
            displayDeleteProduct();

        else if (text.startsWith("-edit "))
            displayEditPriceAlertMessage();

        else if (text.startsWith("-delete "))
            deleteProduct();

        else if (text.equals("-deleteAll"))
            displayDeleteYesOrNo(true);

        else if (text.equals("-deleteAllYes"))
            deleteAllProducts();

        else if (text.equals("-deleteAllNo"))
            backToMenu(true);

        else if (text.equals("-enableEmail") || text.equals("-disableEmail"))
            updateNotifyByEmail();

        else if (text.equals("-setEmail"))
            displaySetEmailMessage();

        else if (text.startsWith("-lang"))
            updateUserLanguage();

        else if (text.equals("-exit"))
            deletePreviousMessage(2000);

        else if (text.equals("-back"))
            backToMenu(true);
    }

    public void addProduct() {
        String[] arrayText = user.getText().split(" ");
        String requestId = arrayText[1];
        String link = getLink(requestId);
        StringBuilder variant = new StringBuilder();
        for (int i = 2; i < arrayText.length; i++)
            variant.append(arrayText[i]).append(" ");

        deletePreviousMessage();
        int messageId = QuickSender.messageWithReturn(user.getChatId(), Messages.other("adding"), false).getMessageId();
        QuickSender.chatAction(user.getChatId(), Action.TYPING);

        Scraper scraper = null;

        if (Pattern.isZalandoURL(link))
            scraper = new ZalandoScraper();
        else if (Pattern.isHebeURL(link))
            scraper = new HebeScraper();

        Product product = scraper.getProduct(link, variant.toString().trim());

        QuickSender.deleteMessage(user.getChatId(), messageId);

        checkProductValidation(product);

        responseFacade.saveProduct(product);
        QuickSender.message(user.getChatId(), Messages.other("productAdded"), false);
    }

    public void displayAllProducts() {
        ActiveUser user = ActiveUser.get();
        List<ProductDTO> productDTOList = responseFacade.getAllProductsByUserId(user.getUserId());

        deletePreviousMessage();

        if (productDTOList.isEmpty()) {
            popupMessage(Messages.other("dontHaveProduct"));
            displayMenu();
            return;
        }

        ProductDisplayer displayer = new AllProductDisplay(productDTOList, user.getChatId());
        displayer.display();
    }

    public void displayAddProduct() {
        popupMessage(Messages.addProduct("justPaste"), 3000, true);
        displayMenu();
    }

    public void displayEditPriceAlert() {
        deletePreviousMessage();

        List<ProductDTO> productDTOList = responseFacade.getAllProductsByUserId(user.getUserId());

        if (productDTOList.isEmpty()) {
            popupMessage(Messages.other("dontHaveProduct"));
            displayMenu();
            return;
        }

        ProductDisplayer displayer = new EditProductDisplay(productDTOList, user.getChatId());
        displayer.display();
    }

    public void displayEditPriceAlertMessage() {
        var awaitedMessageEntity = AwaitedMessageEntity.builder()
                .userId(user.getUserId())
                .request(user.getText())
                .build();
        responseFacade.saveAwaitedMessage(awaitedMessageEntity);

        deletePreviousMessage(1500);

        String message = Messages.other("sendNewAlert");

        QuickSender.message(user.getChatId(), message, true);
    }

    public void displayDeleteProduct() {
        List<ProductDTO> productDTOList = responseFacade.getAllProductsByUserId(user.getUserId());

        deletePreviousMessage();

        if (productDTOList.isEmpty()) {
            popupMessage(Messages.other("dontHaveProduct"));
            displayMenu();
            return;
        }

        ProductDisplayer displayer = new DeleteProductDisplay(productDTOList, user.getChatId());
        displayer.display();
    }

    public void deleteProduct() {
        long productId = Long.parseLong(user.getText().split(" ")[1]);

        responseFacade.deleteProductById(productId);

        popupMessage(Messages.other("deleted"), true);

        displayMenu();
    }

    private void displayDeleteYesOrNo(boolean deletePreviousMessage) {
        if (deletePreviousMessage)
            deletePreviousMessage();

        displayDeleteYesOrNo();
    }

    public void deleteAllProducts() {
        responseFacade.deleteAllProductsByUserId(user.getUserId());

        popupMessage(Messages.other("allDeleted"), true);

        displayMenu();
    }

    private void backToMenu(boolean deletePreviousMessage) {
        if (deletePreviousMessage)
            QuickSender.deleteMessage(user.getChatId(), user.getMessageId());

        backToMenu();
    }

    private void updateUserLanguage() {
        deletePreviousMessage();

        String language = user.getText().split(" ")[1];

        responseFacade.updateLanguageByUserId(user.getUserId(), language);
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages", Locale.of(language)));

        displayMenu();
    }

    public void displaySetEmailMessage() {
        deletePreviousMessage(1500);

        var awaitedMessageEntity = AwaitedMessageEntity.builder()
                .userId(ActiveUser.get().getUserId())
                .request(user.getText())
                .build();

        responseFacade.saveAwaitedMessage(awaitedMessageEntity);

        String message = Messages.other("sendNewEmail");

        QuickSender.message(user.getChatId(), message, true);
    }


    @SneakyThrows
    public void updateNotifyByEmail() {
        deletePreviousMessage();

        if (user.getEmail() == null) {
            popupMessage(Messages.other("setEmailFirst"));
            displayNotification();
            return;
        }

        if (!user.isVerifiedEmail()) {
            popupMessage(Messages.other("verifyEmailFirst"));
            displayNotification();
            return;
        }


        boolean notifyByEmail = user.getText().equals("-enableEmail");
        responseFacade.updateNotifyByEmailById(user.getUserId(), notifyByEmail);
        user.setNotifyByEmail(notifyByEmail);
        Thread.sleep(1000);
        displayNotification();
    }


    private void checkProductValidation(Product product) {
        if (responseFacade.productExistsByUserIdAndLinkAndVariant(user.getChatId(), product.getLink(), product.getVariant()))
            throw new InvalidLinkException(Messages.other("alreadyHave"), "User try to add same product.");

        if (responseFacade.countProductsByUserId(user.getUserId()) > 9)
            throw new InvalidLinkException(Messages.other("productLimit"), "User try to add more than 10 products.");
    }


    private String getLink(String requestId) {
        LinkRequestEntity linkRequest = responseFacade.getLinkRequestByIdAndDelete(requestId);

        checkExpiration(linkRequest.getExpiration());
        return linkRequest.getLink();
    }

    private void checkExpiration(long expiration) {
        if (System.currentTimeMillis() > expiration)
            throw new InvalidLinkException(Messages.other("sessionExpired"), "User '%s' session expired".formatted(user.getUserId()));
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
        QuickSender.deleteMessage(user.getChatId(), user.getMessageId());
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
        QuickSender.message(InlineBlock.getNotification(user.isNotifyByEmail(), user.isVerifiedEmail(), user.getEmail()));
    }

    @SneakyThrows
    public void displayDeleteYesOrNo() {
        QuickSender.message(InlineBlock.getDeleteYesOrNo());
        Thread.sleep(1500);
    }

    @SneakyThrows
    private void backToMenu() {
        QuickSender.message(InlineBlock.getMenu());
        Thread.sleep(1500);
    }

    private void displayNoProducts() {
        QuickSender.popupMessage(user.getChatId(), "You don't have any products.");
    }

    public void displayNoProducts(boolean deletePreviousMessage) {
        if (deletePreviousMessage)
            QuickSender.deleteMessage(user.getChatId(), user.getMessageId());

        displayNoProducts();
    }

    public void popupMessage(String message) {
        QuickSender.popupMessage(user.getChatId(), message);
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
        QuickSender.popupMessage(user.getChatId(), message, popupTime);
    }
}
