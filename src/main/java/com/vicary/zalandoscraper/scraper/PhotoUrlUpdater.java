package com.vicary.zalandoscraper.scraper;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import com.vicary.zalandoscraper.scraper.config.DefaultExtraHeaders;
import com.vicary.zalandoscraper.scraper.config.DefaultLaunchOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class PhotoUrlUpdater {
    private final static Logger logger = LoggerFactory.getLogger(PhotoUrlUpdater.class);
    private final Map<String, String> extraHeaders = new DefaultExtraHeaders();
    private final BrowserType.LaunchOptions launchOptions = new DefaultLaunchOptions();
    private final Page.NavigateOptions navigateOptions = new Page.NavigateOptions().setWaitUntil(WaitUntilState.COMMIT);

    public String getPhotoUrl(String link, String serviceName) {
        try (Playwright playwright = Playwright.create()) {
            BrowserType.LaunchOptions l = new BrowserType.LaunchOptions();
            l.setHeadless(false);
            Browser browser = playwright.chromium().launch(launchOptions);

            Page page = browser.newPage();
            page.setDefaultTimeout(10000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link, navigateOptions);

            Page.WaitForSelectorOptions wait = new Page.WaitForSelectorOptions();
            wait.setTimeout(5000);
            try {
                if (serviceName.contains("nike"))
                    return page.waitForSelector(Tag.Nike.PHOTO_URL, wait).getAttribute("src");
                else if (serviceName.contains("zalando"))
                    return page.waitForSelector(Tag.Zalando.PHOTO_URL, wait).getAttribute("src");
                else if (serviceName.contains("house"))
                    return page.waitForSelector(Tag.House.PHOTO_URL, wait).getAttribute("src");
                else if (serviceName.contains("zara"))
                    return page.waitForSelector(Tag.Zara.PHOTO_URL, wait).getAttribute("src");
                else if (serviceName.contains("hebe")) {
                    page.waitForSelector(Tag.Hebe.NAME, wait);
                    List<Locator> locators = page.locator(Tag.Hebe.PHOTO_URL).all();
                    locators = locators.stream()
                            .filter(e -> e.getAttribute("srcset") != null)
                            .toList();
                    return locators.get(0).getAttribute("srcset");
                }
            } catch (Exception ex) {
                logger.info("No photo url");
            }
        }
        return "no url";
    }
}
