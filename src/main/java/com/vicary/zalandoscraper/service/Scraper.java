package com.vicary.zalandoscraper.service;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.tag.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Scraper {

    private final static Logger logger = LoggerFactory.getLogger(Scraper.class);
    public static boolean BUGGED;
    private final Map<String, String> extraHeaders = Map.of("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
    private final BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
    private final Page.NavigateOptions navigateOptions = new Page.NavigateOptions().setWaitUntil(WaitUntilState.COMMIT);
    private static Scraper INSTANCE;
    private boolean bugged;

    private Scraper() {
        launchOptions.setArgs(List.of(
                "--no-sandbox",
                "--disable-setuid-sandbox",
                "--disable-dev-shm-usage",
                "--disable-accelerated-2d-canvas",
                "--no-first-run",
                "--no-zygote",
                "--single-process",
                "--disable-gpu"
        ));
//        launchOptions.setHeadless(true);
    }

    public static Scraper getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Scraper();
        return INSTANCE;
    }

    public void updateProducts(List<ProductDTO> DTOs) {
        try (Playwright playwright = Playwright.create()) {

            try (BrowserContext browser = playwright.chromium().launch(launchOptions).newContext()) {
                browser.newPage();

                for (int i = 0; i < DTOs.size(); i++) {
                    Page newPage = browser.newPage();
                    newPage.setExtraHTTPHeaders(extraHeaders);
                    newPage.navigate(DTOs.get(i).getLink(), navigateOptions);

                    if (i == 0)
                        clickCookiesButton(newPage);

                    updateProduct(newPage, DTOs.get(i));
                }
            }
        }
    }

    private void updateProduct(Page page, ProductDTO dto) {
        try (page) {
            waitForMainPage(page);

            if (!isLinkValid(page)) {
                logger.debug("Product '{}' - link invalid", dto.getProductId());
                dto.setNewPrice(0);
                return;
            }

            if (isItemSoldOut(page)) {
                logger.debug("Product '{}' - item sold out", dto.getProductId());
                dto.setNewPrice(0);
                return;
            }

            if (dto.getVariant().startsWith("-oneVariant")) {
                dto.setNewPrice(getPrice(page));
                return;
            }

            if (isVariantAlreadyChosen(page, dto.getVariant())) {
                dto.setNewPrice(getPrice(page));
                return;
            }

            clickSizeButton(page);

            if (!clickAvailableVariant(getAvailableVariantsAsLocators(page), dto.getVariant())) {
                dto.setNewPrice(0);
                logger.debug("Product '{}' - item variant not available", dto.getProductId());
                return;
            }

            dto.setNewPrice(getPrice(page));

        } catch (PlaywrightException ex) {
            ex.printStackTrace();
            logger.warn("Failed to update productId '{}'", dto.getProductId());
        }
    }


    public Product getProduct(String link, String variant) {
        try (Playwright playwright = Playwright.create()) {
            try (Browser browser = playwright.chromium().launch(launchOptions)) {
                Page page = browser.newPage();
                page.setDefaultTimeout(10000);
                page.setExtraHTTPHeaders(extraHeaders);
                page.navigate(link);

                try {
                    Product product = Product.builder()
                            .name(getName(page))
                            .description(getDescription(page))
                            .price(0)
                            .variant(variant)
                            .link(link)
                            .build();

                    if (isItemSoldOut(page))
                        return product;

                    if (variant.startsWith("-oneVariant")) {
                        product.setPrice(getPrice(page));
                        return product;
                    }

                    if (isVariantAlreadyChosen(page, variant)) {
                        product.setPrice(getPrice(page));
                        return product;
                    }

                    clickSizeButton(page);

                    List<Locator> availableVariantsAsLocators = getAvailableVariantsAsLocators(page);

                    if (!clickAvailableVariant(availableVariantsAsLocators, variant))
                        return product;

                    product.setPrice(getPrice(page));

                    return product;

                } catch (PlaywrightException ex) {
                    return getProductWithCookiesClicks(page, link, variant);
                }
            }
        }
    }

    private Product getProductWithCookiesClicks(Page page, String link, String variant) {
        clickCookiesButton(page);

        Product product = Product.builder()
                .name(getName(page))
                .description(getDescription(page))
                .price(0)
                .variant(variant)
                .link(link)
                .build();

        if (isItemSoldOut(page))
            return product;

        if (variant.startsWith("-oneVariant")) {
            product.setPrice(getPrice(page));
            return product;
        }

        if (isVariantAlreadyChosen(page, variant)) {
            product.setPrice(getPrice(page));
            return product;
        }

        clickSizeButton(page);

        List<Locator> availableVariantsAsLocators = getAvailableVariantsAsLocators(page);

        if (!clickAvailableVariant(availableVariantsAsLocators, variant))
            return product;

        product.setPrice(getPrice(page));

        return product;
    }


    public List<String> getAllVariants(String link) {
        try (Playwright playwright = Playwright.create()) {
            try (Browser browser = playwright.chromium().launch(launchOptions)) {
                Page page = browser.newPage();
                page.setDefaultTimeout(10000);
                page.setExtraHTTPHeaders(extraHeaders);
                page.navigate(link);

                try {
                    if (!isLinkValid(page))
                        throw new InvalidLinkException("It seems your link is incorrect, please check it and try again.", "User %s specified wrong link: %s".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

                    if (isItemOneVariant(page)) {
                        return List.of("-oneVariant " + getOneVariantName(page));
                    }

                    clickSizeButton(page);

                    return getAllVariants(page);

                } catch (PlaywrightException ex) {

                    clickCookiesButton(page);

                    if (!isLinkValid(page))
                        throw new InvalidLinkException("It seems your link is incorrect, please check it and try again.", "User %s specified wrong link: %s".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

                    if (isItemOneVariant(page)) {
                        return List.of("-oneVariant " + getOneVariantName(page));
                    }

                    clickSizeButton(page);

                    return getAllVariants(page);
                }
            }
        }
    }

    private void waitForMainPage(Page page) {
        page.waitForSelector(Tag.LINK_VALID);
    }

    private String getOneVariantName(Page page) {
        return page.locator(Tag.ONE_VARIANT_NAME).innerText();
    }

    private String getName(Page page) {
        return page.locator(Tag.NAME).innerText();
    }

    private String getDescription(Page page) {
        return page.locator(Tag.DESCRIPTION).innerText();
    }

    private double getPrice(Page page) {
        String price = page.locator(Tag.PRICE).textContent();

        if (price.contains(" "))
            price = price.replaceAll(" ", "");

        if (price.contains(" "))
            price = price.replaceAll(" ", "");

        if (price.startsWith("od"))
            price = price.substring(2);

        price = price.substring(0, price.length() - 2);

        return Double.parseDouble(price.replaceFirst(",", "."));
    }


    private boolean isLinkValid(Page page) {
        return page.locator(Tag.LINK_VALID).count() > 0;
    }


    private boolean isItemOneVariant(Page page) {
        return page.getByTestId(Tag.VARIANT_BUTTON).isDisabled();
    }

    private boolean isItemSoldOut(Page page) {
        return page.getByText(Tag.SOLD_OUT).count() == 1;
    }

    private void clickSizeButton(Page page) {
        page.getByTestId(Tag.VARIANT_BUTTON).click();
        page.waitForSelector(Tag.ALL_VARIANTS);
    }

    private void clickCookiesButton(Page page) {
        try {
            page.waitForSelector(Tag.COOKIES_BUTTON).click();
        } catch (TimeoutError ignored) {
        }
    }


    private List<String> getAllVariants(Page page) {
        return page
                .locator(Tag.ALL_VARIANTS)
                .allTextContents();
    }

    private List<String> getAvailableVariants(Page page) {
        return getAvailableVariantsAsLocators(page)
                .stream()
                .map(Locator::textContent)
                .collect(Collectors.toList());
    }

    private List<Locator> getAvailableVariantsAsLocators(Page page) {
        return page
                .locator(Tag.AVAILABLE_VARIANTS)
                .all();
    }

    private boolean isVariantAlreadyChosen(Page page, String variant) {
        return page.getByTestId(Tag.VARIANT_BUTTON).innerText().startsWith(variant);
    }

    private boolean clickAvailableVariant(List<Locator> locators, String variant) {
        for (Locator l : locators)
            if (l.textContent().equals(variant)) {
                l.click();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new PlaywrightException(e.getMessage());
                }
                return true;
            }
        return false;
    }

    public void setBugged(boolean bugged) {
        launchOptions.setHeadless(!bugged);
    }
}
