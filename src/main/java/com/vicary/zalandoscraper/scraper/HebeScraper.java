package com.vicary.zalandoscraper.scraper;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class HebeScraper implements Scraper {
    private final static Logger logger = LoggerFactory.getLogger(HebeScraper.class);
    private final Map<String, String> extraHeaders = Map.of("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
    private final BrowserType.LaunchOptions launchOptions = new DefaultLaunchOptions();
    private final Page.NavigateOptions navigateOptions = new Page.NavigateOptions().setWaitUntil(WaitUntilState.COMMIT);

    @Override
    public void updateProducts(List<ProductDTO> DTOs) {
//        try (Playwright playwright = Playwright.create()) {
//
//            try (BrowserContext browser = playwright.chromium().launch(launchOptions).newContext()) {
//                browser.newPage();
//                browser.setDefaultTimeout(4000);
//
//                for (int i = 0; i < DTOs.size(); i++) {
//                    Page newPage = browser.newPage();
//                    newPage.setExtraHTTPHeaders(extraHeaders);
//                    newPage.navigate(DTOs.get(i).getLink(), navigateOptions);
//                    newPage.setDefaultTimeout(4000);
//
//                    if (i == 0)
//                        clickCookiesButton(newPage);
//
//                    updateProduct(newPage, DTOs.get(i));
//                }
//            }
//        }
    }

    @Override
    public Product getProduct(String link, String variant) {
        return null;
    }

    @Override
    public List<String> getAllVariants(String link) {
        try (Playwright playwright = Playwright.create()) {
            BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
            launchOptions.setHeadless(false);
            Browser browser = playwright.chromium().launch(launchOptions);

            Page page = browser.newPage();
            page.setDefaultTimeout(4000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link, navigateOptions);

            waitForContent(page);

            if (isMultiVariant(page))
                return getAllVariantsAsString(page);

            return List.of("-oneVariant");
        }
    }

    @Override
    public void setBugged(boolean bugged) {

    }

    private boolean isPageValid(Page page) {
        return page.locator("div.product-summary__main-box").count() > 0;
    }

    private boolean isMultiVariant(Page page) {
        return page.isVisible(Tag.Hebe.IS_MULTI_VARIANT);
    }

    private boolean isItemSoldOut(Page page) {
        return page.getByText("Produkt niedostępny online").isVisible();
    }

    private static String getName(Page page) {
        String name = page.locator("p.product-content__brand").innerText().replace("\n", " ");
        if (name.contains("\n"))
            name = name.replaceAll("\n", " ");
        return name;
    }

    private static String getDescription(Page page) {
        return page.locator("p.js-product-short-description").innerText();
    }

    private static double getPrice(Page page) {
        String price = page.locator("div.price-product__wrapper").innerText().trim();

        String[] priceArray = price.split("\n");

        if (priceArray[0].contains("."))
            priceArray[0] = priceArray[0].replaceAll("\\.", "");

        return Double.parseDouble(priceArray[0] + "." + priceArray[1]);
    }

    private List<String> getAllAvailableVariantsAsString(Page page) {
        return page.locator("div.swatch__item--selectable")
                .all()
                .stream()
                .map(e -> e.textContent().trim())
                .toList();
    }

    private List<String> getAllVariantsAsString(Page page) {
        return page.locator(Tag.Hebe.GET_ALL_VARIANTS)
                .all()
                .stream()
                .map(e -> e.textContent().trim())
                .toList();
    }

    private void waitForContent(Page page) {
        page.waitForSelector(Tag.Hebe.WAIT_FOR_CONTENT);
    }
}
