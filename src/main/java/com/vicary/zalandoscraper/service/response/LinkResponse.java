package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.scraper.Scraper;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import com.vicary.zalandoscraper.api_telegram.api_object.Action;
import com.vicary.zalandoscraper.api_telegram.api_request.send.SendMessage;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LinkResponse implements Responser {
    private final static Logger logger = LoggerFactory.getLogger(LinkResponse.class);
    private final ResponseFacade responseFacade;
    private final ActiveUser user;
    private final Scraper scraper;

    public LinkResponse(ResponseFacade responseFacade,
                        ActiveUser user,
                        Scraper scraper) {
        this.responseFacade = responseFacade;
        this.user = user;
        this.scraper = scraper;
    }

    @Override
    public void response() {
        int messageId = QuickSender.messageWithReturn(user.getChatId(), Messages.other("processing"), false).getMessageId();
        QuickSender.chatAction(user.getChatId(), Action.TYPING);

        List<String> variants = scraper.getAllVariants(user.getText());

        if (isItemOneVariant(variants)) {
            getAndSaveOneVariantProduct(variants.get(0), scraper);
            QuickSender.deleteMessage(user.getChatId(), messageId);
            QuickSender.message(user.getUserId(), Messages.other("productAdded"), false);
        } else {
            String requestId = responseFacade.generateAndSaveRequest(user.getText());
            sendVariantMessage(variants, requestId);
            QuickSender.deleteMessage(user.getChatId(), messageId);
        }
    }


    private void getAndSaveOneVariantProduct(String variant, Scraper scraper) {
        Product product = scraper.getProduct(user.getText(), variant);

        checkProductValidation(product);

        responseFacade.saveProduct(product);
    }

    private void checkProductValidation(Product product) {
        if (responseFacade.productExistsByUserIdAndLinkAndVariant(user.getChatId(), product.getLink(), product.getVariant()))
            throw new InvalidLinkException(Messages.other("alreadyHave"), "User try to add same product.");

        if (responseFacade.countProductsByUserId(user.getUserId()) > 9)
            throw new InvalidLinkException(Messages.other("productLimit"), "User try to add more than 10 products.");
    }

    private void sendVariantMessage(List<String> variants, String requestId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(user.getChatId())
                .text(Messages.other("selectVariant"))
                .replyMarkup(InlineBlock.getVariantChoice(variants, requestId))
                .build();

        QuickSender.message(sendMessage);
    }

    private boolean isItemOneVariant(List<String> variants) {
        return variants.size() == 1 && variants.get(0).contains("-oneVariant");
    }
}
