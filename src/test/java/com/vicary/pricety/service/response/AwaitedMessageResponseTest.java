package com.vicary.pricety.service.response;

import com.vicary.pricety.api_telegram.service.QuickSender;
import com.vicary.pricety.exception.IllegalInputException;
import com.vicary.pricety.messages.Messages;
import com.vicary.pricety.model.Product;
import com.vicary.pricety.model.UserDTO;
import com.vicary.pricety.service.response.inline_markup.InlineKeyboardMarkupFactory;
import com.vicary.pricety.thread_local.ActiveLanguage;
import com.vicary.pricety.thread_local.ActiveUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AwaitedMessageResponseTest {

    private static AwaitedMessageResponse awaitedMessageResponse;
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
    }

    @Test
    void response_setNick_ValidNick() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("vicary");
        String givenRequest = "-setNick";

        //when
        when(responseFacade.getAwaitedMessageRequest(givenUser.getChatId())).thenReturn(givenRequest);
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, givenUser, quickSender);
        awaitedMessageResponse.response();

        //then
        verify(responseFacade, times(1)).updateUserNick(givenUser.getUserId(), givenUser.getText());
        verify(responseFacade, times(1)).deleteAwaitedMessage(givenUser.getChatId());
        verify(quickSender, times(1)).popupMessage(givenUser.getChatId(), Messages.other("setNickSuccess"));
        verify(quickSender, times(1)).message(givenUser.getChatId(), Messages.command("start").formatted(" " + givenUser.getText()), true);
    }

    @Test
    void response_setNick_InvalidNick() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("invalid!");
        String givenRequest = "-setNick";

        //when
        doThrow(new IllegalInputException()).when(responseFacade).updateUserNick(givenUser.getUserId(), givenUser.getText());
        when(responseFacade.getAwaitedMessageRequest(givenUser.getChatId())).thenReturn(givenRequest);
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, givenUser, quickSender);

        //then
        assertThrows(IllegalInputException.class, () -> awaitedMessageResponse.response());
        verify(responseFacade, times(1)).updateUserNick(givenUser.getUserId(), givenUser.getText());
        verify(responseFacade, times(0)).deleteAwaitedMessage(givenUser.getChatId());
        verify(quickSender, times(0)).popupMessage(givenUser.getChatId(), Messages.other("setNickSuccess"));
        verify(quickSender, times(0)).message(givenUser.getChatId(), Messages.command("start").formatted(" " + givenUser.getText()), true);
    }

    @Test
    void response_editPriceAlert_validPriceAlert() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("200 zł");

        long givenProductId = 200;
        String givenRequest = "-edit " + givenProductId;
        Product givenProduct = getDefaultProduct();
        givenProduct.setPrice(300);

        //when
        when(responseFacade.getAwaitedMessageRequest(givenUser.getChatId())).thenReturn(givenRequest);
        when(responseFacade.getProductById(givenProductId)).thenReturn(givenProduct);
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, givenUser, quickSender);
        awaitedMessageResponse.response();

        //then
        verify(responseFacade, times(1)).deleteAwaitedMessage(givenUser.getChatId());
        verify(responseFacade, times(1)).getProductById(givenProductId);
        verify(responseFacade, times(1)).updateProductPriceAlert(givenProductId, "200.00");
        verify(quickSender, times(1)).popupMessage(givenUser.getChatId(), Messages.other("priceAlertUpdated"));
        verify(quickSender, times(1)).inlineMarkup(
                eq(givenUser.getChatId()),
                anyString(),
                eq(InlineKeyboardMarkupFactory.getMenu()),
                eq(true));
    }

    @Test
    void response_editPriceAlert_priceAlertIsHigherThanActualPrice() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("200 zł");

        long givenProductId = 200;
        String givenRequest = "-edit " + givenProductId;
        Product givenProduct = getDefaultProduct();
        givenProduct.setPrice(100);

        //when
        when(responseFacade.getAwaitedMessageRequest(givenUser.getChatId())).thenReturn(givenRequest);
        when(responseFacade.getProductById(givenProductId)).thenReturn(givenProduct);
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, givenUser, quickSender);

        //then
        assertThrows(IllegalInputException.class, () -> awaitedMessageResponse.response());
        verify(responseFacade, times(1)).deleteAwaitedMessage(givenUser.getChatId());
        verify(responseFacade, times(1)).getProductById(givenProductId);
        verify(responseFacade, times(0)).updateProductPriceAlert(givenProductId, "200.00");
        verify(quickSender, times(0)).popupMessage(givenUser.getChatId(), Messages.other("priceAlertUpdated"));
        verify(quickSender, times(0)).inlineMarkup(
                eq(givenUser.getChatId()),
                anyString(),
                eq(InlineKeyboardMarkupFactory.getMenu()),
                eq(true));
    }

    @Test
    void response_UpdateUserEmail_validEmail() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("example@email.com");
        String givenRequest = "-setEmail";

        //when
        when(responseFacade.getAwaitedMessageRequest(givenUser.getChatId())).thenReturn(givenRequest);
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, givenUser, quickSender);
        awaitedMessageResponse.response();

        //then
        verify(responseFacade, times(1)).deleteAwaitedMessage(givenUser.getChatId());
        verify(responseFacade, times(1)).updateEmailAndSendToken(givenUser.getUserId(), givenUser.getText());
        verify(quickSender, times(1)).message(givenUser.getChatId(), Messages.other("verificationCodeMessage"), true);
    }

    @Test
    void response_updateUserEmail_EmailIsDeleteFirstVariant() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("delete");
        String givenRequest = "-setEmail";

        //when
        when(responseFacade.getAwaitedMessageRequest(givenUser.getChatId())).thenReturn(givenRequest);
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, givenUser, quickSender);
        awaitedMessageResponse.response();

        //then
        verify(responseFacade, times(1)).deleteAwaitedMessage(givenUser.getChatId());
        verify(responseFacade, times(1)).deleteEmailById(givenUser.getUserId());
        verify(quickSender, times(1)).popupMessage(givenUser.getChatId(), Messages.other("emailDeleted"));
        verify(quickSender, times(1)).inlineMarkup(
                eq(givenUser.getChatId()),
                anyString(),
                eq(InlineKeyboardMarkupFactory.getMenu()),
                eq(true));
        verify(responseFacade, times(0)).updateEmailAndSendToken(givenUser.getUserId(), givenUser.getText());
        verify(quickSender, times(0)).message(givenUser.getChatId(), Messages.other("verificationCodeMessage"), true);
    }

    @Test
    void response_updateUserEmail_EmailIsDeleteSecondVariant() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("DELETE");
        String givenRequest = "-setEmail";

        //when
        when(responseFacade.getAwaitedMessageRequest(givenUser.getChatId())).thenReturn(givenRequest);
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, givenUser, quickSender);
        awaitedMessageResponse.response();

        //then
        verify(responseFacade, times(1)).deleteAwaitedMessage(givenUser.getChatId());
        verify(responseFacade, times(1)).deleteEmailById(givenUser.getUserId());
        verify(quickSender, times(1)).popupMessage(givenUser.getChatId(), Messages.other("emailDeleted"));
        verify(quickSender, times(1)).inlineMarkup(
                eq(givenUser.getChatId()),
                anyString(),
                eq(InlineKeyboardMarkupFactory.getMenu()),
                eq(true));
        verify(responseFacade, times(0)).updateEmailAndSendToken(givenUser.getUserId(), givenUser.getText());
        verify(quickSender, times(0)).message(givenUser.getChatId(), Messages.other("verificationCodeMessage"), true);
    }

    @Test
    void response_updateUserEmail_EmailIsInvalid() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("example@email");
        String givenRequest = "-setEmail";

        //when
        when(responseFacade.getAwaitedMessageRequest(givenUser.getChatId())).thenReturn(givenRequest);
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, givenUser, quickSender);

        //then
        assertThrows(IllegalInputException.class, () -> awaitedMessageResponse.response());
        verify(responseFacade, times(1)).deleteAwaitedMessage(givenUser.getChatId());
        verify(responseFacade, times(0)).updateEmailAndSendToken(givenUser.getUserId(), givenUser.getText());
        verify(quickSender, times(0)).message(givenUser.getChatId(), Messages.other("verificationCodeMessage"), true);
    }

    @Test
    void response_updateUserEmail_EmailIsTheSame() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        givenUser.setText("example@email.com");
        givenUser.setEmail("example@email.com");
        String givenRequest = "-setEmail";

        //when
        when(responseFacade.getAwaitedMessageRequest(givenUser.getChatId())).thenReturn(givenRequest);
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, givenUser, quickSender);

        //then
        assertThrows(IllegalInputException.class, () -> awaitedMessageResponse.response());
        verify(responseFacade, times(1)).deleteAwaitedMessage(givenUser.getChatId());
        verify(responseFacade, times(0)).updateEmailAndSendToken(givenUser.getUserId(), givenUser.getText());
        verify(quickSender, times(0)).message(givenUser.getChatId(), Messages.other("verificationCodeMessage"), true);
    }

    @Test
    void getPriceAlertFromText_expectEquals_normalText() {
        //given
        String givenText = "200";
        String expectedPriceAlert = "200.00";

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);
        String actualPriceAlert = awaitedMessageResponse.getPriceAlertFromText(givenText);

        //then
        assertEquals(expectedPriceAlert, actualPriceAlert);
    }

    @Test
    void getPriceAlertFromText_expectEquals_normalTextDecimalNumber() {
        //given
        String givenText = "215.23";
        String expectedPriceAlert = "215.23";

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);
        String actualPriceAlert = awaitedMessageResponse.getPriceAlertFromText(givenText);

        //then
        assertEquals(expectedPriceAlert, actualPriceAlert);
    }

    @Test
    void getPriceAlertFromText_expectEquals_RoundedDecimalNumber() {
        //given
        String givenText = "215.237";
        String expectedPriceAlert = "215.24";

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);
        String actualPriceAlert = awaitedMessageResponse.getPriceAlertFromText(givenText);

        //then
        assertEquals(expectedPriceAlert, actualPriceAlert);
    }

    @Test
    void getPriceAlertFromText_expectEquals_textWithZlFirstVariant() {
        //given
        String givenText = "200 zl";
        String expectedPriceAlert = "200.00";

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);
        String actualPriceAlert = awaitedMessageResponse.getPriceAlertFromText(givenText);

        //then
        assertEquals(expectedPriceAlert, actualPriceAlert);
    }

    @Test
    void getPriceAlertFromText_expectEquals_textWithZlSecondVariant() {
        //given
        String givenText = "200zl";
        String expectedPriceAlert = "200.00";

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);
        String actualPriceAlert = awaitedMessageResponse.getPriceAlertFromText(givenText);

        //then
        assertEquals(expectedPriceAlert, actualPriceAlert);
    }

    @Test
    void getPriceAlertFromText_expectEquals_textWithZlThirdVariant() {
        //given
        String givenText = "200 zł";
        String expectedPriceAlert = "200.00";

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);
        String actualPriceAlert = awaitedMessageResponse.getPriceAlertFromText(givenText);

        //then
        assertEquals(expectedPriceAlert, actualPriceAlert);
    }

    @Test
    void getPriceAlertFromText_expectEquals_textWithZlFourthVariant() {
        //given
        String givenText = "200zł";
        String expectedPriceAlert = "200.00";

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);
        String actualPriceAlert = awaitedMessageResponse.getPriceAlertFromText(givenText);

        //then
        assertEquals(expectedPriceAlert, actualPriceAlert);
    }

    @Test
    void getPriceAlertFromText_expectEquals_textWithComma() {
        //given
        String givenText = "200,55";
        String expectedPriceAlert = "200.55";

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);
        String actualPriceAlert = awaitedMessageResponse.getPriceAlertFromText(givenText);

        //then
        assertEquals(expectedPriceAlert, actualPriceAlert);
    }

    @Test
    void getPriceAlertFromText_expectEquals_textIsAUTOFirstVariant() {
        //given
        String givenText = "auto";
        String expectedPriceAlert = "AUTO";

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);
        String actualPriceAlert = awaitedMessageResponse.getPriceAlertFromText(givenText);

        //then
        assertEquals(expectedPriceAlert, actualPriceAlert);
    }

    @Test
    void getPriceAlertFromText_expectEquals_textIsAUTOSecondVariant() {
        //given
        String givenText = "AUTO";
        String expectedPriceAlert = "AUTO";

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);
        String actualPriceAlert = awaitedMessageResponse.getPriceAlertFromText(givenText);

        //then
        assertEquals(expectedPriceAlert, actualPriceAlert);
    }

    @Test
    void getPriceAlertFromText_expectEquals_textIsOffFirstVariant() {
        //given
        String givenText = "off";
        String expectedPriceAlert = "OFF";

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);
        String actualPriceAlert = awaitedMessageResponse.getPriceAlertFromText(givenText);

        //then
        assertEquals(expectedPriceAlert, actualPriceAlert);
    }

    @Test
    void getPriceAlertFromText_expectEquals_textIsOffSecondVariant() {
        //given
        String givenText = "OFF";
        String expectedPriceAlert = "OFF";

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);
        String actualPriceAlert = awaitedMessageResponse.getPriceAlertFromText(givenText);

        //then
        assertEquals(expectedPriceAlert, actualPriceAlert);
    }

    @Test
    void getPriceAlertFromText_expectThrow_textIsZero() {
        //given
        String givenText = "0";

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);

        //then
        assertThrows(IllegalInputException.class, () -> awaitedMessageResponse.getPriceAlertFromText(givenText));
    }

    @Test
    void getPriceAlertFromText_expectThrow_textIsBelowZero() {
        //given
        String givenText = "-300";

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);

        //then
        assertThrows(IllegalInputException.class, () -> awaitedMessageResponse.getPriceAlertFromText(givenText));
    }

    @Test
    void getPriceAlertFromText_expectThrow_NoPriceInText() {
        //given
        String givenText = "text";

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);

        //then
        assertThrows(IllegalInputException.class, () -> awaitedMessageResponse.getPriceAlertFromText(givenText));
    }

    @Test
    void getPriceAlertFromText_expectThrow_TextWithDoubleComma() {
        //given
        String givenText = "200,,31";

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);

        //then
        assertThrows(IllegalInputException.class, () -> awaitedMessageResponse.getPriceAlertFromText(givenText));
    }

    @Test
    void isPriceAlertHigherThanPrice_expectFalse_PriceAlertIsAuto() {
        //given
        String givenPriceAlert = "AUTO";
        double givenPrice = 200.50;

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);

        //then
        assertFalse(awaitedMessageResponse.isPriceAlertHigherThanPrice(givenPriceAlert, givenPrice));
    }

    @Test
    void isPriceAlertHigherThanPrice_expectFalse_PriceAlertIsOFF() {
        //given
        String givenPriceAlert = "OFF";
        double givenPrice = 200.50;

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);

        //then
        assertFalse(awaitedMessageResponse.isPriceAlertHigherThanPrice(givenPriceAlert, givenPrice));
    }

    @Test
    void isPriceAlertHigherThanPrice_expectFalse_PriceAlertIsLowerThanPrice() {
        //given
        String givenPriceAlert = "100.50";
        double givenPrice = 200.50;

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);

        //then
        assertFalse(awaitedMessageResponse.isPriceAlertHigherThanPrice(givenPriceAlert, givenPrice));
    }

    @Test
    void isPriceAlertHigherThanPrice_expectTrue_PriceAlertIsHigherThanPrice() {
        //given
        String givenPriceAlert = "300.50";
        double givenPrice = 200.50;

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);

        //then
        assertTrue(awaitedMessageResponse.isPriceAlertHigherThanPrice(givenPriceAlert, givenPrice));
    }

    @Test
    void isPriceAlertHigherThanPrice_expectTrue_PriceAlertIsEqualToPrice() {
        //given
        String givenPriceAlert = "200.50";
        double givenPrice = 200.50;

        //when
        awaitedMessageResponse = new AwaitedMessageResponse(responseFacade, getDefaultActiveUser(), quickSender);

        //then
        assertTrue(awaitedMessageResponse.isPriceAlertHigherThanPrice(givenPriceAlert, givenPrice));
    }


    private ActiveUser getDefaultActiveUser() {
        ActiveUser givenUser = new ActiveUser();
        givenUser.setUserId(123);
        givenUser.setTelegramId("123");
        givenUser.setChatId("123");
        givenUser.setMessageId(123);
        return givenUser;
    }

    private Product getDefaultProduct() {
        return Product.builder()
                .variant("M")
                .link("link")
                .userDTO(UserDTO.builder().email("email@email.pl").build())
                .priceAlert("100.00")
                .build();
    }
}