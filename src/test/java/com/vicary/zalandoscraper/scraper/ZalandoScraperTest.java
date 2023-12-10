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
class ZalandoScraperTest {

    /**
     * These tests should only be run manually.
     * Internet connection required and user have to update the links before.
     */

    private final static String INVALID_LINK = "https://www.zalando.pl/";
    private final static String ONE_VARIANT_LINK = "https://www.zalando.pl/new-era-new-york-yankees-czapki-i-kapelusze-niebieski-ne344a07t-502.html";
    private final static String MULTI_VARIANTS_ENABLED_AND_DISABLED_LINK = "https://www.zalando.pl/puma-shuffle-mid-unisex-sneakersy-wysokie-whiteblackteam-gold-pu115n01n-a12.html";
    private final static String SOLD_OUT_LINK = "https://www.zalando.pl/vans-t-shirt-z-nadrukiem-black-va222o0p4-q11.html";
    private final ZalandoScraper scraper = new ZalandoScraper();

    @BeforeAll
    static void beforeAll() {
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages"));
    }


    @Test
    void test() {
        //given
        String givenLink = "https://www.zalando.es/nike-sportswear-air-max-90-gtx-unisex-zapatillas-blackhoneydewmica-green-ni112o0xk-q11.html";
        String givenVariant = "55";

        Product product = scraper.getProduct(givenLink, givenVariant);
        System.out.println(product);
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
    void getAllVariants_expectEquals_SoldOutItem() {
        //given
        //when
        List<String> actualList = scraper.getAllVariants(SOLD_OUT_LINK);

        //then
        assertFalse(actualList.isEmpty());
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
    void getProduct_expectEquals_SoldOutItem() {
        //given
        String givenLink = SOLD_OUT_LINK;
        String givenVariant = "givenVariant";

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
    void updateProduct_expectTrue_OneVariantItem() {
        //given
        Product givenDto = Product.builder()
                .price(500)
                .newPrice(0)
                .variant("-oneVariant One Size")
                .build();

        //when
        runPlaywrightAndUpdateProduct(ONE_VARIANT_LINK, givenDto);

        //then
        assertTrue(givenDto.getNewPrice() != 0);
    }

    @Test
    void updateProduct_expectEquals_ItemSoldOut() {
        //given
        int expectedPrice = 0;
        Product givenDto = Product.builder()
                .price(500)
                .newPrice(500)
                .build();

        //when
        runPlaywrightAndUpdateProduct(SOLD_OUT_LINK, givenDto);

        //then
        assertEquals(expectedPrice, givenDto.getNewPrice());
    }


    @Test
    void updateProduct_expectEquals_ItemWithAvailableVariant() {
        //given
        String givenLink = MULTI_VARIANTS_ENABLED_AND_DISABLED_LINK;
        List<String> givenVariants = scraper.getAvailableVariants(givenLink);
        String givenVariant = givenVariants.get(ThreadLocalRandom.current().nextInt(0, givenVariants.size()));

        Product givenDto = Product.builder()
                .price(500)
                .newPrice(0)
                .variant(givenVariant)
                .build();

        //when
        runPlaywrightAndUpdateProduct(givenLink, givenDto);

        //then
        assertTrue(givenDto.getNewPrice() != 0);
    }

    @Test
    void updateProduct_expectEquals_ItemWithNotAvailableVariant() {
        //given
        int expectedPrice = 0;
        String givenLink = MULTI_VARIANTS_ENABLED_AND_DISABLED_LINK;
        List<String> givenVariants = scraper.getNonAvailableVariants(givenLink);
        String givenVariant = givenVariants.get(ThreadLocalRandom.current().nextInt(0, givenVariants.size()));

        Product givenDto = Product.builder()
                .price(500)
                .newPrice(500)
                .variant(givenVariant)
                .build();

        //when
        runPlaywrightAndUpdateProduct(givenLink, givenDto);

        assertEquals(expectedPrice, givenDto.getNewPrice());
    }

    private void runPlaywrightAndUpdateProduct(String link, Product product) {
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

    @Test
    void getServiceName_expectEquals_ZalandoPL() {
        //given
        String givenLink = "https://www.zalando.pl/asdasd-asd/asd.html";
        String expectedServiceName = "zalando.pl";

        //when
        String actualServiceName = scraper.getServiceName(givenLink);

        //then
        assertEquals(expectedServiceName, actualServiceName);
    }

    @Test
    void getServiceName_expectEquals_ZalandoCZ() {
        //given
        String givenLink = "https://www.zalando.cz/asdasd-asd/asd.html";
        String expectedServiceName = "zalando.cz";

        //when
        String actualServiceName = scraper.getServiceName(givenLink);

        //then
        assertEquals(expectedServiceName, actualServiceName);
    }

    @Test
    void getServiceName_expectEquals_ZalandoWHATEVER() {
        //given
        String givenLink = "https://www.zalando.whatever123/asdasd-asd/asd.html";
        String expectedServiceName = "zalando.whatever123";

        //when
        String actualServiceName = scraper.getServiceName(givenLink);

        //then
        assertEquals(expectedServiceName, actualServiceName);
    }

    @Test
    void getServiceName_expectEquals_ZalandoEnAndDe() {
        //given
        String givenLink = "https://en.zalando.de/asdasd-asd/asd.html";
        String expectedServiceName = "zalando.de";

        //when
        String actualServiceName = scraper.getServiceName(givenLink);

        //then
        assertEquals(expectedServiceName, actualServiceName);
    }

    @Test
    void getProduct_NotAvailableVariant_UK() {
        //given
        String givenLink = "https://it.zalando.ch/hugo-t-shirt-basic-blackwhite-hu722o05k-q11.html";
        String givenVariant = "XL";
        //when
        Product actualProduct = scraper.getProduct(givenLink, givenVariant);
        System.out.println(actualProduct);
    }
}


















