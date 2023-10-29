package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.PrettyTime;
import com.vicary.zalandoscraper.api_request.send.SendMessage;
import com.vicary.zalandoscraper.entity.AwaitedMessageEntity;
import com.vicary.zalandoscraper.entity.LinkRequestEntity;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.entity.*;
import com.vicary.zalandoscraper.service.Scraper;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.service.quick_sender.QuickSender;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyMarkupResponse {

    private final Scraper scraper;

    private final LinkRequestService linkRequestService;

    private final QuickSender quickSender;

    private final ProductService productService;

    private final AwaitedMessageService awaitedMessageService;

    private final UserService userService;

    private final UpdatesHistoryService updatesHistoryService;


    public void response(String text) {
        System.out.println(text);

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
            displayDeleteYesOrNo();

        else if (text.equals("-deleteAllYes"))
            deleteAllProducts();

        else if (text.equals("-deleteAllNo"))
            backToMenu();

        else if (text.equals("-exit"))
            exit();

        else if (text.equals("-back"))
            backToMenu();

        else if (text.equals("-notification"))
            displayNotification();

        else if (text.equals("-enableEmail") || text.equals("-disableEmail"))
            updateNotifyByEmail(text);

        else if (text.equals("-setEmail"))
            displaySetEmailMessage(text);
    }


    @SneakyThrows
    public void displaySetEmailMessage(String text) {
        var awaitedMessageEntity = AwaitedMessageEntity.builder()
                .userId(ActiveUser.get().getUserId())
                .request(text)
                .build();
        awaitedMessageService.saveAwaitedMessage(awaitedMessageEntity);
        quickSender.deleteMessage(ActiveUser.get().getChatId(), ActiveUser.get().getMessageId());
        Thread.sleep(1500);

        String message = """
                Okay, send me a new email address.
                                
                Type DELETE if you want to delete your email address.""";

        quickSender.message(ActiveUser.get().getUserId(), message, false);
    }


    @SneakyThrows
    public void updateNotifyByEmail(String text) {
        ActiveUser user = ActiveUser.get();

        if (user.getEmail() == null) {
            int messageId = quickSender.messageWithReturn(user.getChatId(), "You have to set up an email address.", false).getMessageId();
            user.setMessageId(messageId);
            Thread.sleep(2000);
            displayNotification();
            return;
        }
        boolean notifyByEmail = text.equals("-enableEmail");
        userService.updateNotifyByEmailById(user.getUserId(), notifyByEmail);
        user.setNotifyByEmail(notifyByEmail);
        Thread.sleep(1000);
        displayNotification();
    }


    public void displayNotification() {
        ActiveUser user = ActiveUser.get();

        quickSender.deleteMessage(user.getChatId(), user.getMessageId());

        String email = user.getEmail() == null ? "not specified" : user.getEmail();

        quickSender.message(InlineBlock.getNotification(user.isNotifyByEmail(), email));
    }


    @SneakyThrows
    public void deleteAllProducts() {
        ActiveUser user = ActiveUser.get();

        quickSender.deleteMessage(user.getChatId(), user.getMessageId());

        productService.deleteAllProductsByUserId(user.getUserId());

        int messageId = quickSender.messageWithReturn(user.getChatId(), "All products deleted successfully", false).getMessageId();
        Thread.sleep(2000);

        quickSender.deleteMessage(user.getChatId(), messageId);
        quickSender.message(InlineBlock.getMenu());
    }


    @SneakyThrows
    public void displayDeleteYesOrNo() {
        ActiveUser user = ActiveUser.get();

        quickSender.deleteMessage(user.getChatId(), user.getMessageId());

        quickSender.message(InlineBlock.getDeleteYesOrNo());
        Thread.sleep(1500);
    }

    @SneakyThrows
    public void deleteProduct(String text) {
        String[] textArray = text.split(" ");
        long productId = Long.parseLong(textArray[1]);
        ActiveUser user = ActiveUser.get();

        quickSender.deleteMessage(user.getChatId(), user.getMessageId());

        productService.deleteProductById(productId);

        int messageId = quickSender.messageWithReturn(user.getChatId(), "Product deleted successfully.", false).getMessageId();
        Thread.sleep(2000);
        quickSender.deleteMessage(user.getChatId(), messageId);
        quickSender.message(InlineBlock.getMenu());
    }


    @SneakyThrows
    public void displayDeleteProduct() {
        ActiveUser user = ActiveUser.get();
        List<ProductDTO> productDTOList = productService.getAllProductsDtoByUserId(user.getUserId());

        quickSender.deleteMessage(user.getChatId(), user.getMessageId());

        if (productDTOList.isEmpty()) {
            quickSender.deleteMessage(user.getChatId(), user.getMessageId());
            int messageId = quickSender.messageWithReturn(user.getChatId(), "You don't have any products.", false).getMessageId();
            Thread.sleep(2000);
            quickSender.deleteMessage(user.getChatId(), messageId);
            quickSender.message(InlineBlock.getMenu());
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < productDTOList.size(); i++) {
            ProductDTO dto = productDTOList.get(i);

            if (productDTOList.size() == 1)
                sb.append("Product:\n\n");

            else if (i == 0)
                sb.append("Products:\n\n");

            String price = dto.getPrice() == 0 ? "Sold Out" : String.format("%.2f zł", dto.getPrice());
            String variant = dto.getVariant();
            if (variant.startsWith("-oneVariant "))
                variant = variant.substring(12);

            sb.append("""     
                    Product nr %d
                    Name: %s
                    Description: %s
                    Variant: %s
                    Price: %s"""
                    .formatted(i + 1,
                            dto.getName(),
                            dto.getDescription(),
                            variant,
                            price));

            if (i != productDTOList.size() - 1)
                sb.append("\n\n------------\n\n");
        }

        sb.append("\n\nPlease select the item number you want to delete.");

        SendMessage sendMessage = SendMessage.builder()
                .chatId(user.getChatId())
                .text(sb.toString())
                .disableWebPagePreview(true)
                .replyMarkup(InlineBlock.getProductChoice(productDTOList, "-delete"))
                .build();
        quickSender.message(sendMessage);
    }


    @SneakyThrows
    public void displayEditPriceAlertMessage(String text) {
        var awaitedMessageEntity = AwaitedMessageEntity.builder()
                .userId(ActiveUser.get().getUserId())
                .request(text)
                .build();
        awaitedMessageService.saveAwaitedMessage(awaitedMessageEntity);
        quickSender.deleteMessage(ActiveUser.get().getChatId(), ActiveUser.get().getMessageId());
        Thread.sleep(1500);

        String[] textArray = text.split(" ");
        Long productId = Long.valueOf(textArray[1]);

        ProductDTO dto = productService.getProductDTOById(productId);

        String price = dto.getPrice() == 0 ? "Sold Out" : String.format("%.2f zł", dto.getPrice()).replaceFirst(",", ".");
        String priceAlert = dto.getPriceAlert().equals("0") ? "OFF" : dto.getPriceAlert().equals("AUTO") ? "AUTO" : dto.getPriceAlert() + " zł";
        String variant = dto.getVariant();
        if (variant.startsWith("-oneVariant "))
            variant = variant.substring(12);

        String message = """
                Product to edit:
                                
                Name: %s
                Description: %s
                Variant: %s
                Price: %s
                Price Alert: %s
                                
                ------------
                                
                Okay, send me a new price alert.
                For example: 100.50zł
                                
                Type AUTO for automatic notification.
                Type 0 if you don't want notification at all."""
                .formatted(dto.getName(),
                        dto.getDescription(),
                        variant,
                        price,
                        priceAlert);

        quickSender.message(ActiveUser.get().getUserId(), message, false);
    }

    @SneakyThrows
    public void backToMenu() {
        quickSender.deleteMessage(ActiveUser.get().getChatId(), ActiveUser.get().getMessageId());
        quickSender.message(InlineBlock.getMenu());
        Thread.sleep(1500);
    }

    @SneakyThrows
    public void displayEditPriceAlert() {
        ActiveUser user = ActiveUser.get();
        List<ProductDTO> productDTOList = productService.getAllProductsDtoByUserId(user.getUserId());

        quickSender.deleteMessage(user.getChatId(), user.getMessageId());

        if (productDTOList.isEmpty()) {
            quickSender.deleteMessage(user.getChatId(), user.getMessageId());
            int messageId = quickSender.messageWithReturn(user.getChatId(), "You don't have any products.", false).getMessageId();
            Thread.sleep(2000);
            quickSender.deleteMessage(user.getChatId(), messageId);
            quickSender.message(InlineBlock.getMenu());
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < productDTOList.size(); i++) {
            ProductDTO dto = productDTOList.get(i);

            if (productDTOList.size() == 1)
                sb.append("Product:\n\n");

            else if (i == 0)
                sb.append("Products:\n\n");

            String price = dto.getPrice() == 0 ? "Sold Out" : String.format("%.2f zł", dto.getPrice()).replaceFirst(",", ".");
            String priceAlert = dto.getPriceAlert().equals("0") ? "OFF" : dto.getPriceAlert().equals("AUTO") ? "AUTO" : dto.getPriceAlert() + " zł";
            String variant = dto.getVariant();
            if (variant.startsWith("-oneVariant "))
                variant = variant.substring(12);

            sb.append("""     
                    Product %d
                                        
                    Name: %s
                    Description: %s
                    Variant: %s
                    Price: %s
                    Price alert: %s"""
                    .formatted(i + 1,
                            dto.getName(),
                            dto.getDescription(),
                            variant,
                            price,
                            priceAlert));

            if (i != productDTOList.size() - 1)
                sb.append("\n\n------------\n\n");
        }

        sb.append("\n\nPlease select the item number you want to edit.");

        SendMessage sendMessage = SendMessage.builder()
                .chatId(user.getChatId())
                .text(sb.toString())
                .disableWebPagePreview(true)
                .replyMarkup(InlineBlock.getProductChoice(productDTOList, "-edit"))
                .build();
        quickSender.message(sendMessage);
    }


    @SneakyThrows
    public void displayAddProduct() {
        ActiveUser user = ActiveUser.get();
        quickSender.deleteMessage(user.getChatId(), user.getMessageId());
        int messageId = quickSender.messageWithReturn(user.getChatId(), "Just paste Zalando URL down below 👇", false).getMessageId();
        Thread.sleep(3000);
        quickSender.deleteMessage(user.getChatId(), messageId);
        quickSender.message(InlineBlock.getMenu());
    }

    @SneakyThrows
    public void exit() {
        quickSender.deleteMessage(ActiveUser.get().getChatId(), ActiveUser.get().getMessageId());
        Thread.sleep(2000);
    }


    @SneakyThrows
    public void displayAllProducts() {
        ActiveUser user = ActiveUser.get();
        List<ProductDTO> productDTOList = productService.getAllProductsDtoByUserId(user.getUserId());

        quickSender.deleteMessage(user.getChatId(), user.getMessageId());

        if (productDTOList.isEmpty()) {
            quickSender.deleteMessage(user.getChatId(), user.getMessageId());
            int messageId = quickSender.messageWithReturn(user.getChatId(), "You don't have any products.", false).getMessageId();
            Thread.sleep(2000);
            quickSender.deleteMessage(user.getChatId(), messageId);
            quickSender.message(InlineBlock.getMenu());
            return;
        }

        List<StringBuilder> stringBuilders = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < productDTOList.size(); i++) {
            if (i % 14 == 0) {
                stringBuilders.add(new StringBuilder(sb.toString()));
                sb.setLength(0);
            }

            ProductDTO dto = productDTOList.get(i);

            if (productDTOList.size() == 1)
                sb.append("That's your product:\n\n");

            else if (i == 0)
                sb.append("That's your all products:\n\n");

            String price = dto.getPrice() == 0 ? "Sold Out" : String.format("%.2f zł", dto.getPrice()).replaceFirst(",", ".");
            String priceAlert = dto.getPriceAlert().equals("0") ? "OFF" : dto.getPriceAlert().equals("AUTO") ? "AUTO" : dto.getPriceAlert() + " zł";
            String variant = dto.getVariant();
            if (variant.startsWith("-oneVariant"))
                variant = variant.substring(12);
            sb.append("""     
                    Product %d
                                        
                    Name: %s
                    Description: %s
                    Link: %s
                    Variant: %s
                    Price: %s
                    Price alert: %s""".
                    formatted(i + 1,
                            dto.getName(),
                            dto.getDescription(),
                            dto.getLink(),
                            variant,
                            price,
                            priceAlert));

            if (i != productDTOList.size() - 1)
                sb.append("\n\n------------\n\n");
        }

        sb.append("\n\nLast updated: ").append(PrettyTime.get(updatesHistoryService.getLastUpdateTime()));

        System.out.println("SB length: " + sb.length());
        SendMessage sendMessage = SendMessage.builder()
                .chatId(user.getChatId())
                .text(sb.toString())
                .replyMarkup(InlineBlock.getBack())
                .disableWebPagePreview(true)
                .build();

        quickSender.message(sendMessage);
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

        quickSender.deleteMessage(chatId, ActiveUser.get().getMessageId());
        int messageId = quickSender.messageWithReturn(chatId, "Adding product...", false).getMessageId();
        quickSender.chatAction(chatId, "typing");

        Product product = scraper.getProduct(link, variant.toString().trim());

        quickSender.deleteMessage(chatId, messageId);

        if (productService.existsByLinkAndVariant(product.getLink(), product.getVariant()))
            throw new InvalidLinkException("You already have this product in your watchlist.", "User try to add same product.");

        productService.saveProduct(product);
        quickSender.messageWithReturn(ActiveUser.get().getChatId(), "Product added successfully.", false);
    }


    private String getLink(String requestId) {
        LinkRequestEntity linkRequest = linkRequestService.findByRequestId(requestId);
        if (linkRequest == null)
            throw new InvalidLinkException("This session expired.", "User '%s' session expired".formatted(ActiveUser.get().getUserId()));

        checkExpiration(linkRequest.getExpiration());
        return linkRequest.getLink();
    }

    private void checkExpiration(long expiration) {
        if (System.currentTimeMillis() > expiration)
            throw new InvalidLinkException("This session expired.", "User '%s' session expired".formatted(ActiveUser.get().getUserId()));
    }
}

















