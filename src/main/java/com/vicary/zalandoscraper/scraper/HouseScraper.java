package com.vicary.zalandoscraper.scraper;

import com.microsoft.playwright.*;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.options.WaitUntilState;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.exception.TimeoutException;
import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HouseScraper implements Scraper {
    private final static Logger logger = LoggerFactory.getLogger(HebeScraper.class);
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
        try (Playwright playwright = Playwright.create()) {

            try (BrowserContext browser = playwright.chromium().launch(launchOptions).newContext()) {
                browser.newPage();
                browser.setDefaultTimeout(4000);

                for (Product p : products) {
                    Page newPage = browser.newPage();
                    newPage.setExtraHTTPHeaders(extraHeaders);
                    newPage.navigate(p.getLink(), navigateOptions);
                    newPage.setDefaultTimeout(4000);

                    updateProduct(newPage, p);
                }
            }
        }
    }

    private void updateProduct(Page page, Product product) {

    }

    @Override
    public Product getProduct(String link, String variant) {
        return null;
    }

    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<String> getAllVariants(String link) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(launchOptions);

            Page page = browser.newPage();
            page.setDefaultTimeout(14000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link);

            clickCookiesButton(page);

            if (!isLinkValid(page))
                throw new InvalidLinkException(Messages.scraper("invalidLink"), "User %s specified wrong link: %s".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));


            if (isMultiVariant(page))
                return getAllVariantsAsString(page);

            return List.of("-oneVariant " + getOneVariant(page));
        }
    }

    private String getOneVariant(Page page) {
        return page.getByTestId("size-picker-size-name").first().textContent().trim();
    }

    private void clickCookiesButton(Page page) {
        page.waitForSelector("button#cookiebotDialogOkButton").click();
    }

    private List<String> getAllVariantsAsString(Page page) {
        return page.locator("li.itemstyled__ItemStyled-sc-1p6n2ae-0.zwcmQ").all().stream()
                .map(l -> l.textContent().trim())
                .toList();
    }

    private boolean isMultiVariant(Page page) {
        return !page.locator("li.itemstyled__ItemStyled-sc-1p6n2ae-0.zwcmQ").all().isEmpty();
    }

    private void waitForContent(Page page) {

    }

    private boolean isLinkValid(Page page) {
        Page.WaitForSelectorOptions wait = new Page.WaitForSelectorOptions();
        wait.setTimeout(4000);
        try {
            page.waitForSelector("div.desktop__RightSection-sc-19wvx4y-1.llWjxx", wait);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public void setBugged(boolean bugged) {
        launchOptions.setHeadless(!bugged);
    }
}
