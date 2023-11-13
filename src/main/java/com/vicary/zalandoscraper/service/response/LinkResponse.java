package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.api_telegram.api_object.Action;
import com.vicary.zalandoscraper.api_telegram.api_object.keyboard.InlineKeyboardButton;
import com.vicary.zalandoscraper.api_telegram.api_object.keyboard.InlineKeyboardMarkup;
import com.vicary.zalandoscraper.api_telegram.api_request.send.SendMessage;
import com.vicary.zalandoscraper.entity.LinkRequestEntity;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.Scraper;
import com.vicary.zalandoscraper.service.entity.LinkRequestService;
import com.vicary.zalandoscraper.service.entity.ProductService;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class LinkResponse {

    private final Scraper scraper = Scraper.getInstance();

    private final LinkRequestService linkRequestService;

    private final ProductService productService;

    public void response(String link) {
        String chatId = ActiveUser.get().getChatId();
        int messageId = QuickSender.messageWithReturn(chatId, "Processing...", false).getMessageId();
        QuickSender.chatAction(chatId, Action.TYPING);
        ActiveUser.get().setMessageId(messageId);

        List<String> variants = scraper.getAllVariants(link);

        if (variants.size() == 1 && variants.getFirst().contains("-oneVariant")) {
            addProduct(link, variants.getFirst());
        } else {
            sendVariantMessage(variants);
        }
    }


    public void addProduct(String link, String oneVariant) {
        Product product = scraper.getProduct(link, oneVariant);
        QuickSender.deleteMessage(ActiveUser.get().getChatId(), ActiveUser.get().getMessageId());

        if (productService.existsByUserIdAndLinkAndVariant(ActiveUser.get().getUserId(), product.getLink(), product.getVariant()))
            throw new InvalidLinkException("You already have this product in your watchlist.", "User try to add same product.");

        if (productService.countByUserId(ActiveUser.get().getUserId()) > 9)
            throw new InvalidLinkException("You cannot add more than 10 products.", "User try to add more than 10 products.");

        productService.saveProduct(product);
        QuickSender.message(ActiveUser.get().getChatId(), "Product added successfully.", false);
    }

    public void sendVariantMessage(List<String> variants) {
        String requestId = generateRequestId();
        addLinkRequestToRepository(requestId);
        String command = "-l " + requestId + " ";

        List<List<InlineKeyboardButton>> listOfListsOfButtons = new ArrayList<>();
        List<InlineKeyboardButton> listOfButtons = new ArrayList<>();
        for (int i = 0; i < variants.size(); i++) {

            listOfButtons.add(InlineKeyboardButton.builder()
                    .text(variants.get(i))
                    .callbackData(command + variants.get(i))
                    .build());

            if (i == variants.size() - 1) {
                listOfListsOfButtons.add(listOfButtons);
            }

            if (listOfButtons.size() == 3) {
                List<InlineKeyboardButton> threeButtons = new ArrayList<>();
                for (int k = 0; k < 3; k++)
                    threeButtons.add(listOfButtons.get(k));

                listOfListsOfButtons.add(threeButtons);
                listOfButtons.clear();
            }
        }

        InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup(listOfListsOfButtons);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(ActiveUser.get().getChatId())
                .text("Select a product variant")
                .replyMarkup(replyMarkup)
                .build();

        QuickSender.deleteMessage(ActiveUser.get().getChatId(), ActiveUser.get().getMessageId());
        QuickSender.message(sendMessage);
    }

    public void addLinkRequestToRepository(String requestId) {
        long fiveMinutes = 1000 * 60 * 5;
        ActiveUser activeUser = ActiveUser.get();
        LinkRequestEntity entity = LinkRequestEntity.builder()
                .requestId(requestId)
                .link(activeUser.getText())
                .expiration(System.currentTimeMillis() + fiveMinutes)
                .build();
        linkRequestService.saveRequest(entity);
    }

    public String generateRequestId() {
        StringBuilder sb = new StringBuilder();
        IntStream intStream = ThreadLocalRandom.current().ints(10, 0, 10);
        intStream.forEach(sb::append);
        return sb.toString();
    }
}
