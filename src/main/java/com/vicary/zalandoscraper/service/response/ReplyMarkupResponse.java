package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.api_object.Update;
import com.vicary.zalandoscraper.api_object.message.Message;
import com.vicary.zalandoscraper.api_object.other.CallbackQuery;
import com.vicary.zalandoscraper.api_request.edit_message.DeleteMessage;
import com.vicary.zalandoscraper.api_request.edit_message.EditMessageText;
import com.vicary.zalandoscraper.entity.LinkRequestEntity;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.exception.ZalandoScraperBotException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.repository.ProductRepository;
import com.vicary.zalandoscraper.service.LinkRequestService;
import com.vicary.zalandoscraper.service.ProductService;
import com.vicary.zalandoscraper.service.Scraper;
import com.vicary.zalandoscraper.service.quick_sender.QuickSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyMarkupResponse {

    private final Scraper scraper;

    private final LinkRequestService linkRequestService;

    private final QuickSender quickSender;

    private final ProductService productService;

    public void response(String text) {
        if (text.startsWith("-l"))
            addProduct(text.substring(3));

    }


    public void addProduct(String requestId) {
        String chatId = ActiveUser.get().getChatId();
        String[] arrayRequest = requestId.split(" ");
        String link = getLink(arrayRequest[0]);
        String variant = arrayRequest[1];

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

















