package com.vicary.pricety.service.response.inline_markup;

import com.vicary.pricety.api_telegram.api_object.Action;
import com.vicary.pricety.api_telegram.api_object.message.Message;
import com.vicary.pricety.api_telegram.service.QuickSender;
import com.vicary.pricety.entity.LinkRequestEntity;
import com.vicary.pricety.exception.ChartGeneratorException;
import com.vicary.pricety.exception.InvalidLinkException;
import com.vicary.pricety.format.MarkdownV2;
import com.vicary.pricety.messages.Messages;
import com.vicary.pricety.model.Product;
import com.vicary.pricety.scraper.Scraper;
import com.vicary.pricety.service.chart.ProductChartGenerator;
import com.vicary.pricety.service.dto.ProductHistoryDTO;
import com.vicary.pricety.service.response.ResponseFacade;
import com.vicary.pricety.thread_local.ActiveLanguage;
import com.vicary.pricety.thread_local.ActiveUser;
import com.vicary.pricety.utils.TerminalExecutor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InlineMarkupResponseTest {
    private InlineMarkupResponse inlineMarkupResponse;
    private ResponseFacade responseFacade;
    private QuickSender quickSender;
    private Scraper scraper;

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
    void addProduct_everythingValid() {
        //given
        String givenLink = "https://www.zalando.pl/123";
        String givenRequestId = "123456789";
        String givenVariant = "-oneVariant One Size";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("-l " + givenRequestId + " " + givenVariant);
        Message givenMessage = getDefaultMessage();
        Product givenProduct = getDefaultProduct();
        LinkRequestEntity givenLinkRequest = getDefaultLinkRequestEntity();

        //when
        when(quickSender.messageWithReturn(givenUser.getChatId(), Messages.other("adding"), false)).thenReturn(givenMessage);
        when(scraper.getProduct(givenLink, givenVariant)).thenReturn(givenProduct);
        when(responseFacade.productExistsByUserIdAndLinkAndVariant(
                givenUser.getUserId(),
                givenProduct.getLink(),
                givenProduct.getVariant()))
                .thenReturn(false);
        when(responseFacade.countProductsByUserId(givenUser.getUserId())).thenReturn(0);
        when(responseFacade.getLinkRequestByIdAndDelete(givenRequestId)).thenReturn(givenLinkRequest);
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.addProduct(givenLink, givenVariant, scraper);

        //then
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenMessage.getMessageId());
        verify(quickSender, times(1)).messageWithReturn(givenUser.getChatId(), Messages.other("adding"), false);
        verify(quickSender, times(1)).chatAction(givenUser.getChatId(), Action.TYPING);
        verify(responseFacade, times(1)).productExistsByUserIdAndLinkAndVariant(anyLong(), anyString(), anyString());
        verify(responseFacade, times(1)).saveProduct(givenProduct, givenUser.getUserId());
        verify(quickSender, times(1)).message(givenUser.getChatId(), Messages.other("productAdded"), false);
    }

    @Test
    void addProduct_expectThrow_UserTryToAddSameProduct() {
        //given
        String givenLink = "https://www.zalando.pl/123";
        String givenRequestId = "123456789";
        String givenVariant = "-oneVariant One Size";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("-l " + givenRequestId + " " + givenVariant);
        Message givenMessage = getDefaultMessage();
        Product givenProduct = getDefaultProduct();
        LinkRequestEntity givenLinkRequest = getDefaultLinkRequestEntity();

        //when
        when(quickSender.messageWithReturn(givenUser.getChatId(), Messages.other("adding"), false)).thenReturn(givenMessage);
        when(scraper.getProduct(givenLink, givenVariant)).thenReturn(givenProduct);
        when(responseFacade.productExistsByUserIdAndLinkAndVariant(
                givenUser.getUserId(),
                givenProduct.getLink(),
                givenProduct.getVariant()))
                .thenReturn(false);
        when(responseFacade.productExistsByUserIdAndLinkAndVariant(givenUser.getUserId(), givenProduct.getLink(), givenProduct.getVariant())).thenReturn(true);
        when(responseFacade.countProductsByUserId(givenUser.getUserId())).thenReturn(0);
        when(responseFacade.getLinkRequestByIdAndDelete(givenRequestId)).thenReturn(givenLinkRequest);

        //then
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        assertThrows(InvalidLinkException.class, () -> inlineMarkupResponse.addProduct(givenLink, givenVariant, scraper));

        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenMessage.getMessageId());
        verify(quickSender, times(1)).messageWithReturn(givenUser.getChatId(), Messages.other("adding"), false);
        verify(quickSender, times(1)).chatAction(givenUser.getChatId(), Action.TYPING);
        verify(responseFacade, times(1)).productExistsByUserIdAndLinkAndVariant(anyLong(), anyString(), anyString());
        verify(responseFacade, times(0)).countProductsByUserId(givenUser.getUserId());
        verify(responseFacade, times(0)).saveProduct(givenProduct, givenUser.getUserId());
        verify(quickSender, times(0)).message(givenUser.getChatId(), Messages.other("productAdded"), false);
    }

    @Test
    void addProduct_expectNotThrow_UserHaveMaxLimitButHeIsPremium() {
        //given
        String givenLink = "https://www.zalando.pl/123";
        String givenRequestId = "123456789";
        String givenVariant = "-oneVariant One Size";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setPremium(true);
        givenUser.setText("-l " + givenRequestId + " " + givenVariant);
        Message givenMessage = getDefaultMessage();
        Product givenProduct = getDefaultProduct();
        LinkRequestEntity givenLinkRequest = getDefaultLinkRequestEntity();

        //when
        when(quickSender.messageWithReturn(givenUser.getChatId(), Messages.other("adding"), false)).thenReturn(givenMessage);
        when(scraper.getProduct(givenLink, givenVariant)).thenReturn(givenProduct);
        when(responseFacade.productExistsByUserIdAndLinkAndVariant(
                givenUser.getUserId(),
                givenProduct.getLink(),
                givenProduct.getVariant()))
                .thenReturn(false);
        when(responseFacade.productExistsByUserIdAndLinkAndVariant(givenUser.getUserId(), givenProduct.getLink(), givenProduct.getVariant())).thenReturn(false);
        when(responseFacade.countProductsByUserId(givenUser.getUserId())).thenReturn(11);
        when(responseFacade.getLinkRequestByIdAndDelete(givenRequestId)).thenReturn(givenLinkRequest);

        //then
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        assertDoesNotThrow(() -> inlineMarkupResponse.addProduct(givenLink, givenVariant, scraper));

        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenMessage.getMessageId());
        verify(quickSender, times(1)).messageWithReturn(givenUser.getChatId(), Messages.other("adding"), false);
        verify(quickSender, times(1)).chatAction(givenUser.getChatId(), Action.TYPING);
        verify(responseFacade, times(1)).productExistsByUserIdAndLinkAndVariant(anyLong(), anyString(), anyString());
        verify(responseFacade, times(1)).saveProduct(givenProduct, givenUser.getUserId());
        verify(quickSender, times(1)).message(givenUser.getChatId(), Messages.other("productAdded"), false);
    }


    @Test
    void displayAllProducts_expectDoesNotThrow_ValidList() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        AllProductDisplay givenDisplayer = mock(AllProductDisplay.class);
        List<Product> givenProducts = getDefaultListOfProducts();

        //when
        when(responseFacade.getAllProductsByUserId(givenUser.getUserId())).thenReturn(givenProducts);

        //then
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.displayProducts(givenDisplayer);

        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(givenDisplayer, times(1)).display();
    }

    @Test
    void displayAllProducts_expectReturn_EmptyList() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        AllProductDisplay givenDisplayer = mock(AllProductDisplay.class);
        List<Product> givenProducts = Collections.emptyList();

        //when
        when(responseFacade.getAllProductsByUserId(givenUser.getUserId())).thenReturn(givenProducts);

        //then
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.displayProducts(givenDisplayer);

        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(quickSender, times(1)).popupMessage(givenUser.getChatId(), Messages.other("dontHaveProduct"));
        verify(quickSender, times(1)).inlineMarkup(
                eq(givenUser.getChatId()),
                anyString(),
                eq(InlineKeyboardMarkupFactory.getMenu()),
                eq(true));
        verify(givenDisplayer, times(0)).display();
    }

    @Test
    void displayEditProducts_expectDoesNotThrow_ValidList() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        EditProductDisplay givenDisplayer = mock(EditProductDisplay.class);
        List<Product> givenProducts = getDefaultListOfProducts();

        //when
        when(responseFacade.getAllProductsByUserId(givenUser.getUserId())).thenReturn(givenProducts);

        //then
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.displayProducts(givenDisplayer);

        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(givenDisplayer, times(1)).display();
    }

    @Test
    void displayEditProducts_expectReturn_EmptyList() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        EditProductDisplay givenDisplayer = mock(EditProductDisplay.class);
        List<Product> givenProducts = Collections.emptyList();

        //when
        when(responseFacade.getAllProductsByUserId(givenUser.getUserId())).thenReturn(givenProducts);

        //then
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.displayProducts(givenDisplayer);

        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(quickSender, times(1)).popupMessage(givenUser.getChatId(), Messages.other("dontHaveProduct"));
        verify(quickSender, times(1)).inlineMarkup(
                eq(givenUser.getChatId()),
                anyString(),
                eq(InlineKeyboardMarkupFactory.getMenu()),
                eq(true));
        verify(givenDisplayer, times(0)).display();
    }

    @Test
    void displayDeleteProducts_expectDoesNotThrow_ValidList() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        EditProductDisplay givenDisplayer = mock(EditProductDisplay.class);
        List<Product> givenProducts = getDefaultListOfProducts();

        //when
        when(responseFacade.getAllProductsByUserId(givenUser.getUserId())).thenReturn(givenProducts);

        //then
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.displayProducts(givenDisplayer);

        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(givenDisplayer, times(1)).display();
    }

    @Test
    void displayDeleteProducts_expectReturn_EmptyList() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        EditProductDisplay givenDisplayer = mock(EditProductDisplay.class);
        List<Product> givenProducts = Collections.emptyList();

        //when
        when(responseFacade.getAllProductsByUserId(givenUser.getUserId())).thenReturn(givenProducts);

        //then
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.displayProducts(givenDisplayer);

        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(quickSender, times(1)).popupMessage(givenUser.getChatId(), Messages.other("dontHaveProduct"));
        verify(quickSender, times(1)).inlineMarkup(
                eq(givenUser.getChatId()),
                anyString(),
                eq(InlineKeyboardMarkupFactory.getMenu()),
                eq(true));
        verify(givenDisplayer, times(0)).display();
    }

    @Test
    void displayProducts_expectNotThrow_JustDisplay() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();

        //when
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.displayAddProduct();

        //then
        verify(quickSender, times(1)).popupMessage(givenUser.getChatId(), Messages.addProduct("justPaste"), 3000);
    }

    @Test
    void displayNotification_expectNotThrow_JustDisplay() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();

        //when
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.displayNotification();

        //then
        verify(quickSender, times(1)).inlineMarkup(
                givenUser.getChatId(),
                Messages.notifications(givenUser),
                InlineKeyboardMarkupFactory.getNotification(givenUser),
                true);
    }

    @Test
    void deleteProduct_expectNotThrow_JustDelete() {
        //given
        long givenProductId = 200;
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("-delete " + givenProductId);

        //when
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.deleteProduct();

        //then
        verify(responseFacade, times(1)).deleteProduct(givenProductId);
        verify(quickSender, times(1)).popupMessage(givenUser.getChatId(), Messages.other("deleted"));
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(quickSender, times(1)).inlineMarkup(
                eq(givenUser.getChatId()),
                anyString(),
                eq(InlineKeyboardMarkupFactory.getMenu()),
                eq(true));
    }

    @Test
    void displayEditPriceAlertMessage_expectNotThrow_JustDisplay() {
        //given
        long givenProductId = 200;
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("-edit " + givenProductId);

        //when
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.displayEditPriceAlertMessage();

        //then
        verify(responseFacade, times(1)).createAndSaveAwaitedMessage(givenUser.getChatId(), givenUser.getText());
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(quickSender, times(1)).message(givenUser.getChatId(), Messages.other("sendNewAlert"), true);
    }

    @Test
    void response_displayDeleteYesOrNo_JustDisplay() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("-deleteAll");

        //when
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.response();

        //then
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(quickSender, times(1)).inlineMarkup(
                givenUser.getChatId(),
                Messages.deleteProduct("areYouSure"),
                InlineKeyboardMarkupFactory.getDeleteYesOrNo());
    }

    @Test
    void response_deleteAllProduct_UserClickYes() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("-deleteAllYes");

        //when
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.response();

        //then
        verify(responseFacade, times(1)).deleteAllProductsByUserId(givenUser.getUserId());
        verify(quickSender, times(1)).popupMessage(givenUser.getChatId(), Messages.other("allDeleted"));
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(quickSender, times(1)).inlineMarkup(
                eq(givenUser.getChatId()),
                anyString(),
                eq(InlineKeyboardMarkupFactory.getMenu()),
                eq(true));
    }

    @Test
    void response_deleteAllProduct_UserClicksNo() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("-deleteAllNo");

        //when
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.response();

        //then
        verify(responseFacade, times(0)).deleteAllProductsByUserId(givenUser.getUserId());
        verify(quickSender, times(0)).popupMessage(givenUser.getChatId(), Messages.other("allDeleted"));
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(quickSender, times(1)).inlineMarkup(
                eq(givenUser.getChatId()),
                anyString(),
                eq(InlineKeyboardMarkupFactory.getMenu()),
                eq(true));
    }

    @Test
    void response_updateNotifyByEmail_EnableEmailEverythingIsValid() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("-enableEmail");
        givenUser.setEmail("email@email.pl");
        givenUser.setVerifiedEmail(true);

        //when
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.response();

        //then
        assertTrue(givenUser.isNotifyByEmail());
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(responseFacade, times(1)).updateNotifyByEmailByUserId(givenUser.getUserId(), true);
        verify(quickSender, times(1)).inlineMarkup(
                givenUser.getChatId(),
                Messages.notifications(givenUser),
                InlineKeyboardMarkupFactory.getNotification(givenUser),
                true);
    }

    @Test
    void response_updateNotifyByEmail_DisableEmailEverythingIsValid() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("-disableEmail");
        givenUser.setEmail("email@email.pl");
        givenUser.setVerifiedEmail(true);
        givenUser.setNotifyByEmail(true);

        //when
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.response();

        //then
        assertFalse(givenUser.isNotifyByEmail());
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(responseFacade, times(1)).updateNotifyByEmailByUserId(givenUser.getUserId(), false);
        verify(quickSender, times(1)).inlineMarkup(
                givenUser.getChatId(),
                Messages.notifications(givenUser),
                InlineKeyboardMarkupFactory.getNotification(givenUser),
                true);
    }

    @Test
    void response_updateNotifyByEmail_EnableEmailButUserDontHaveEmail() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("-enableEmail");
        givenUser.setEmail(null);

        //when
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.response();

        //then
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(quickSender, times(1)).popupMessage(givenUser.getChatId(), Messages.other("setEmailFirst"));
        verify(quickSender, times(1)).inlineMarkup(
                givenUser.getChatId(),
                Messages.notifications(givenUser),
                InlineKeyboardMarkupFactory.getNotification(givenUser),
                true);
        verify(responseFacade, times(0)).updateNotifyByEmailByUserId(anyLong(), anyBoolean());
    }

    @Test
    void response_updateNotifyByEmail_EnableEmailButUserDontHaveVerifiedEmail() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("-enableEmail");
        givenUser.setEmail("email@email.com");
        givenUser.setVerifiedEmail(false);

        //when
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.response();

        //then
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(quickSender, times(1)).popupMessage(givenUser.getChatId(), Messages.other("verifyEmailFirst"));
        verify(quickSender, times(1)).inlineMarkup(
                givenUser.getChatId(),
                Messages.notifications(givenUser),
                InlineKeyboardMarkupFactory.getNotification(givenUser),
                true);
        verify(responseFacade, times(0)).updateNotifyByEmailByUserId(anyLong(), anyBoolean());
    }

    @Test
    void response_displaySetEmailMessage_JustDisplay() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("-setEmail");

        //when
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.response();

        //then
        verify(responseFacade, times(1)).createAndSaveAwaitedMessage(givenUser.getChatId(), givenUser.getText());
        verify(quickSender, times(1)).message(givenUser.getChatId(), Messages.other("sendNewEmail"), true);
    }

    @Test
    void response_updateUserLanguage_JustUpdate() {
        //given
        String givenLanguage = "pl";
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("-lang " + givenLanguage);

        //when
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.response();

        //then
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(responseFacade, times(1)).updateUserLanguage(givenUser.getUserId(), givenLanguage);
        verify(quickSender, times(1)).inlineMarkup(
                eq(givenUser.getChatId()),
                anyString(),
                eq(InlineKeyboardMarkupFactory.getMenu()),
                eq(true));
    }

    @Test
    void response_deletePreviousMessage_JustExit() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("-exit");

        //when
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.response();

        //then
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
    }

    @Test
    void response_backToMenu_JustBack() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("-back");

        //when
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.response();

        //then
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(quickSender, times(1)).inlineMarkup(
                eq(givenUser.getChatId()),
                anyString(),
                eq(InlineKeyboardMarkupFactory.getMenu()),
                eq(true));
    }

    @Test
    void generateProduct_JustGenerate() {
        //given
        File givenFile = getDefaultFile();
        Product givenProduct = getDefaultProduct();
        List<ProductHistoryDTO> givenDTOs = Collections.emptyList();
        ProductChartGenerator givenGenerator = mock(ProductChartGenerator.class);
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("-generate 100");

        //when
        when(responseFacade.getProductById(100L)).thenReturn(givenProduct);
        when(responseFacade.getAllReducedProductHistory(100L)).thenReturn(givenDTOs);
        when(quickSender.messageWithReturn(givenUser.getChatId(), MarkdownV2.applyWithManualBoldAndItalic(Messages.generateProduct("generating")), true)).thenReturn(getDefaultMessage());
        when(givenGenerator.asPngHighResolution(givenProduct, givenDTOs)).thenReturn(givenFile);
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        inlineMarkupResponse.generateProduct(givenGenerator);

        //then
        verify(responseFacade, times(1)).getAllReducedProductHistory(100L);
        verify(responseFacade, times(1)).getProductById(100L);
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(quickSender, times(1)).messageWithReturn(givenUser.getChatId(), MarkdownV2.applyWithManualBoldAndItalic(Messages.generateProduct("generating")), true);
        verify(quickSender, times(1)).chatAction(givenUser.getChatId(), Action.UPLOAD_PHOTO);
        verify(givenGenerator, times(1)).asPngHighResolution(givenProduct, givenDTOs);
        verify(quickSender, times(1)).photo(givenUser.getChatId(), givenFile);
        verify(quickSender, times(1)).inlineMarkup(givenUser.getChatId(), MarkdownV2.applyWithManualBoldAndItalic(Messages.generateProduct("generated")), InlineKeyboardMarkupFactory.getBack(), true);
    }

    @Test
    void generateProduct_GeneratorThrowsException() {
        //given
        File givenFile = getDefaultFile();
        Product givenProduct = getDefaultProduct();
        List<ProductHistoryDTO> givenDTOs = Collections.emptyList();
        ProductChartGenerator givenGenerator = mock(ProductChartGenerator.class);
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("-generate 100");

        //when
        when(responseFacade.getProductById(100L)).thenReturn(givenProduct);
        when(responseFacade.getAllReducedProductHistory(100L)).thenReturn(givenDTOs);
        when(quickSender.messageWithReturn(givenUser.getChatId(), MarkdownV2.applyWithManualBoldAndItalic(Messages.generateProduct("generating")), true)).thenReturn(getDefaultMessage());
        doThrow(new ChartGeneratorException("throw", "throw")).when(givenGenerator).asPngHighResolution(givenProduct, givenDTOs);
        inlineMarkupResponse = new InlineMarkupResponse(responseFacade, givenUser, quickSender);
        TerminalExecutor.deleteFile(givenFile);

        //then
        assertThrows(IllegalArgumentException.class, () -> inlineMarkupResponse.generateProduct(givenGenerator));
        verify(responseFacade, times(1)).getAllReducedProductHistory(100L);
        verify(responseFacade, times(1)).getProductById(100L);
        verify(quickSender, times(1)).deleteMessage(givenUser.getChatId(), givenUser.getMessageId());
        verify(quickSender, times(1)).messageWithReturn(givenUser.getChatId(), MarkdownV2.applyWithManualBoldAndItalic(Messages.generateProduct("generating")), true);
        verify(quickSender, times(1)).chatAction(givenUser.getChatId(), Action.UPLOAD_PHOTO);
        verify(givenGenerator, times(1)).asPngHighResolution(givenProduct, givenDTOs);
        verify(quickSender, times(0)).photo(givenUser.getChatId(), givenFile);
        verify(quickSender, times(0)).inlineMarkup(givenUser.getChatId(), MarkdownV2.applyWithManualBoldAndItalic(Messages.generateProduct("generated")), InlineKeyboardMarkupFactory.getBack(), true);
    }

    private File getDefaultFile() {
        File file = new File("testFile");
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    private ActiveUser getDefaultActiveUser() {
        ActiveUser givenUser = new ActiveUser();
        givenUser.setUserId(123);
        givenUser.setTelegramId("123");
        givenUser.setChatId("123");
        givenUser.setMessageId(123);
        return givenUser;
    }

    private Message getDefaultMessage() {
        return Message.builder()
                .messageId(1234)
                .build();
    }

    private Product getDefaultProduct() {
        return Product.builder()
                .link("https://www.zalando.pl/123")
                .variant("-oneVariant One Variant")
                .build();
    }

    private LinkRequestEntity getDefaultLinkRequestEntity() {
        return LinkRequestEntity.builder()
                .requestId("123456789")
                .link("https://www.zalando.pl/123")
                .expiration(System.currentTimeMillis() + 50000)
                .build();
    }

    private LinkRequestEntity getExpiredLinkRequestEntity() {
        return LinkRequestEntity.builder()
                .requestId("123456789")
                .link("https://www.zalando.pl/123")
                .expiration(System.currentTimeMillis() - 50000)
                .build();
    }

    private List<Product> getDefaultListOfProducts() {
        return List.of(new Product());
    }
}