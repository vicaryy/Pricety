package com.vicary.zalandoscraper.scraper;

import com.microsoft.playwright.*;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.options.WaitUntilState;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZaraScraper implements Scraper {

    private final static Logger logger = LoggerFactory.getLogger(ZaraScraper.class);
    private final Map<String, String> extraHeaders = new HashMap<>();
    private final BrowserType.LaunchOptions launchOptions = new DefaultLaunchOptions();
    private final Page.NavigateOptions navigateOptions = new Page.NavigateOptions().setWaitUntil(WaitUntilState.COMMIT);

    {
        extraHeaders.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
        extraHeaders.put("Upgrade-Insecure-Requests", "1");
        extraHeaders.put("Sec-Fetch-User", "?1");
        extraHeaders.put("Sec-Fetch-Site", "same-origin");
        extraHeaders.put("Sec-Fetch-Mode", "navigate");
        extraHeaders.put("Sec-Fetch-Dest", "dust");
        extraHeaders.put("Sec-Ch-Ua-Platform", "\"macOS\"");
        extraHeaders.put("Sec-Ch-Ua-Mobile", "?0");
        extraHeaders.put("Sec-Ch-Ua", "\"Google Chrome\";v=\"119\", \"Chromium\";v=\"119\", \"Not?A_Brand\";v=\"24\"");
        extraHeaders.put("Cache-Control", "max-age=0");
    }

    @Override
    public void updateProducts(List<Product> products) {

    }

    @Override
    public Product getProduct(String link, String variant) {
        return null;
    }

    @Override
    public List<String> getAllVariants(String link) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(launchOptions);

            Page page = browser.newPage();
            page.setDefaultTimeout(10000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link);

//            clickCookiesButton(page);

            if (!isLinkValid(page))
                throw new InvalidLinkException(Messages.scraper("invalidLink"), "User %s specified wrong link: %s".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

            String color = "";
            String variant = "";
            boolean multiColor = isMultiColor(page);
            boolean multiVariant = isMultiVariant(page);

            // blue+M

            if (multiColor && multiVariant) {

            }

            if (multiColor && !multiVariant) {
                logger.info("Multi color but NOT multi variant");
                return getAllMultiColorsAsString(page);
            }

            if (!multiColor && multiVariant) {
                logger.info("Multi variant but NOT multi color");
                return getAllMultiVariantsAsString(page);
            }


            if (!multiColor && !multiVariant) {
                logger.info("One Variant item");
                return List.of("-oneVariant One Variant");
            }


            return null;
//
//            if (isMultiVariant(page))
//                return getVariantsAsString(page);
//
//            return List.of("-oneVariant " + getOneVariant(page));
        }
    }

    private List<String> getAllMultiVariantsAsString(Page page) {
        return getAllMultiVariants(page).stream()
                .map(l -> "One Color" + " + " + l.locator("div.product-size-info__main-label").first().textContent().trim())
                .toList();
    }

    private List<Locator> getAllMultiVariants(Page page) {
        return page.locator("li.size-selector-list__item").all();
    }

    private List<String> getAllMultiColorsAsString(Page page) {
        return getAllMultiColors(page).stream()
                .map(l -> l.textContent().trim() + " + " + "One Variant")
                .toList();
    }

    private List<Locator> getAllMultiColors(Page page) {
        return page.locator("li.product-detail-color-selector__color").all();
    }

    private boolean isMultiColor(Page page) {
        if (page.isVisible("li.product-detail-color-selector__color")) {
            logger.info("Is multi color");
            return true;
        }
        logger.info("Is NOT multi color");
        return false;
    }

    private boolean isMultiVariant(Page page) {
        if (page.isVisible("li.size-selector-list__item")) {
            logger.info("Is multi variant");
            return true;
        }
        logger.info("Is NOT multi variant");
        return false;
    }

    private boolean isLinkValid(Page page) {
        Page.WaitForSelectorOptions wait = new Page.WaitForSelectorOptions();
        wait.setTimeout(4000);
        try {
            page.waitForSelector("div.product-detail-info", wait);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private void sleep() {
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void clickCookiesButton(Page page) {
        page.waitForSelector("button.onetrust-accept-btn-handler").click();
    }

    @Override
    public void setBugged(boolean bugged) {

    }
}
