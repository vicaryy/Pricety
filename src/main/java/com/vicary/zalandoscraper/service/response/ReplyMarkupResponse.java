package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.api_request.send.SendMessage;
import com.vicary.zalandoscraper.entity.AwaitedMessageEntity;
import com.vicary.zalandoscraper.entity.LinkRequestEntity;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.entity.AwaitedMessageService;
import com.vicary.zalandoscraper.service.entity.LinkRequestService;
import com.vicary.zalandoscraper.service.entity.ProductService;
import com.vicary.zalandoscraper.service.Scraper;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.service.quick_sender.QuickSender;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyMarkupResponse {

    private final Scraper scraper;

    private final LinkRequestService linkRequestService;

    private final QuickSender quickSender;

    private final ProductService productService;

    private final AwaitedMessageService awaitedMessageService;


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

        else if (text.equals("-exit"))
            exit();

        else if (text.equals("-back"))
            back();

    }


    @SneakyThrows
    public void displayEditPriceAlertMessage(String text) {
        var awaitedMessageEntity = AwaitedMessageEntity.builder()
                .userId(ActiveUser.get().getUserId())
                .request(text)
                .build();
//        awaitedMessageService.saveAwaitedMessage(awaitedMessageEntity);
        quickSender.deleteMessage(ActiveUser.get().getChatId(), ActiveUser.get().getMessageId());
        Thread.sleep(1500);

        String[] textArray = text.split(" ");
        Long productId = Long.valueOf(textArray[1]);

        ProductDTO productDTO = productService.getProductDTOById(productId);


        String message = """
                Product to edit:
                
                Name: %s
                Description: %s
                Price: %.2f
                Price Alert: %s
                
                ------------
                
                Okay, send me a new price alert.
                For example: 100.50zł
                
                Type AUTO for automatic notification.
                Type 0 if you don't want notification at all."""
                .formatted(productDTO.getName(), productDTO.getDescription(), productDTO.getPrice(), productDTO.getPriceAlert());

        quickSender.message(ActiveUser.get().getUserId(), message, false);
    }

    public void back() {
        quickSender.deleteMessage(ActiveUser.get().getChatId(), ActiveUser.get().getMessageId());
        quickSender.message(InlineBlock.getMenu());
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

            sb.append("""     
                    Product nr %d
                    Description: %s
                    Price: %.2f zł
                    Price alert: %s""".formatted(i + 1, dto.getDescription(), dto.getPrice(), dto.getPriceAlert()));

            if (i != productDTOList.size() - 1)
                sb.append("\n\n------------\n\n");
        }

        sb.append("\n\nPlease select the item number you want to edit.");

        SendMessage sendMessage = SendMessage.builder()
                .chatId(user.getChatId())
                .text(sb.toString())
                .disableWebPagePreview(true)
                .replyMarkup(InlineBlock.getProductChoice(productDTOList))
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
        Thread.sleep(1500);
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

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < productDTOList.size(); i++) {
            ProductDTO dto = productDTOList.get(i);

            if (productDTOList.size() == 1)
                sb.append("That's your product:\n\n");

            else if (i == 0)
                sb.append("That's your all products:\n\n");

            sb.append("""     
                    Product nr %d
                    Name: %s
                    Description: %s
                    Link: %s
                    Variant: %s
                    Price: %.2f zł
                    Price alert: %s""".formatted(i + 1, dto.getName(), dto.getDescription(), dto.getLink(), dto.getVariant(), dto.getPrice(), dto.getPriceAlert()));

            if (i != productDTOList.size() - 1)
                sb.append("\n\n------------\n\n");

            if (i == productDTOList.size() - 1)
                sb.append("\n\nLast updated: Tutaj powinien być last update // TODO");
        }

        SendMessage sendMessage = SendMessage.builder()
                .chatId(user.getChatId())
                .text(sb.toString())
                .replyMarkup(InlineBlock.getBack())
                .disableWebPagePreview(true)
                .build();

        quickSender.message(sendMessage);
    }


    public void addProduct(String text) {
        String chatId = ActiveUser.get().getChatId();
        String[] arrayText = text.split(" ");
        String requestId = arrayText[1];
        String link = getLink(requestId);
        String variant = arrayText[2];

        quickSender.deleteMessage(chatId, ActiveUser.get().getMessageId());
        int messageId = quickSender.messageWithReturn(chatId, "Adding product..", false).getMessageId();
        quickSender.chatAction(chatId, "typing");

        Product product = scraper.getProduct(link, variant);

        productService.saveProduct(product);
        quickSender.deleteMessage(chatId, messageId);
        quickSender.message(chatId, "Product added successfully.", false);
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

















