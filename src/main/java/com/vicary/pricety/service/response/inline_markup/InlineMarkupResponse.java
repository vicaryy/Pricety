package com.vicary.pricety.service.response.inline_markup;

import com.vicary.pricety.api_telegram.service.QuickSender;
import com.vicary.pricety.exception.ChartGeneratorException;
import com.vicary.pricety.format.MarkdownV2;
import com.vicary.pricety.messages.Messages;
import com.vicary.pricety.scraper.Scraper;
import com.vicary.pricety.scraper.ScraperFactory;
import com.vicary.pricety.service.chart.ChartGeneratorFactory;
import com.vicary.pricety.service.chart.ProductChartGenerator;
import com.vicary.pricety.service.dto.ProductHistoryDTO;
import com.vicary.pricety.service.response.ResponseFacade;
import com.vicary.pricety.service.response.Responser;
import com.vicary.pricety.thread_local.ActiveLanguage;
import com.vicary.pricety.thread_local.ActiveUser;
import com.vicary.pricety.api_telegram.api_object.Action;
import com.vicary.pricety.entity.LinkRequestEntity;
import com.vicary.pricety.exception.InvalidLinkException;
import com.vicary.pricety.model.Product;
import com.vicary.pricety.utils.TerminalExecutor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
public class InlineMarkupResponse implements Responser {
    private final ResponseFacade responseFacade;
    private final ActiveUser user;
    private final QuickSender quickSender;

    public InlineMarkupResponse(ResponseFacade responseFacade, ActiveUser user) {
        this.responseFacade = responseFacade;
        this.user = user;
        this.quickSender = new QuickSender();
    }

    public InlineMarkupResponse(ResponseFacade responseFacade, ActiveUser user, QuickSender quickSender) {
        this.responseFacade = responseFacade;
        this.user = user;
        this.quickSender = quickSender;
    }

