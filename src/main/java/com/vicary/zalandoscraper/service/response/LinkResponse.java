package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.api_object.keyboard.*;
import com.vicary.zalandoscraper.api_object.message.Message;
import com.vicary.zalandoscraper.api_request.edit_message.EditMessageText;
import com.vicary.zalandoscraper.api_request.send.SendMessage;
import com.vicary.zalandoscraper.entity.LinkRequestEntity;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.LinkRequestService;
import com.vicary.zalandoscraper.service.RequestService;
import com.vicary.zalandoscraper.service.Scraper;
import com.vicary.zalandoscraper.service.quick_sender.QuickSender;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class LinkResponse {

    private final Scraper scraper;

    private final QuickSender quickSender;

    private final RequestService requestService;

    private final LinkRequestService linkRequestService;

    public void response(String URL) {
        String chatId = ActiveUser.get().getChatId();
        int messageId = quickSender.messageWithReturn(chatId, "Give me a sec..", false).getMessageId();
        quickSender.chatAction(chatId, "typing");
        ActiveUser.get().setMessageId(messageId);

        List<String> variants = scraper.getSizes(URL);

        if (variants.size() == 1 && variants.getFirst().contains("-oneVariant")) {
            addProduct(URL, variants.getFirst());
        } else {
            sendVariantMessage(variants);
        }
    }


    public void addProduct(String URL, String oneVariant) {
        Product product = scraper.getProduct(URL, oneVariant);
        System.out.println(product);
        quickSender.deleteMessage(ActiveUser.get().getChatId(), ActiveUser.get().getMessageId());
        quickSender.message(ActiveUser.get().getChatId(), "Product added successfully.", false);
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

        try {
            quickSender.deleteMessage(ActiveUser.get().getChatId(), ActiveUser.get().getMessageId());
            requestService.sendRequest(sendMessage);
        } catch (WebClientResponseException e) {
            System.out.println(e.getResponseBodyAsString());
        }
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
