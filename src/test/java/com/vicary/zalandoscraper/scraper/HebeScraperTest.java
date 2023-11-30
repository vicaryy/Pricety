package com.vicary.zalandoscraper.scraper;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitUntilState;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
class HebeScraperTest {

    /**
     * These tests should only be run manually.
     * Internet connection required and user have to update the links.
     */

    private final static String INVALID_LINK = "https://www.hebe.pl/";
    private final static String ONE_VARIANT_LINK = "https://www.hebe.pl/nacomi-niacinamide-15-30-ml-000000000000381750.html";
    private final static String MULTI_VARIANTS_ENABLED_AND_DISABLED_LINK = "https://www.hebe.pl/essence-blyszczyk-do-ust-104-10-ml-000000000000437262.html";
    private final static String SOLD_OUT_LINK = "https://www.hebe.pl/by-hebe-kosmetyczka-caffe-latte-000000000000358461.html";
    private final HebeScraper scraper = new HebeScraper();

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
        //when
        List<String> actualList = scraper.getAllVariants(SOLD_OUT_LINK);

        //then
        assertNotEquals(Collections.emptyList(), actualList);
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
    void getProduct_expectEquals_SoldOut() {
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
        ProductDTO givenDto = ProductDTO.builder()
                .price(500)
                .newPrice(0)
                .variant("-oneVariant oneVariant")
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
        ProductDTO givenDto = ProductDTO.builder()
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

        ProductDTO givenDto = ProductDTO.builder()
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

        ProductDTO givenDto = ProductDTO.builder()
                .price(500)
                .newPrice(500)
                .variant(givenVariant)
                .build();

        //when
        runPlaywrightAndUpdateProduct(givenLink, givenDto);

        assertEquals(expectedPrice, givenDto.getNewPrice());
    }

    void runPlaywrightAndUpdateProduct(String link, ProductDTO givenDto) {
        Map<String, String> extraHeaders = Map.of("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
        BrowserType.LaunchOptions launchOptions = new DefaultLaunchOptions();
        Page.NavigateOptions navigateOptions = new Page.NavigateOptions().setWaitUntil(WaitUntilState.COMMIT);

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(launchOptions);
            Page page = browser.newPage();
            page.setDefaultTimeout(10000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link, navigateOptions);

            scraper.updateProduct(page, givenDto);
        }
    }
}


