    @Override
    public void response() {
        String text = user.getText();

        if (text.startsWith("-l ")) {
            String link = getLinkFromText();
            String variant = getVariantFromText();
            Scraper scraper = ScraperFactory.getScraperFromLink(link).orElseThrow();
            addProduct(link, variant, scraper);
        } else if (text.equals("-addProduct"))
            displayAddProduct();

        else if (text.equals("-allProducts"))
            displayProducts(new AllProductDisplay());

        else if (text.equals("-editPriceAlert"))
            displayProducts(new EditProductDisplay());

        else if (text.equals("-deleteProduct"))
            displayProducts(new DeleteProductDisplay());

        else if (text.equals("-generateProduct"))
            displayProducts(new GenerateProductDisplay());

        else if (text.equals("-notification"))
            displayNotification(true);

        else if (text.startsWith("-edit "))
            displayEditPriceAlertMessage();

        else if (text.startsWith("-delete "))
            deleteProduct();

        else if (text.startsWith("-generate "))
            generateProduct(ChartGeneratorFactory.getDefaultChartGenerator());

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

    void addProduct(String link, String variant, Scraper scraper) {
        deletePreviousMessage();
        int messageId = quickSender.messageWithReturn(user.getChatId(), Messages.other("adding"), false).getMessageId();
        quickSender.chatAction(user.getChatId(), Action.TYPING);

        Product product = scraper.getProduct(link, variant);

        quickSender.deleteMessage(user.getChatId(), messageId);

        checkIfUserHaveProduct(product);

        responseFacade.saveProduct(product, user.getUserId());
        quickSender.message(user.getChatId(), Messages.other("productAdded"), false);
    }

    private String getLinkFromText() {
        String[] arrayText = user.getText().split(" ");
        String requestId = arrayText[1];

        LinkRequestEntity linkRequest = responseFacade.getLinkRequestByIdAndDelete(requestId);

        return linkRequest.getLink();
    }

    private String getVariantFromText() {
        String[] arrayText = user.getText().split(" ");
        StringBuilder variant = new StringBuilder();
        for (int i = 2; i < arrayText.length; i++)
            variant.append(arrayText[i]).append(" ");

        return variant.toString().trim();
    }

    void displayProducts(ProductDisplayer displayer) {
        List<Product> products = responseFacade.getAllProductsByUserId(user.getUserId());

        deletePreviousMessage();

        if (products.isEmpty()) {
            popupMessage(Messages.other("dontHaveProduct"));
            displayMenu();
            return;
        }

        displayer.setProductDTOList(products);
        displayer.setChatId(user.getChatId());
        displayer.display();
    }

    void displayAddProduct() {
        popupMessage(Messages.addProduct("justPaste"), 3000, true);
        displayMenu();
    }

    void displayEditPriceAlertMessage() {
        responseFacade.createAndSaveAwaitedMessage(user.getTelegramId(), user.getText());

        deletePreviousMessage(1500);

        quickSender.message(user.getChatId(), Messages.other("sendNewAlert"), true);
    }

    void deleteProduct() {
        long productId = Long.parseLong(user.getText().split(" ")[1]);

        responseFacade.deleteProduct(productId);

        popupMessage(Messages.other("deleted"), true);

        displayMenu();
    }

    void generateProduct(ProductChartGenerator generator) {
        long productId = Long.parseLong(user.getText().split(" ")[1]);

        List<ProductHistoryDTO> productHistoryDTOS = responseFacade.getAllReducedProductHistory(productId);
        Product product = responseFacade.getProductById(productId);

        deletePreviousMessage();
        int messageId = quickSender.messageWithReturn(user.getChatId(), MarkdownV2.applyWithManualBoldAndItalic(Messages.generateProduct("generating")), true).getMessageId();
        quickSender.chatAction(user.getChatId(), Action.UPLOAD_PHOTO);

        File generatedChart;
        try {
            generatedChart = generator.asPngHighResolution(product, productHistoryDTOS);
        } catch (ChartGeneratorException ex) {
            quickSender.message(user.getChatId(), ex.getMessage(), true);
            quickSender.deleteMessage(user.getChatId(), messageId);
            throw new IllegalArgumentException(ex.getLoggerMessage());
        }
        quickSender.photo(user.getChatId(), generatedChart);
        quickSender.deleteMessage(user.getChatId(), messageId);
        TerminalExecutor.deleteFile(generatedChart);
        quickSender.inlineMarkup(user.getChatId(), MarkdownV2.applyWithManualBoldAndItalic(Messages.generateProduct("generated")), InlineKeyboardMarkupFactory.getBack(), true);
    }

    private void displayDeleteYesOrNo(boolean deletePreviousMessage) {
        if (deletePreviousMessage)
            deletePreviousMessage();

        displayDeleteYesOrNo();
    }

    private void deleteAllProducts() {
        responseFacade.deleteAllProductsByUserId(user.getUserId());

        popupMessage(Messages.other("allDeleted"), true);

        displayMenu();
    }

    private void backToMenu(boolean deletePreviousMessage) {
        if (deletePreviousMessage)
            quickSender.deleteMessage(user.getChatId(), user.getMessageId());

        backToMenu();
    }

    private void updateUserLanguage() {
        deletePreviousMessage();

        String language = user.getText().split(" ")[1];

        responseFacade.updateUserLanguage(user.getUserId(), language);
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages", Locale.of(language)));

        displayMenu();
    }

    private void displaySetEmailMessage() {
        deletePreviousMessage(1500);

        responseFacade.createAndSaveAwaitedMessage(user.getTelegramId(), user.getText());

        quickSender.message(user.getChatId(), Messages.other("sendNewEmail"), true);
    }


    @SneakyThrows
    private void updateNotifyByEmail() {
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
        responseFacade.updateNotifyByEmailByUserId(user.getUserId(), notifyByEmail);
        user.setNotifyByEmail(notifyByEmail);
        Thread.sleep(1000);
        displayNotification();
    }


    private void checkIfUserHaveProduct(Product product) {
        if (responseFacade.productExistsByUserIdAndLinkAndVariant(user.getUserId(), product.getLink(), product.getVariant()))
            throw new InvalidLinkException(Messages.other("alreadyHave"), "User try to add same product.");
    }


    private void displayMenu() {
        quickSender.inlineMarkup(
                user.getChatId(),
                Messages.menu("welcome"),
                InlineKeyboardMarkupFactory.getMenu(),
                true);
    }

    private void displayMenu(boolean deletePreviousMessage) {
        if (deletePreviousMessage)
            deletePreviousMessage();

        displayMenu();
    }

    private void deletePreviousMessage() {
        quickSender.deleteMessage(user.getChatId(), user.getMessageId());
    }

    @SneakyThrows
    private void deletePreviousMessage(long waitAfterDelete) {
        deletePreviousMessage();
        Thread.sleep(waitAfterDelete);
    }

    private void displayNotification(boolean deletePreviousMessage) {
        if (deletePreviousMessage)
            deletePreviousMessage();

        displayNotification();
    }

    void displayNotification() {
        quickSender.inlineMarkup(
                user.getChatId(),
                Messages.notifications(user),
                InlineKeyboardMarkupFactory.getNotification(user),
                true);
    }

    @SneakyThrows
    private void displayDeleteYesOrNo() {
        quickSender.inlineMarkup(
                user.getChatId(),
                Messages.deleteProduct("areYouSure"),
                InlineKeyboardMarkupFactory.getDeleteYesOrNo());
        Thread.sleep(1500);
    }

    @SneakyThrows
    private void backToMenu() {
        displayMenu();
        Thread.sleep(1500);
    }

    private void displayNoProducts() {
        quickSender.popupMessage(user.getChatId(), "You don't have any products.");
    }

    private void displayNoProducts(boolean deletePreviousMessage) {
        if (deletePreviousMessage)
            quickSender.deleteMessage(user.getChatId(), user.getMessageId());

        displayNoProducts();
    }

    private void popupMessage(String message) {
        quickSender.popupMessage(user.getChatId(), message);
    }

    private void popupMessage(String message, boolean deletePreviousMessage) {
        if (deletePreviousMessage)
            deletePreviousMessage();

        popupMessage(message);
    }

    private void popupMessage(String message, long popupTime, boolean deletePreviousMessage) {
        if (deletePreviousMessage)
            deletePreviousMessage();

        popupMessage(message, popupTime);
    }

    private void popupMessage(String message, long popupTime) {
        quickSender.popupMessage(user.getChatId(), message, popupTime);
    }
}
