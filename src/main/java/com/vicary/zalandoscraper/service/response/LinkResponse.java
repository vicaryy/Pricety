package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.scraper.Scraper;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import com.vicary.zalandoscraper.api_telegram.api_object.Action;
import com.vicary.zalandoscraper.api_telegram.api_request.send.SendMessage;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.entity.LinkRequestService;
import com.vicary.zalandoscraper.service.entity.ProductService;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkResponse {

    private final LinkRequestService linkRequestService;

    private final ProductService productService;

    public void response(ActiveUser user, Scraper scraper) {
        String link = user.getText();
        int messageId = QuickSender.messageWithReturn(user.getChatId(), Messages.other("processing"), false).getMessageId();
        QuickSender.chatAction(user.getChatId(), Action.TYPING);

        List<String> variants = scraper.getAllVariants(link);

        if (isItemOneVariant(variants)) {
            getAndSaveOneVariantProduct(link, variants.get(0), scraper, user.getUserId());
            QuickSender.deleteMessage(user.getChatId(), messageId);
            QuickSender.message(user.getUserId(), Messages.other("productAdded"), false);
        } else {
            String requestId = linkRequestService.generateAndSaveRequest(link);
            sendVariantMessage(variants, requestId, user.getUserId());
            QuickSender.deleteMessage(user.getChatId(), messageId);
        }
    }


    private void getAndSaveOneVariantProduct(String link, String variant, Scraper scraper, String userId) {
        Product product = scraper.getProduct(link, variant);

        checkProductValidation(product, userId);

        productService.saveProduct(product);
    }

    private void checkProductValidation(Product product, String userId) {
        if (productService.existsByUserIdAndLinkAndVariant(userId, product.getLink(), product.getVariant()))
            throw new InvalidLinkException(Messages.other("alreadyHave"), "User try to add same product.");

        if (productService.countByUserId(userId) > 9)
            throw new InvalidLinkException(Messages.other("productLimit"), "User try to add more than 10 products.");
    }

    private void sendVariantMessage(List<String> variants, String requestId, String userId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(userId)
                .text(Messages.other("selectVariant"))
                .replyMarkup(InlineBlock.getVariantChoice(variants, requestId))
                .build();

        QuickSender.message(sendMessage);
    }

    private boolean isItemOneVariant(List<String> variants) {
        return variants.size() == 1 && variants.get(0).contains("-oneVariant");
    }
}
