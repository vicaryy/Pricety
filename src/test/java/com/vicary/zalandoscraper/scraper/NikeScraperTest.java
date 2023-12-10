package com.vicary.zalandoscraper.scraper;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitUntilState;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
class NikeScraperTest {

    /**
     * These tests should only be run manually.
     * Internet connection required and user have to update the links before.
     */

    private final static String INVALID_LINK = "https://www.nike.com/pl";
    private final static String ONE_VARIANT_LINK = "https://www.nike.com/pl/t/torba-treningowa-one-1TGt0K/CV0063-010";
    private final static String MULTI_VARIANTS_ENABLED_AND_DISABLED_LINK = "https://www.nike.com/pl/t/buty-meskie-air-force-1-07-VJhk3P/DV0788-001";
    private final static String SOLD_OUT_LINK = "https://www.nike.com/pl/t/meska-bluza-z-kapturem-i-zamkiem-calej-dlugosci-sportswear-tech-fleece-p545Hj/CU4489-016";
    private final static String NOT_AVAILABLE_LINK = "https://www.nike.com/pl/t/dres-meski-nba-courtside-boston-celtics-starting-5-city-edition-mpTvVL/FB4382-133";
    private final static String MOBILE_AVAILABLE_LINK = "https://nike.sng.link/Astn5/6tbz/r_01ad125340";
    private final NikeScraper scraper = new NikeScraper();

    @BeforeAll
    static void beforeAll() {
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages"));
    }

    @Test
    void getAllVariants_expectThrow_InvalidLink() {
        assertThrows(InvalidLinkException.class, () -> scraper.getAllVariants(INVALID_LINK));
    }

    @Test
    void getAllVariants_expectEquals_OneVariantItem() {
        //given
        int expectedSizeOfList = 1;

        //when
        List<String> actualList = scraper.getAllVariants(ONE_VARIANT_LINK);

        //then
        assertEquals(expectedSizeOfList, actualList.size());
        assertTrue(actualList.get(0).startsWith("-oneVariant "));
    }

    @Test
    void getAllVariants_expectTrue_AllVariantItem() {
        //given
        //when
        List<String> actualList = scraper.getAllVariants(MULTI_VARIANTS_ENABLED_AND_DISABLED_LINK);

        //then
        assertTrue(actualList.size() > 1);
    }


    @Test
    void getAllVariants_expectEquals_NotAvailable() {
        //given
        int expectedSizeOfList = 1;
        //when
        List<String> actualList = scraper.getAllVariants(SOLD_OUT_LINK);

        //then
        assertEquals(expectedSizeOfList, actualList.size());
        assertTrue(actualList.get(0).startsWith("-oneVariant Unknown"));
    }


    @Test
    void getProduct_expectEquals_ValidProduct() {
        //given
        String givenLink = MULTI_VARIANTS_ENABLED_AND_DISABLED_LINK;
        List<String> givenVariants = scraper.getAvailableVariants(givenLink);
        String givenVariant = givenVariants.get(ThreadLocalRandom.current().nextInt(0, givenVariants.size()));

        //when
        Product actualProduct = scraper.getProduct(givenLink, givenVariant);

        //then
        assertTrue(actualProduct.getPrice() != 0);
        assertNotNull(actualProduct.getName());
        assertNotNull(actualProduct.getDescription());
        assertEquals(givenVariant, actualProduct.getVariant());
        assertEquals(givenLink, actualProduct.getLink());
    }

    @Test
    void getProduct_expectEquals_ValidProductSizeNotAvailable() {
        //given
        String givenLink = MULTI_VARIANTS_ENABLED_AND_DISABLED_LINK;
        List<String> givenVariants = scraper.getNonAvailableVariants(givenLink);
        String givenVariant = givenVariants.get(ThreadLocalRandom.current().nextInt(0, givenVariants.size()));

        //when
        Product actualProduct = scraper.getProduct(givenLink, givenVariant);

        //then
        assertEquals(0, actualProduct.getPrice());
        assertNotNull(actualProduct.getName());
        assertNotNull(actualProduct.getDescription());
        assertEquals(givenVariant, actualProduct.getVariant());
        assertEquals(givenLink, actualProduct.getLink());
    }

