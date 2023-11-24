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
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class NikeScraperTest {
    private final NikeScraper scraper = new NikeScraper();

    @BeforeAll
    static void beforeAll() {
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages"));
    }

    @Test
    void getAllVariants_expectThrow_InvalidLink() {
        String givenLink = "https://www.nike.com/pl";
        assertThrows(InvalidLinkException.class, () -> scraper.getAllVariants(givenLink));
    }

    @Test
    void getAllVariants_expectEquals_OneVariantItem() {
        String givenLink = "https://www.nike.com/pl/t/torba-pilkarska-academy-team-jGfTTw/CU8090-010";

        List<String> actualList = scraper.getAllVariants(givenLink);
        assertEquals(List.of("-oneVariant W JEDNYM ROZMIARZE"), actualList);
    }

    @Test
    void getAllVariants_expectEquals_AllVariantItem() {
        String givenLink = "https://www.nike.com/pl/t/pilka-do-pilki-noznej-academy-marcus-rashford-sJ9NqB/FQ8763-100";

        List<String> actualList = scraper.getAllVariants(givenLink);
        assertEquals(List.of("4", "5"), actualList);
    }


    @Test
    void getAllVariants_expectEquals_NotAvailable() {
        String givenLink = "https://www.nike.com/pl/t/bluza-z-kapturem-sportswear-club-fleece-tz1kGP/BV2654-591";

        List<String> actualList = scraper.getAllVariants(givenLink);
        System.out.println(actualList);
        assertEquals(List.of("-oneVariant Unknown"), actualList);
    }


    @Test
    void getProduct_expectEquals_ValidProduct() {
        String givenLink = "https://www.nike.com/pl/u/custom-mercurial-vapor-15-elite-shoes-by-you-10001668/5042664981";
        Product expectedProduct = Product.builder()
                .name("Nike Mercurial Vapor 15 Elite By You")
                .description("Personalizowane korki piłkarskie na twardą murawę")
                .price(1399.99)
                .variant("EU 44")
                .link("https://www.nike.com/pl/u/custom-mercurial-vapor-15-elite-shoes-by-you-10001668/5042664981")
                .build();
        Product actualProduct = scraper.getProduct(givenLink, "EU 44");

        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    void getProduct_expectEquals_ValidProductSizeNotAvailable() {
        String givenLink = "https://www.nike.com/pl/t/buty-meskie-air-jordan-7-retro-S3rkwk/CU9307-160";
        Product expectedProduct = Product.builder()
                .name("Air Jordan 7 Retro")
                .description("Buty męskie")
                .price(0)
                .variant("EU 44")
                .link("https://www.nike.com/pl/t/buty-meskie-air-jordan-7-retro-S3rkwk/CU9307-160")
                .build();
        Product actualProduct = scraper.getProduct(givenLink, "EU 44");

        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    void getProduct_expectEquals_VariantUnknown() {
        String givenLink = "https://www.nike.com/pl/t/bluza-z-kapturem-sportswear-club-fleece-tz1kGP/BV2654-591";
        Product expectedProduct = Product.builder()
                .name("Nike Sportswear Club Fleece")
                .description("Bluza z kapturem")
                .price(0)
                .variant("-oneVariant Unknown")
                .link("https://www.nike.com/pl/t/bluza-z-kapturem-sportswear-club-fleece-tz1kGP/BV2654-591")
                .build();
        Product actualProduct = scraper.getProduct(givenLink, "-oneVariant Unknown");

        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    void updateProduct_expectEquals_OneVariantItem() {
        String link = "https://www.nike.com/pl/t/plecak-hike-SJzGh0/DJ9677-225";

        ProductDTO givenDto = ProductDTO.builder()
                .price(500)
                .newPrice(500)
                .build();

        ProductDTO expectedDto = ProductDTO.builder()
                .price(500)
                .newPrice(329.99)
                .build();

        Map<String, String> extraHeaders = Map.of("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
        BrowserType.LaunchOptions launchOptions = new DefaultLaunchOptions();
        Page.NavigateOptions navigateOptions = new Page.NavigateOptions().setWaitUntil(WaitUntilState.COMMIT);

        try (Playwright playwright = Playwright.create()) {
            com.microsoft.playwright.BrowserType.LaunchOptions l = new BrowserType.LaunchOptions();
            l.setHeadless(false);
            Browser browser = playwright.chromium().launch(l);
            Page page = browser.newPage();
            page.setDefaultTimeout(10000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link, navigateOptions);


            scraper.updateProduct(page, givenDto);
        }

        assertEquals(givenDto, expectedDto);
    }

    @Test
    void updateProduct_expectEquals_ItemSoldOut() {
        String link = "https://www.nike.com/pl/t/bluza-z-kapturem-sportswear-club-fleece-tz1kGP/BV2654-591";

        ProductDTO givenDto = ProductDTO.builder()
                .price(500)
                .newPrice(500)
                .build();

        ProductDTO expectedDto = ProductDTO.builder()
                .price(500)
                .newPrice(0)
                .build();

        Map<String, String> extraHeaders = Map.of("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
        BrowserType.LaunchOptions launchOptions = new DefaultLaunchOptions();
        Page.NavigateOptions navigateOptions = new Page.NavigateOptions().setWaitUntil(WaitUntilState.COMMIT);

        try (Playwright playwright = Playwright.create()) {
            com.microsoft.playwright.BrowserType.LaunchOptions l = new BrowserType.LaunchOptions();
            l.setHeadless(false);
            Browser browser = playwright.chromium().launch(l);
            Page page = browser.newPage();
            page.setDefaultTimeout(10000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link, navigateOptions);


            scraper.updateProduct(page, givenDto);
        }

        assertEquals(givenDto, expectedDto);
    }


    @Test
    void updateProduct_expectEquals_ItemWithAvailableVariant() {
        String link = "https://www.nike.com/pl/t/buty-meskie-air-jordan-7-retro-S3rkwk/CU9307-160";

        ProductDTO givenDto = ProductDTO.builder()
                .price(500)
                .newPrice(500)
                .variant("EU 50.5")
                .build();

        ProductDTO expectedDto = ProductDTO.builder()
                .price(500)
                .newPrice(689.97)
                .build();

        Map<String, String> extraHeaders = Map.of("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
        BrowserType.LaunchOptions launchOptions = new DefaultLaunchOptions();
        Page.NavigateOptions navigateOptions = new Page.NavigateOptions().setWaitUntil(WaitUntilState.COMMIT);

        try (Playwright playwright = Playwright.create()) {
            com.microsoft.playwright.BrowserType.LaunchOptions l = new BrowserType.LaunchOptions();
            l.setHeadless(false);
            Browser browser = playwright.chromium().launch(l);
            Page page = browser.newPage();
            page.setDefaultTimeout(10000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link, navigateOptions);


            scraper.updateProduct(page, givenDto);
        }

        assertEquals(givenDto, expectedDto);
    }



    // niedostepny - https://www.nike.com/pl/t/bluza-z-kapturem-sportswear-club-fleece-tz1kGP/BV2654-591
    // one size - https://www.nike.com/pl/t/czapka-dla-peak-swoosh-6n8BrH/FB6492-010
    @Test
    @SneakyThrows
    void getTextToFile() {
//        String link = "https://www.zalando.pl/jack-and-jones-jpstmarco-bojowki-otter-ja222e0r9-o12.html";
//        String locator = "";
//
//        final Map<String, String> extraHeaders = Map.of("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
//        try (Playwright playwright = Playwright.create()) {
//            com.microsoft.playwright.BrowserType.LaunchOptions l = new BrowserType.LaunchOptions();
//            l.setHeadless(false);
//            Browser browser = playwright.chromium().launch(l);
//
//            Page page = browser.newPage();
//            page.setDefaultTimeout(10000);
//            page.setExtraHTTPHeaders(extraHeaders);
//            page.navigate(link);
//            Thread.sleep(500);
////            Files.writeString(Path.of("src/test/java/com/vicary/zalandoscraper/scraper/zalando.txt"), page.locator(locator).innerHTML());
//            Files.writeString(Path.of("src/test/java/com/vicary/zalandoscraper/scraper/zalando.txt"), page.content());
//        }
    }
}


















