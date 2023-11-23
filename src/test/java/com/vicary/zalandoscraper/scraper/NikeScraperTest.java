package com.vicary.zalandoscraper.scraper;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.impl.PageImpl;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class NikeScraperTest {
    private final NikeScraper scraper = new NikeScraper();

    private static Playwright playwright;
    private static Browser browser;
    private static Page page;


    @BeforeAll
    static void beforeAll() {
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages", Locale.of("en")));
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        page = browser.newPage();
    }

    @AfterAll
    static void afterAll() {
        playwright.close();
    }

    @Test
    void getAllVariants() {
        List<String> actualList = scraper.getAllVariants("https://www.nike.com/pl/t/buty-meskie-court-vision-mid-winter-8LrcSX/DR7882-002");
        assertEquals(List.of("-oneVariant One Variant"), actualList);
    }

    @Test
    void getAllVariants_expectThrows_InvalidLink() {
//        page.setContent();
        String link = "https://www.nike.com/pl/t/wrongLink";
        assertThrows(InvalidLinkException.class, () -> scraper.getAllVariants(page));
    }

    @Test
    @SneakyThrows
    void getAllVariants_expectThrows_ValidLinkButNotForProduct() {
        long time = System.currentTimeMillis();
        page.setContent(PageHTML.Nike.getValidLinkButWithoutProduct());
        System.out.println(System.currentTimeMillis() - time);
        assertThrows(InvalidLinkException.class, () -> scraper.getAllVariants(page));
    }

    @Test
    @SneakyThrows
    void getTextToFile() {
        String link = "https://www.zalando.pl/jack-and-jones-jpstmarco-bojowki-otter-ja222e0r9-o12.html";
        String locator = "";

        final Map<String, String> extraHeaders = Map.of("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
        try (Playwright playwright = Playwright.create()) {
            com.microsoft.playwright.BrowserType.LaunchOptions l = new BrowserType.LaunchOptions();
            l.setHeadless(false);
            Browser browser = playwright.chromium().launch(l);

            Page page = browser.newPage();
            page.setDefaultTimeout(10000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link);
            Thread.sleep(500);
//            Files.writeString(Path.of("src/test/java/com/vicary/zalandoscraper/scraper/zalando.txt"), page.locator(locator).innerHTML());
            Files.writeString(Path.of("src/test/java/com/vicary/zalandoscraper/scraper/zalando.txt"), page.content());
        }
    }
}


















