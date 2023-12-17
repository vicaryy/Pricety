package com.vicary.zalandoscraper.scraper;

import com.microsoft.playwright.*;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.options.WaitUntilState;
import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.scraper.config.DefaultExtraHeaders;
import com.vicary.zalandoscraper.scraper.config.DefaultLaunchOptions;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ZalandoScraper implements Scraper {

    private final static Logger logger = LoggerFactory.getLogger(ZalandoScraper.class);
    private final Map<String, String> extraHeaders = Map.of("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
    private final BrowserType.LaunchOptions launchOptions = new DefaultLaunchOptions();
    private final Page.NavigateOptions navigateOptions = new Page.NavigateOptions().setWaitUntil(WaitUntilState.COMMIT);


    @Override
    public void updateProducts(List<Product> products) {
        try (Playwright playwright = Playwright.create()) {

            try (BrowserContext browser = playwright.chromium().launch(launchOptions).newContext()) {
                browser.newPage();
                browser.setDefaultTimeout(8000);

                for (int i = 0; i < products.size(); i++) {
                    Page newPage = browser.newPage();
                    newPage.setExtraHTTPHeaders(extraHeaders);
                    newPage.navigate(products.get(i).getLink(), navigateOptions);
                    newPage.setDefaultTimeout(8000);

                    if (i == 0)
                        clickCookiesButton(newPage);

                    updateProduct(newPage, products.get(i));
                }
            }
        }
    }

    void updateProduct(Page page, Product product) {
        try (page) {
            if (!isLinkValid(page)) {
                logger.debug("Product '{}' - is not longer available, delete it", product.getProductId());
                product.setNewPrice(0);
                return;
            }

            if (isItemSoldOut(page)) {
                logger.debug("Product '{}' - item sold out", product.getProductId());
                product.setNewPrice(0);
                return;
            }

            if (product.getVariant().startsWith("-oneVariant")) {
                product.setNewPrice(getPrice(page));
                return;
            }

            if (isVariantAlreadyChosen(page, product.getVariant())) {
                product.setNewPrice(getPrice(page));
                return;
            }

            clickSizeButton(page);

            if (!clickAvailableVariant(getAvailableVariantsAsLocators(page), product.getVariant())) {
                product.setNewPrice(0);
                logger.debug("Product '{}' - item variant not available", product.getProductId());
                return;
            }

            product.setNewPrice(getPrice(page));

        } catch (PlaywrightException ex) {
            logger.warn("Failed to update productId '{}'", product.getProductId());
        }
    }

    @Override
    public Product getProduct(String link, String variant) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(launchOptions);
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
                        .link(page.url())
                        .serviceName(getServiceName(link))
                        .currency(getCurrency(link))
                        .build();

                if (isItemSoldOut(page)) {
                    logger.debug("Item sold out.");
                    return product;
                }

                if (variant.startsWith("-oneVariant")) {
                    logger.debug("Item one variant.");
                    product.setPrice(getPrice(page));
                    return product;
                }

                if (isVariantAlreadyChosen(page, variant)) {
                    logger.debug("Item variant already chosen.");
                    product.setPrice(getPrice(page));
                    return product;
                }

                clickSizeButton(page);

                List<Locator> availableVariantsAsLocators = getAvailableVariantsAsLocators(page);

                if (!clickAvailableVariant(availableVariantsAsLocators, variant)) {
                    logger.debug("Item variant don't available.");
                    return product;
                }

                logger.debug("Item variant available.");
                product.setPrice(getPrice(page));

                return product;

            } catch (PlaywrightException ex) {
                return getProductWithCookiesClicks(page, link, variant);
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


    @Override
    public List<String> getAllVariants(String link) {
        try (Playwright playwright = Playwright.create()) {
            try (Browser browser = playwright.chromium().launch()) {
                Page page = browser.newPage();
                page.setDefaultTimeout(10000);
                page.setExtraHTTPHeaders(extraHeaders);
                page.navigate(link);

                try {
                    if (!isLinkValid(page))
                        throw new InvalidLinkException(Messages.scraper("invalidLink"), "User %s specified wrong link: %s".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

                    if (isItemOneVariant(page)) {
                        return List.of("-oneVariant " + getOneVariantName(page));
                    }

                    clickSizeButton(page);

                    return getAllVariants(page);

                } catch (PlaywrightException ex) {

                    clickCookiesButton(page);

                    if (!isLinkValid(page))
                        throw new InvalidLinkException(Messages.scraper("invalidLink"), "User %s specified wrong link: %s".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

                    if (isItemOneVariant(page)) {
                        return List.of("-oneVariant " + getOneVariantName(page));
                    }

                    clickSizeButton(page);

                    return getAllVariants(page);
                }
            }
        }
    }

    List<String> getAvailableVariants(String link) {
        try (Playwright playwright = Playwright.create()) {
            try (Browser browser = playwright.chromium().launch()) {
                Page page = browser.newPage();
                page.setDefaultTimeout(10000);
                page.setExtraHTTPHeaders(extraHeaders);
                page.navigate(link);

                try {
                    if (!isLinkValid(page))
                        throw new InvalidLinkException(Messages.scraper("invalidLink"), "User %s specified wrong link: %s".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

                    if (isItemOneVariant(page)) {
                        return List.of("-oneVariant " + getOneVariantName(page));
                    }

                    clickSizeButton(page);

                    return getAvailableVariants(page);

                } catch (PlaywrightException ex) {

                    clickCookiesButton(page);

                    if (!isLinkValid(page))
                        throw new InvalidLinkException(Messages.scraper("invalidLink"), "User %s specified wrong link: %s".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

                    if (isItemOneVariant(page)) {
                        return List.of("-oneVariant " + getOneVariantName(page));
                    }

                    clickSizeButton(page);

                    return getAvailableVariants(page);
                }
            }
        }
    }

    List<String> getNonAvailableVariants(String link) {
        try (Playwright playwright = Playwright.create()) {
            try (Browser browser = playwright.chromium().launch()) {
                Page page = browser.newPage();
                page.setDefaultTimeout(10000);
                page.setExtraHTTPHeaders(extraHeaders);
                page.navigate(link);

                try {
                    if (!isLinkValid(page))
                        throw new InvalidLinkException(Messages.scraper("invalidLink"), "User %s specified wrong link: %s".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

                    if (isItemOneVariant(page)) {
                        return List.of("-oneVariant " + getOneVariantName(page));
                    }

                    clickSizeButton(page);

                    return getNonAvailableVariants(page);

                } catch (PlaywrightException ex) {

                    clickCookiesButton(page);

                    if (!isLinkValid(page))
                        throw new InvalidLinkException(Messages.scraper("invalidLink"), "User %s specified wrong link: %s".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

                    if (isItemOneVariant(page)) {
                        return List.of("-oneVariant " + getOneVariantName(page));
                    }

                    clickSizeButton(page);

                    return getNonAvailableVariants(page);
                }
            }
        }
    }

    private void waitForMainPage(Page page) {
        page.waitForSelector(Tag.Zalando.MAIN_TAB);
    }

    private String getOneVariantName(Page page) {
        return page.locator(Tag.Zalando.ONE_VARIANT_TAB).innerText();
    }

    private String getName(Page page) {
        return page.locator(Tag.Zalando.NAME).innerText();
    }

    private String getDescription(Page page) {
        return page.locator(Tag.Zalando.DESCRIPTION).innerText();
    }

    private double getPrice(Page page) {
        String price = "";

        if (page.isVisible(Tag.Zalando.PRICE_SPAN))
            price = page.locator(Tag.Zalando.PRICE_SPAN).first().textContent();
        else if (page.isVisible(Tag.Zalando.PRICE_P))
            price = page.locator(Tag.Zalando.PRICE_P).first().textContent();
        else if (page.isVisible(Tag.Zalando.PRICE_P_SECOND_VARIANT))
            price = page.locator(Tag.Zalando.PRICE_P_SECOND_VARIANT).first().textContent();

        StringBuilder finalPrice = new StringBuilder();
        for (char c : price.toCharArray())
            if (Character.isDigit(c) || c == ',' || c == '.')
                finalPrice.append(c);

        return Double.parseDouble(finalPrice.toString().replaceFirst(",", "."));
    }

    private String getCurrency(String link) {
        if (link.startsWith("https://www.zalando.pl/"))
            return "zł";
        if (link.startsWith("https://www.zalando.cz/"))
            return "Kč";
        if (link.startsWith("https://www.zalando.no/") || link.startsWith("https://www,zalando.se/"))
            return "kr";
        if (link.startsWith("https://www.zalando.ro/"))
            return "lei";
        if (link.startsWith("https://www.zalando.ch/"))
            return "CHF";
        if (link.startsWith("https://www.zalando.co.uk/"))
            return "£";
        return "€";
    }

    String getServiceName(String link) {
        String[] arrayLink = link.split("\\.");
        return arrayLink[1] + "." + arrayLink[2].split("/")[0];
    }


    private boolean isLinkValid(Page page) {
        Page.WaitForSelectorOptions wait = new Page.WaitForSelectorOptions();
        wait.setTimeout(4000);
        try {
            page.waitForSelector(Tag.Zalando.MAIN_TAB, wait);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    private boolean isItemOneVariant(Page page) {
        return page.getByTestId(Tag.Zalando.VARIANT_BUTTON).isDisabled();
    }

    private boolean isItemSoldOut(Page page) {
        return page.locator(Tag.Zalando.SOLD_OUT_TAB).count() == 1;
    }

    void clickSizeButton(Page page) {
        page.getByTestId(Tag.Zalando.VARIANT_BUTTON).click();
        page.waitForSelector(Tag.Zalando.ALL_VARIANTS);
    }

    private void clickCookiesButton(Page page) {
        try {
            page.waitForSelector(Tag.Zalando.COOKIES_BUTTON).click();
        } catch (TimeoutError ignored) {
        }
    }


    private List<String> getAllVariants(Page page) {
        return page
                .locator(Tag.Zalando.ALL_VARIANTS)
                .allTextContents();
    }

    private List<String> getAvailableVariants(Page page) {
        return getAvailableVariantsAsLocators(page)
                .stream()
                .map(Locator::textContent)
                .collect(Collectors.toList());
    }

    private List<String> getNonAvailableVariants(Page page) {
        return getNonAvailableVariantsAsLocators(page)
                .stream()
                .map(Locator::textContent)
                .collect(Collectors.toList());
    }

    private List<Locator> getAvailableVariantsAsLocators(Page page) {
        return page
                .locator(Tag.Zalando.AVAILABLE_VARIANTS)
                .all();
    }

    private List<Locator> getNonAvailableVariantsAsLocators(Page page) {
        return page
                .locator(Tag.Zalando.NON_AVAILABLE_VARIANTS)
                .all();
    }

    private boolean isVariantAlreadyChosen(Page page, String variant) {
        return page.getByTestId(Tag.Zalando.VARIANT_BUTTON).innerText().startsWith(variant);
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

    @Override
    public void setBugged(boolean bugged) {
        launchOptions.setHeadless(!bugged);
    }
}
