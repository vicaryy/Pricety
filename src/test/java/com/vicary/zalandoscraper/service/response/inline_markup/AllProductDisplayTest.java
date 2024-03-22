package com.vicary.zalandoscraper.service.response.inline_markup;

import com.vicary.zalandoscraper.api_telegram.api_object.ParseMode;
import com.vicary.zalandoscraper.api_telegram.api_object.keyboard.InlineKeyboardMarkup;
import com.vicary.zalandoscraper.api_telegram.api_request.send.SendMessage;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.model.Product;
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

class AllProductDisplayTest {

    private static AllProductDisplay allProductDisplay;
    private static QuickSender quickSender;
    private static ThreadRandom threadRandom;

    @BeforeAll
    static void beforeAll() {
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages", Locale.of("en")));
    }

    @BeforeEach
    void beforeEach() {
        quickSender = mock(QuickSender.class);
        threadRandom = mock(ThreadRandom.class);
    }

    @Test
    void display_expectEquals_DisplayFiveProducts() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        List<Product> givenProducts = getFiveDifferentProducts();
        InlineKeyboardMarkup expectedReplyMarkup = InlineKeyboardMarkupFactory.getBack();
        String expectedMessage = getExpectedMessageForFiveProducts();
        SendMessage expectedSendMessage = SendMessage.builder()
                .chatId(givenUser.getChatId())
                .text(expectedMessage)
                .replyMarkup(expectedReplyMarkup)
                .parseMode(ParseMode.MarkdownV2)
                .disableWebPagePreview(true)
                .build();

        //when
        when(threadRandom.generate(1, 25)).thenReturn(5);
        allProductDisplay = new AllProductDisplay(givenProducts, givenUser.getChatId(), quickSender, threadRandom);
        allProductDisplay.display();

        //then
        verify(quickSender, times(1)).message(expectedSendMessage);
    }

    @Test
    void display_expectEquals_MoreThanTenProducts() {
        //given
        ActiveUser givenUser = getDefaultActiveUser();
        List<Product> givenProducts = getDefaultListOfProducts(35);
        InlineKeyboardMarkup expectedReplyMarkup = InlineKeyboardMarkupFactory.getBack();
        List<String> expectedMessages = getFiveExpectedMessagesForThirtyFiveProducts();

        SendMessage expectedLastSendMessage = SendMessage.builder()
                .chatId(givenUser.getChatId())
                .text(expectedMessages.get(4))
                .replyMarkup(expectedReplyMarkup)
                .parseMode(ParseMode.MarkdownV2)
                .disableWebPagePreview(true)
                .build();

        //when
        when(threadRandom.generate(1, 25)).thenReturn(5);
        allProductDisplay = new AllProductDisplay(givenProducts, givenUser.getChatId(), quickSender, threadRandom);
        allProductDisplay.display();

        //then
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedMessages.get(0), true);
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedMessages.get(1), true);
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedMessages.get(2), true);
        verify(quickSender, times(1)).message(givenUser.getChatId(), expectedMessages.get(3), true);
        verify(quickSender, times(1)).message(expectedLastSendMessage);
    }


    private ActiveUser getDefaultActiveUser() {
        ActiveUser givenUser = new ActiveUser();
        givenUser.setTelegramId("123");
        givenUser.setChatId("123");
        givenUser.setMessageId(123);
        givenUser.setText("v-123456789");
        return givenUser;
    }

    private List<Product> getFiveDifferentProducts() {
        List<Product> products = new ArrayList<>();

        products.add(Product.builder()
                .name("example name")
                .description("example description")
                .link("https://www.link.pl/")
                .variant("example variant")
                .serviceName("zalando.pl")
                .currency("zł")
                .price(200.55)
                .priceAlert("150.00")
                .build());
        products.add(Product.builder()
                .name("example name")
                .description("example description")
                .link("https://www.link.pl/")
                .variant("example variant")
                .serviceName("zalando.ch")
                .currency("CHF")
                .price(0)
                .priceAlert("AUTO")
                .build());
        products.add(Product.builder()
                .name("example name")
                .description("example description")
                .link("https://www.link.pl/")
                .variant("-oneVariant One Sizer")
                .serviceName("hebe.pl")
                .currency("zł")
                .price(200.55)
                .priceAlert("AUTO")
                .build());
        products.add(Product.builder()
                .name("example name")
                .description("example description")
                .link("https://www.link.pl/")
                .variant("-oneVariant Unknown")
                .serviceName("nike.pl")
                .currency("zł")
                .price(200.55)
                .priceAlert("OFF")
                .build());
        products.add(Product.builder()
                .name("example name")
                .description("example description")
                .link("https://www.link.pl/")
                .variant("example variant")
                .serviceName("zalando.de")
                .currency("€")
                .price(200.55)
                .priceAlert("AUTO")
                .build());

        return products;
    }

    private List<Product> getDefaultListOfProducts(int howMuch) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < howMuch; i++) {
            products.add(Product.builder()
                    .name("example name")
                    .description("example description")
                    .link("https://www.link.pl/")
                    .variant("example variant")
                    .serviceName("zalando.pl")
                    .currency("zł")
                    .price(200.55)
                    .priceAlert("AUTO")
                    .build());
        }
        return products;
    }

    private String getExpectedMessageForFiveProducts() {
        return """
                *That's your products \uD83D\uDCDD*

                *Product nr 1*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* 150\\.00 zł

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 2*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․ch](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* Sold Out
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 3*

                *Name:* example name
                *Description:* example description
                *Link:* [hebe․pl](https://www\\.link\\.pl/)
                *Variant:* One Sizer
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 4*

                *Name:* example name
                *Description:* example description
                *Link:* [nike․pl](https://www\\.link\\.pl/)
                *Variant:* Undefined
                *Price:* 200\\.55 zł
                *Price Alert:* OFF

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 5*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․de](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 €
                *Price Alert:* AUTO


                *Did you know? \uD83D\uDCA1*
                The Great Wall of China is not visible from the Moon without aid, contrary to popular belief \uD83D\uDC80""";
    }

    private List<String> getFiveExpectedMessagesForThirtyFiveProducts() {
        String first = """
                *That's your products \uD83D\uDCDD*

                *Product nr 1*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 2*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 3*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 4*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 5*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 6*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 7*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 8*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 9*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 10*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 11*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                """;

        String second = """
                *Product nr 12*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 13*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 14*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 15*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 16*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 17*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 18*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 19*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 20*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 21*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                """;

        String third = """
                *Product nr 22*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 23*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 24*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 25*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 26*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 27*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 28*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 29*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 30*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 31*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                """;

        String fourth = """
                *Product nr 32*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 33*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 34*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO

                \\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-

                *Product nr 35*

                *Name:* example name
                *Description:* example description
                *Link:* [zalando․pl](https://www\\.link\\.pl/)
                *Variant:* example variant
                *Price:* 200\\.55 zł
                *Price Alert:* AUTO""";

        String fifth = """



                *Did you know? \uD83D\uDCA1*
                The Great Wall of China is not visible from the Moon without aid, contrary to popular belief \uD83D\uDC80""";
        ;

        return List.of(first, second, third, fourth, fifth);
    }
}
















