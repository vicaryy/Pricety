package com.vicary.zalandoscraper.scraper;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.ResourceBundle;

public class ZalandoScraperTest {

    private ZalandoScraper scraper = new ZalandoScraper();
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
    void test() {
        page.setContent(PageHTML.Zalando.getTest());
        scraper.clickSizeButton(page);
    }
}
