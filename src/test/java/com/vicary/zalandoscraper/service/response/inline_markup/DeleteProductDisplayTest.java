package com.vicary.zalandoscraper.service.response.inline_markup;

import com.vicary.zalandoscraper.api_telegram.api_object.ParseMode;
import com.vicary.zalandoscraper.api_telegram.api_object.keyboard.InlineKeyboardMarkup;
import com.vicary.zalandoscraper.api_telegram.api_request.send.SendMessage;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.service.response.InlineKeyboardMarkupFactory;
import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.mockito.Mockito.*;

class DeleteProductDisplayTest {

    private static DeleteProductDisplay deleteProductDisplay;
    private static QuickSender quickSender;

    @BeforeAll
    static void beforeAll() {
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages", Locale.of("en")));
    }

    @BeforeEach
    void beforeEach() {
        quickSender = mock(QuickSender.class);
    }

    @Test
    void display_expectEquals_DisplayFiveProducts() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        List<ProductDTO> givenDTOs = getFiveDifferentItems();
        InlineKeyboardMarkup expectedReplyMarkup = InlineKeyboardMarkupFactory.getProductChoice(givenDTOs, "-delete");
        String expectedMessage = getExpectedMessageForFiveProducts();
        SendMessage expectedSendMessage = SendMessage.builder()
                .chatId(givenUser.getChatId())
                .text(expectedMessage)
                .replyMarkup(expectedReplyMarkup)
                .parseMode(ParseMode.MarkdownV2)
                .disableWebPagePreview(true)
                .build();

        //when
        deleteProductDisplay = new DeleteProductDisplay(givenDTOs, givenUser.getChatId(), quickSender);
        deleteProductDisplay.display();

        //then
        verify(quickSender, times(1)).message(expectedSendMessage);
    }

    @Test
    void display_expectEquals_MoreThanTenProducts() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        List<ProductDTO> givenDTOs = getDefaultListOfDTOs(35);
        InlineKeyboardMarkup expectedReplyMarkup = InlineKeyboardMarkupFactory.getProductChoice(givenDTOs, "-delete");
        List<String> expectedMessages = getFiveExpectedMessagesForThirtyFiveProducts();

        SendMessage expectedLastSendMessage = SendMessage.builder()
                .chatId(givenUser.getChatId())
                .text(expectedMessages.get(4))
                .replyMarkup(expectedReplyMarkup)
                .parseMode(ParseMode.MarkdownV2)
                .disableWebPagePreview(true)
                .build();

        //when
        deleteProductDisplay = new DeleteProductDisplay(givenDTOs, givenUser.getChatId(), quickSender);
        deleteProductDisplay.display();

        //then
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedMessages.get(0), true);
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedMessages.get(1), true);
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedMessages.get(2), true);
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedMessages.get(3), true);
        verify(quickSender, times(1)).message(expectedLastSendMessage);
    }


    private ActiveUser getDefaultActiveUser() {
        ActiveUser givenUser = new ActiveUser();
        givenUser.setUserId("123");
        givenUser.setChatId("123");
        givenUser.setMessageId(123);
        givenUser.setText("v-123456789");
        return givenUser;
    }

    private List<ProductDTO> getFiveDifferentItems() {
        List<ProductDTO> DTOs = new ArrayList<>();

        DTOs.add(ProductDTO.builder()
                .name("example name")
                .description("example description")
                .link("https://www.link.pl/")
                .variant("example variant")
                .price(200.55)
                .priceAlert("150.00")
                .build());
        DTOs.add(ProductDTO.builder()
                .name("example name")
                .description("example description")
                .link("https://www.link.pl/")
                .variant("example variant")
                .price(0)
                .priceAlert("AUTO")
                .build());
        DTOs.add(ProductDTO.builder()
                .name("example name")
                .description("example description")
                .link("https://www.link.pl/")
                .variant("-oneVariant One Sizer")
                .price(200.55)
                .priceAlert("AUTO")
                .build());
        DTOs.add(ProductDTO.builder()
                .name("example name")
                .description("example description")
                .link("https://www.link.pl/")
                .variant("-oneVariant Unknown")
                .price(200.55)
                .priceAlert("OFF")
                .build());
        DTOs.add(ProductDTO.builder()
                .name("example name")
                .description("example description")
                .link("https://www.link.pl/")
                .variant("example variant")
                .price(200.55)
                .priceAlert("AUTO")
                .build());

        return DTOs;
    }

    private List<ProductDTO> getDefaultListOfDTOs(int howMuch) {
        List<ProductDTO> DTOs = new ArrayList<>();
        for (int i = 0; i < howMuch; i++) {
            DTOs.add(ProductDTO.builder()
                    .name("example name")
                    .description("example description")
                    .link("https://www.link.pl/")
                    .variant("example variant")
                    .price(200.55)
                    .priceAlert("AUTO")
                    .build());
        }
        return DTOs;
    }

    private String getExpectedMessageForFiveProducts() {
        return "*Products to delete 🗑️*\n" +
                "\n" +
                "*Product nr 1*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* 150\\.00 zł\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 2*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* Sold Out\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 3*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* One Sizer\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 4*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* Undefined\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* OFF\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 5*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\u200E \n" +
                "\n" +
                "\n" +
                "*Please select the item you want to delete*\\.";
    }

    private List<String> getFiveExpectedMessagesForThirtyFiveProducts() {
        String first = "*Products to delete 🗑️*\n" +
                "\n" +
                "*Product nr 1*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 2*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 3*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 4*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 5*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 6*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 7*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 8*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 9*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 10*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 11*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n";

        String second = "*Product nr 12*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 13*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 14*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 15*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 16*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 17*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 18*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 19*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 20*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 21*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n";

        String third = "*Product nr 22*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 23*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 24*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 25*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 26*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 27*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 28*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 29*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 30*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 31*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n";

        String fourth = "*Product nr 32*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 33*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 34*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO\n" +
                "\n" +
                "\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n" +
                "\n" +
                "*Product nr 35*\n" +
                "\n" +
                "*Name:* example name\n" +
                "*Description:* example description\n" +
                "*Link:* [link](https://www\\.link\\.pl/)\n" +
                "*Variant:* example variant\n" +
                "*Price:* 200\\.55 zł\n" +
                "*Price Alert:* AUTO";

        String fifth = "\u200E \n\n\n*Please select the item you want to delete*\\.";

        return List.of(first, second, third, fourth, fifth);
    }
}
















