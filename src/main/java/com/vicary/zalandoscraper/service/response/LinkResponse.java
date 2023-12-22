package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.scraper.Scraper;
import com.vicary.zalandoscraper.service.response.inline_markup.InlineKeyboardMarkupFactory;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import com.vicary.zalandoscraper.api_telegram.api_object.Action;
import com.vicary.zalandoscraper.api_telegram.api_request.send.SendMessage;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.model.Product;

import java.util.List;

public class LinkResponse implements Responser {
    private static final int MAX_PRODUCT_LIMIT = 10;
    private int currentMessageId;
    private final ResponseFacade responseFacade;
    private final ActiveUser user;
    private final Scraper scraper;
    private final QuickSender quickSender;

    public LinkResponse(ResponseFacade responseFacade,
                        ActiveUser user,
                        Scraper scraper) {
        this.responseFacade = responseFacade;
        this.user = user;
        this.scraper = scraper;
        this.quickSender = new QuickSender();
    }

    public LinkResponse(ResponseFacade responseFacade,
                        ActiveUser user,
                        Scraper scraper,
                        QuickSender quickSender) {
        this.responseFacade = responseFacade;
        this.user = user;
        this.scraper = scraper;
        this.quickSender = quickSender;
    }

    @Override
    public void response() {
        currentMessageId = quickSender.messageWithReturn(user.getChatId(), Messages.other("processing"), false).getMessageId();
        quickSender.chatAction(user.getChatId(), Action.TYPING);

        checkUserLimit();

        List<String> variants = scraper.getAllVariants(user.getText());

        if (isItemOneVariant(variants)) {
            checkIfUserHaveProduct(user.getText(), variants.get(0));
            getAndSaveOneVariantProduct(variants.get(0), scraper);
            quickSender.deleteMessage(user.getChatId(), currentMessageId);
            quickSender.message(user.getUserId(), Messages.other("productAdded"), false);
        } else {
            String requestId = responseFacade.generateAndSaveRequest(user.getText());
            quickSender.deleteMessage(user.getChatId(), currentMessageId);
            sendVariantMessage(variants, requestId);
        }
    }


    private void getAndSaveOneVariantProduct(String variant, Scraper scraper) {
        Product product = scraper.getProduct(user.getText(), variant);
        responseFacade.saveProduct(product, user.getUserId());
    }

    private void checkIfUserHaveProduct(String link, String variant) {
        if (responseFacade.productExistsByUserIdAndLinkAndVariant(user.getChatId(), link, variant)) {
            quickSender.deleteMessage(user.getChatId(), currentMessageId);
            throw new InvalidLinkException(Messages.other("alreadyHave"), "User try to add same product.");
        }
    }

    private void checkUserLimit() {
        if (responseFacade.countProductsByUserId(user.getUserId()) >= MAX_PRODUCT_LIMIT && !user.isPremium()) {
            quickSender.deleteMessage(user.getChatId(), currentMessageId);
            throw new InvalidLinkException(Messages.other("productLimit"), "User try to add more than 10 products.");
        }
    }

    private void sendVariantMessage(List<String> variants, String requestId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(user.getChatId())
                .text(Messages.other("selectVariant"))
                .replyMarkup(InlineKeyboardMarkupFactory.getVariantChoice(variants, requestId))
                .build();

        quickSender.message(sendMessage);
    }

    private boolean isItemOneVariant(List<String> variants) {
        return variants.size() == 1 && variants.get(0).contains("-oneVariant");
    }
}
