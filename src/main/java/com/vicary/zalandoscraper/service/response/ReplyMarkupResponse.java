package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.PrettyTime;
import com.vicary.zalandoscraper.api_telegram.api_object.Action;
import com.vicary.zalandoscraper.api_telegram.api_object.ParseMode;
import com.vicary.zalandoscraper.api_telegram.api_request.send.SendMessage;
import com.vicary.zalandoscraper.entity.AwaitedMessageEntity;
import com.vicary.zalandoscraper.entity.LinkRequestEntity;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.format.MarkdownV2;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.Scraper;
import com.vicary.zalandoscraper.service.entity.*;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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


    public void response(String text) {
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
            exit();

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

        if (!user.isVerifiedEmail()) {
            popupMessage("You have to verify your email.");
            displayNotification();
            return;
        }

        if (user.getEmail() == null) {
            popupMessage("You have to set up an email address.");
            displayNotification();
            return;
        }

        boolean notifyByEmail = text.equals("-enableEmail");
        userService.updateNotifyByEmailById(user.getUserId(), notifyByEmail);
        user.setNotifyByEmail(notifyByEmail);
        Thread.sleep(1000);
        displayNotification();
    }


    @SneakyThrows
    public void deleteAllProducts() {
        ActiveUser user = ActiveUser.get();

        productService.deleteAllProductsByUserId(user.getUserId());

        popupMessage("All products deleted successfully.", true);

        displayMenu();
    }


    @SneakyThrows
    public void deleteProduct(String text) {
        String[] textArray = text.split(" ");
        long productId = Long.parseLong(textArray[1]);

        productService.deleteProductById(productId);

        popupMessage("Product deleted successfully.", true);

        displayMenu();
    }


    @SneakyThrows
    public void displayDeleteProduct() {
        ActiveUser user = ActiveUser.get();
        List<ProductDTO> productDTOList = productService.getAllProductsDtoByUserId(user.getUserId());

        deletePreviousMessage();

        if (productDTOList.isEmpty()) {
            popupMessage("You don't have any products.");
            displayMenu();
            return;
        }


        List<StringBuilder> stringBuilders = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < productDTOList.size(); i++) {
            ProductDTO dto = productDTOList.get(i);

            if (productDTOList.size() == 1)
                sb.append(MarkdownV2.apply("Product to delete ❌\n\n").toBold().get());

            else if (i == 0)
                sb.append(MarkdownV2.apply("Products to delete ❌\n\n").toBold().get());

            String price = dto.getPrice() == 0 ? "Sold Out" : String.format("%.2f zł", dto.getPrice());
            String variant = dto.getVariant();
            if (variant.startsWith("-oneVariant "))
                variant = variant.substring(12);

            sb.append("""     
                    *Product nr %d*
                                        
                    *Name:* %s
                    *Description:* %s
                    *Variant:* %s
                    *Price:* %s"""
                    .formatted(i + 1,
                            MarkdownV2.apply(dto.getName()).get(),
                            MarkdownV2.apply(dto.getDescription()).get(),
                            MarkdownV2.apply(variant).get(),
                            MarkdownV2.apply(price).get()));

            if (i != productDTOList.size() - 1)
                sb.append("\n\n\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n\n");

            if (i % 14 == 0 && i != 0) {
                stringBuilders.add(new StringBuilder(sb.toString()));
                sb.setLength(0);
            }
        }
        stringBuilders.add(new StringBuilder(sb));

        if (stringBuilders.size() == 1) {
            sb.append("\u200E \n\n\n*Please select the item you want to delete*\\.");
            QuickSender.message(SendMessage.builder()
                    .chatId(user.getChatId())
                    .text(sb.toString())
                    .disableWebPagePreview(true)
                    .replyMarkup(InlineBlock.getProductChoice(productDTOList, "-delete"))
                    .parseMode(ParseMode.MarkdownV2)
                    .build());
            return;
        }

        for (StringBuilder s : stringBuilders)
            QuickSender.message(user.getChatId(), s.toString(), true);


        QuickSender.message(SendMessage.builder()
                .chatId(user.getChatId())
                .text("\u200E \n*Please select the item you want to delete*\\.")
                .disableWebPagePreview(true)
                .parseMode(ParseMode.MarkdownV2)
                .replyMarkup(InlineBlock.getProductChoice(productDTOList, "-delete"))
                .build());
    }


    @SneakyThrows
    public void displayEditPriceAlertMessage(String text) {
        var awaitedMessageEntity = AwaitedMessageEntity.builder()
                .userId(ActiveUser.get().getUserId())
                .request(text)
                .build();
        awaitedMessageService.saveAwaitedMessage(awaitedMessageEntity);

        deletePreviousMessage(1500);

        String[] textArray = text.split(" ");
        Long productId = Long.valueOf(textArray[1]);

        ProductDTO dto = productService.getProductDTOById(productId);

        String price = dto.getPrice() == 0 ? "Sold Out" : String.format("%.2f zł", dto.getPrice()).replaceFirst(",", ".");
        String priceAlert = dto.getPriceAlert().equals("0") ? "OFF" : dto.getPriceAlert().equals("AUTO") ? "AUTO" : dto.getPriceAlert() + " zł";
        String variant = dto.getVariant();
        if (variant.startsWith("-oneVariant "))
            variant = variant.substring(12);

        String message = """
                ‎\s
                                
                               
                               
                               
                               
                               
                                
                *Product to edit ⚙️*
                                
                *Name:* %s
                *Description:* %s
                *Variant:* %s
                *Price:* %s
                *Price Alert:* %s
                                
                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-
                                
                *Send me a new price alert\\.*
                For example: 100\\.50zł
                                
                Type *auto* for automatic notification\\.
                Type *0* if you don't want notification at all\\."""
                .formatted(
                        MarkdownV2.apply(dto.getName()).get(),
                        MarkdownV2.apply(dto.getDescription()).get(),
                        MarkdownV2.apply(variant).get(),
                        MarkdownV2.apply(price).get(),
                        MarkdownV2.apply(priceAlert).get());

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


        List<StringBuilder> stringBuilders = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < productDTOList.size(); i++) {
            ProductDTO dto = productDTOList.get(i);

            if (productDTOList.size() == 1)
                sb.append("*Product to edit* ⚙️\n\n");

            else if (i == 0)
                sb.append("*Products to edit* ⚙️\n\n");

            String price = dto.getPrice() == 0 ? "Sold Out" : String.format("%.2f zł", dto.getPrice()).replaceFirst(",", ".");
            String priceAlert = dto.getPriceAlert().equals("0") ? "OFF" : dto.getPriceAlert().equals("AUTO") ? "AUTO" : dto.getPriceAlert() + " zł";
            String variant = dto.getVariant();
            if (variant.startsWith("-oneVariant "))
                variant = variant.substring(12);

            sb.append("""     
                    *Product nr %d*
                                        
                    *Name:* %s
                    *Description:* %s
                    *Variant:* %s
                    *Price:* %s
                    *Price alert:* %s"""
                    .formatted(i + 1,
                            MarkdownV2.apply(dto.getName()).get(),
                            MarkdownV2.apply(dto.getDescription()).get(),
                            MarkdownV2.apply(variant).get(),
                            MarkdownV2.apply(price).get(),
                            MarkdownV2.apply(priceAlert).get()));

            if (i != productDTOList.size() - 1)
                sb.append("\n\n\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n\n");

            if (i % 14 == 0 && i != 0) {
                stringBuilders.add(new StringBuilder(sb.toString()));
                sb.setLength(0);
            }
        }
        stringBuilders.add(new StringBuilder(sb));

        if (stringBuilders.size() == 1) {
            sb.append("\u200E \n\n\n*Please select the item you want to edit*\\.");
            QuickSender.message(SendMessage.builder()
                    .chatId(user.getChatId())
                    .text(sb.toString())
                    .disableWebPagePreview(true)
                    .replyMarkup(InlineBlock.getProductChoice(productDTOList, "-edit"))
                    .parseMode(ParseMode.MarkdownV2)
                    .build());
            return;
        }

        for (StringBuilder s : stringBuilders)
            QuickSender.message(user.getChatId(), s.toString(), true);


        QuickSender.message(SendMessage.builder()
                .chatId(user.getChatId())
                .text("\u200E \n*Please select the item you want to edit\\.*")
                .disableWebPagePreview(true)
                .parseMode(ParseMode.MarkdownV2)
                .replyMarkup(InlineBlock.getProductChoice(productDTOList, "-edit"))
                .build());
    }


    @SneakyThrows
    public void displayAddProduct() {
        popupMessage("Just paste Zalando URL down below 👇", 3000, true);
        displayMenu();
    }

    @SneakyThrows
    public void exit() {
        deletePreviousMessage(2000);
    }


    @SneakyThrows
    public void displayAllProducts() {
        ActiveUser user = ActiveUser.get();
        List<ProductDTO> productDTOList = productService.getAllProductsDtoByUserId(user.getUserId());

        deletePreviousMessage();

        if (productDTOList.isEmpty()) {
            popupMessage("You don't have any products.");
            displayMenu();
            return;
        }

        List<StringBuilder> stringBuilders = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < productDTOList.size(); i++) {
            ProductDTO dto = productDTOList.get(i);

            if (productDTOList.size() == 1)
                sb.append("*That's your product* 📝\n\n");

            else if (i == 0)
                sb.append("*That's your all products* 📝\n\n");

            String price = dto.getPrice() == 0 ? "Sold Out" : String.format("%.2f zł", dto.getPrice()).replaceFirst(",", ".");
            String priceAlert = dto.getPriceAlert().equals("0") ? "OFF" : dto.getPriceAlert().equals("AUTO") ? "AUTO" : dto.getPriceAlert() + " zł";
            String variant = dto.getVariant();
            if (variant.startsWith("-oneVariant"))
                variant = variant.substring(12);
            sb.append("""     
                    *Product nr %d*
                                        
                    *Name:* %s
                    *Description:* %s
                    *Link:* %s
                    *Variant:* %s
                    *Price:* %s
                    *Price alert:* %s""".
                    formatted(i + 1,
                            MarkdownV2.apply(dto.getName()).get(),
                            MarkdownV2.apply(dto.getDescription()).get(),
                            MarkdownV2.apply(dto.getLink()).toZalandoURL().get(),
                            MarkdownV2.apply(variant).get(),
                            MarkdownV2.apply(price).get(),
                            MarkdownV2.apply(priceAlert).get()));

            if (i != productDTOList.size() - 1)
                sb.append("\n\n\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n\n");

            if (i % 14 == 0 && i != 0) {
                stringBuilders.add(new StringBuilder(sb.toString()));
                sb.setLength(0);
            }
        }

        stringBuilders.add(new StringBuilder(sb));

        if (stringBuilders.size() == 1) {
            sb.append("\u200E\n\n\n*Last updated:* ").append(PrettyTime.get(updatesHistoryService.getLastUpdateTime()));
            QuickSender.message(SendMessage.builder()
                    .chatId(user.getChatId())
                    .text(sb.toString())
                    .disableWebPagePreview(true)
                    .replyMarkup(InlineBlock.getBack())
                    .parseMode(ParseMode.MarkdownV2)
                    .build());
            return;
        }


        for (StringBuilder s : stringBuilders)
            QuickSender.message(user.getChatId(), s.toString(), true);

        QuickSender.message(SendMessage.builder()
                .chatId(user.getChatId())
                .text("\u200E\n*Last updated:* " + PrettyTime.get(updatesHistoryService.getLastUpdateTime()))
                .replyMarkup(InlineBlock.getBack())
                .parseMode(ParseMode.MarkdownV2)
                .build());
    }


    @SneakyThrows
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

        if (productService.existsByLinkAndVariant(product.getLink(), product.getVariant()))
            throw new InvalidLinkException("You already have this product in your watchlist.", "User try to add same product.");

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

    @SneakyThrows
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

    @SneakyThrows
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

