    @Test
    void getProduct_expectEquals_VariantUnknown() {
        //given
        String givenLink = SOLD_OUT_LINK;
        String givenVariant = "-oneVariant Unknown";

        //when
        Product actualProduct = scraper.getProduct(givenLink, givenVariant);

        //then
        assertEquals(0, actualProduct.getPrice());
        assertNotNull(actualProduct.getName());
        assertNotNull(actualProduct.getDescription());
        assertEquals(givenVariant, actualProduct.getVariant());
        assertEquals(givenLink, actualProduct.getLink());
    }

    @Test
    void getProduct_expectEquals_MobileLink() {
        //given
        String givenLink = MOBILE_AVAILABLE_LINK;
        String givenVariant = "-oneVariant Unknown";

        //when
        Product actualProduct = scraper.getProduct(givenLink, givenVariant);

        //then
        assertEquals(0, actualProduct.getPrice());
        assertNotNull(actualProduct.getName());
        assertNotNull(actualProduct.getDescription());
        assertEquals(givenVariant, actualProduct.getVariant());
        assertNotEquals(givenLink, actualProduct.getLink());
    }

    @Test
    void updateProduct_expectTrue_OneVariantItem() {
        //given
        Product givenProduct = Product.builder()
                .price(500)
                .newPrice(0)
                .build();

        //when
        runPlaywrightAndUpdateProduct(ONE_VARIANT_LINK, givenProduct);

        //then
        assertTrue(givenProduct.getNewPrice() != 0);
    }

    @Test
    void updateProduct_expectEquals_ItemSoldOut() {
        //given
        int expectedPrice = 0;
        Product givenProduct = Product.builder()
                .price(500)
                .newPrice(500)
                .build();

        //when
        runPlaywrightAndUpdateProduct(SOLD_OUT_LINK, givenProduct);

        //then
        assertEquals(expectedPrice, givenProduct.getNewPrice());
    }


    @Test
    void updateProduct_expectEquals_ItemWithAvailableVariant() {
        //given
        String givenLink = MULTI_VARIANTS_ENABLED_AND_DISABLED_LINK;
        List<String> givenVariants = scraper.getAvailableVariants(givenLink);
        String givenVariant = givenVariants.get(ThreadLocalRandom.current().nextInt(0, givenVariants.size()));

        Product givenProduct = Product.builder()
                .price(500)
                .newPrice(0)
                .variant(givenVariant)
                .build();

        //when
        runPlaywrightAndUpdateProduct(givenLink, givenProduct);

        //then
        assertTrue(givenProduct.getNewPrice() != 0);
    }

    @Test
    void updateProduct_expectEquals_ItemWithNotAvailableVariant() {
        //given
        int expectedPrice = 0;
        String givenLink = MULTI_VARIANTS_ENABLED_AND_DISABLED_LINK;
        List<String> givenVariants = scraper.getNonAvailableVariants(givenLink);
        String givenVariant = givenVariants.get(ThreadLocalRandom.current().nextInt(0, givenVariants.size()));

        Product givenProduct = Product.builder()
                .price(500)
                .newPrice(500)
                .variant(givenVariant)
                .build();

        //when
        runPlaywrightAndUpdateProduct(givenLink, givenProduct);

        assertEquals(expectedPrice, givenProduct.getNewPrice());
    }

    @Test
    void updateProduct_expectEquals_ItemNotLongerAvailable() {
        //given
        int expectedPrice = 0;

        Product givenProduct = Product.builder()
                .price(500)
                .newPrice(500)
                .variant("variant")
                .build();

        //when
        runPlaywrightAndUpdateProduct(NOT_AVAILABLE_LINK, givenProduct);

        assertEquals(expectedPrice, givenProduct.getNewPrice());
    }

    void runPlaywrightAndUpdateProduct(String link, Product product) {
        Map<String, String> extraHeaders = Map.of("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
        BrowserType.LaunchOptions launchOptions = new DefaultLaunchOptions();
        Page.NavigateOptions navigateOptions = new Page.NavigateOptions().setWaitUntil(WaitUntilState.COMMIT);

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(launchOptions);
            Page page = browser.newPage();
            page.setDefaultTimeout(10000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link, navigateOptions);

            scraper.updateProduct(page, product);
        }
    }
}


















