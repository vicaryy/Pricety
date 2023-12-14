package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.api_telegram.api_object.Action;
import com.vicary.zalandoscraper.api_telegram.api_object.message.Message;
import com.vicary.zalandoscraper.api_telegram.api_request.send.SendMessage;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.scraper.Scraper;
import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LinkResponseTest {

    private static LinkResponse linkResponse;
    private static Scraper scraper;
    private static ResponseFacade responseFacade;
    private static QuickSender quickSender;

    @BeforeAll
    static void beforeAll() {
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages", Locale.of("en")));
    }

    @BeforeEach
    void beforeEach() {
        responseFacade = mock(ResponseFacade.class);
        quickSender = mock(QuickSender.class);
        scraper = mock(Scraper.class);
    }

    @Test
    void response_OneItemVariantEverythingNormal() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        List<String> givenListOfVariants = List.of("-oneVariant One Variant");
        Product givenProduct = getDefaultProduct();
        Message givenMessage = getDefaultMessage();

        //when
        when(quickSender.messageWithReturn(givenUser.getChatId(), Messages.other("processing"), false)).thenReturn(givenMessage);
        when(scraper.getAllVariants(givenUser.getText())).thenReturn(givenListOfVariants);
        when(scraper.getProduct(givenUser.getText(), givenListOfVariants.get(0))).thenReturn(givenProduct);
        when(responseFacade.productExistsByUserIdAndLinkAndVariant(
                givenUser.getChatId(),
                givenUser.getText(),
                givenProduct.getVariant()))
                .thenReturn(false);
        when(responseFacade.countProductsByUserId(givenUser.getChatId())).thenReturn(0);

        //then
        linkResponse = new LinkResponse(responseFacade, givenUser, scraper, quickSender);
        linkResponse.response();

        verify(quickSender, times(1)).messageWithReturn(givenUser.getChatId(), Messages.other("processing"), false);
        verify(responseFacade, times(1)).saveProduct(givenProduct, givenUser.getChatId());
        verify(quickSender, times(1)).chatAction(givenUser.getChatId(), Action.TYPING);
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(quickSender, times(1)).message(givenUser.getChatId(), Messages.other("productAdded"), false);
    }

    @Test
    void response_MultiVariantEverythingNormal() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        List<String> givenListOfVariants = List.of("First Size", "Second Size", "Third Size");
        String givenRequestId = "12345";
        Message givenMessage = getDefaultMessage();
        SendMessage givenSendMessage = getSendMessageWithVariants(givenUser.getChatId(), givenListOfVariants, givenRequestId);

        //when
        when(quickSender.messageWithReturn(givenUser.getChatId(), Messages.other("processing"), false)).thenReturn(givenMessage);
        when(scraper.getAllVariants(givenUser.getText())).thenReturn(givenListOfVariants);
        when(responseFacade.generateAndSaveRequest(givenUser.getText())).thenReturn(givenRequestId);

        //then
        linkResponse = new LinkResponse(responseFacade, givenUser, scraper, quickSender);
        linkResponse.response();


        verify(quickSender, times(1)).messageWithReturn(givenUser.getChatId(), Messages.other("processing"), false);
        verify(quickSender, times(1)).chatAction(givenUser.getChatId(), Action.TYPING);
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(quickSender, times(1)).message(givenSendMessage);
    }

    @Test
    void response_expectThrow_UserAlreadyHaveThisItem() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        List<String> givenListOfVariants = List.of("-oneVariant One Variant");
        Product givenProduct = getDefaultProduct();
        Message givenMessage = getDefaultMessage();

        //when
        when(quickSender.messageWithReturn(givenUser.getChatId(), Messages.other("processing"), false)).thenReturn(givenMessage);
        when(scraper.getAllVariants(givenUser.getText())).thenReturn(givenListOfVariants);
        when(scraper.getProduct(givenUser.getText(), givenListOfVariants.get(0))).thenReturn(givenProduct);
        when(responseFacade.productExistsByUserIdAndLinkAndVariant(
                givenUser.getChatId(),
                givenUser.getText(),
                givenProduct.getVariant()))
                .thenReturn(true);

        //then
        linkResponse = new LinkResponse(responseFacade, givenUser, scraper, quickSender);
        assertThrows(InvalidLinkException.class, () -> linkResponse.response());

        verify(quickSender, times(1)).messageWithReturn(givenUser.getChatId(), Messages.other("processing"), false);
        verify(quickSender, times(1)).chatAction(givenUser.getChatId(), Action.TYPING);
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(responseFacade, times(0)).saveProduct(givenProduct, givenUser.getChatId());
        verify(quickSender, times(0)).message(givenUser.getChatId(), Messages.other("productAdded"), false);
    }

    @Test
    void response_expectThrow_MaxProductLimit() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        List<String> givenListOfVariants = List.of("-oneVariant One Variant");
        Product givenProduct = getDefaultProduct();
        Message givenMessage = getDefaultMessage();
        int amountOfProducts = 11;

        //when
        when(quickSender.messageWithReturn(givenUser.getChatId(), Messages.other("processing"), false)).thenReturn(givenMessage);
        when(scraper.getAllVariants(givenUser.getText())).thenReturn(givenListOfVariants);
        when(scraper.getProduct(givenUser.getText(), givenListOfVariants.get(0))).thenReturn(givenProduct);
        when(responseFacade.productExistsByUserIdAndLinkAndVariant(
                givenUser.getChatId(),
                givenUser.getText(),
                givenProduct.getVariant()))
                .thenReturn(false);
        when(responseFacade.countProductsByUserId(givenUser.getChatId())).thenReturn(amountOfProducts);

        //then
        linkResponse = new LinkResponse(responseFacade, givenUser, scraper, quickSender);
        assertThrows(InvalidLinkException.class, () -> linkResponse.response());

        verify(quickSender, times(1)).messageWithReturn(givenUser.getChatId(), Messages.other("processing"), false);
        verify(quickSender, times(1)).chatAction(givenUser.getChatId(), Action.TYPING);
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(responseFacade, times(0)).saveProduct(givenProduct, givenUser.getChatId());
        verify(quickSender, times(0)).message(givenUser.getChatId(), Messages.other("productAdded"), false);
    }

    @Test
    void response_expectNotThrow_MaxProductLimitButUserIsPremium() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setPremium(true);
        List<String> givenListOfVariants = List.of("-oneVariant One Variant");
        Product givenProduct = getDefaultProduct();
        Message givenMessage = getDefaultMessage();
        int amountOfProducts = 11;

        //when
        when(quickSender.messageWithReturn(givenUser.getChatId(), Messages.other("processing"), false)).thenReturn(givenMessage);
        when(scraper.getAllVariants(givenUser.getText())).thenReturn(givenListOfVariants);
        when(scraper.getProduct(givenUser.getText(), givenListOfVariants.get(0))).thenReturn(givenProduct);
        when(responseFacade.productExistsByUserIdAndLinkAndVariant(
                givenUser.getChatId(),
                givenUser.getText(),
                givenProduct.getVariant()))
                .thenReturn(false);
        when(responseFacade.countProductsByUserId(givenUser.getChatId())).thenReturn(amountOfProducts);

        //then
        linkResponse = new LinkResponse(responseFacade, givenUser, scraper, quickSender);
        assertDoesNotThrow(() -> linkResponse.response());

        verify(quickSender, times(1)).messageWithReturn(givenUser.getChatId(), Messages.other("processing"), false);
        verify(quickSender, times(1)).chatAction(givenUser.getChatId(), Action.TYPING);
    }


    private ActiveUser getDefaultActiveUser() {
        ActiveUser givenUser = new ActiveUser();
        givenUser.setUserId("123");
        givenUser.setChatId("123");
        givenUser.setMessageId(123);
        givenUser.setText("link");
        return givenUser;
    }

    private Product getDefaultProduct() {
        return Product.builder()
                .link("link")
                .variant("-oneVariant One Variant")
                .build();
    }

    private Message getDefaultMessage() {
        return Message.builder()
                .messageId(123)
                .build();
    }

    private SendMessage getSendMessageWithVariants(String chatId, List<String> variants, String requestId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(Messages.other("selectVariant"))
                .replyMarkup(InlineKeyboardMarkupFactory.getVariantChoice(variants, requestId))
                .build();
    }
}