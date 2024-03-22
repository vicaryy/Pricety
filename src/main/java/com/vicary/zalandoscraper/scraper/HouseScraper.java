package com.vicary.zalandoscraper.scraper;

import com.microsoft.playwright.*;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.options.WaitUntilState;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.scraper.config.DefaultExtraHeaders;
import com.vicary.zalandoscraper.scraper.config.DefaultLaunchOptions;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class HouseScraper implements Scraper {
    private final static Logger logger = LoggerFactory.getLogger(HouseScraper.class);
    private final Map<String, String> extraHeaders = new DefaultExtraHeaders();
    private final BrowserType.LaunchOptions launchOptions = new DefaultLaunchOptions();
    private final Page.NavigateOptions navigateOptions = new Page.NavigateOptions().setWaitUntil(WaitUntilState.COMMIT);

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

    void updateProduct(Page page, Product product) {
        try (page) {
            if (!isLinkValid(page)) {
                logger.info("Product '{}' is not available anymore, delete it.", product.getProductId());
                product.setNewPrice(0);
                return;
            }

            if (isItemSoldOut(page)) {
                logger.info("Product '{}' sold out.", product.getProductId());
                product.setNewPrice(0);
                return;
            }

            if (product.getVariant().startsWith("-oneVariant ")) {
                product.setNewPrice(getPrice(page));
                return;
            }

            if (!isVariantAvailable(page, product.getVariant())) {
                logger.info("Product '{}' variant '{}' not available.", product.getProductId(), product.getVariant());
                product.setNewPrice(0);
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
            page.navigate(link, navigateOptions);

            clickCookiesButton(page);

            Product product = Product.builder()
                    .name(getName(page))
                    .description("-")
                    .photoUrl(getPhotoUrl(page))
                    .price(0)
                    .variant(variant)
                    .link(page.url())
                    .serviceName("housebrand.com")
                    .currency(getCurrency(page))
                    .build();

            if (isItemSoldOut(page)) {
                logger.info("Product sold out.");
                return product;
            }

            if (variant.startsWith("-oneVariant ")) {
                logger.info("One variant item.");
                product.setPrice(getPrice(page));
                return product;
            }

            if (!isVariantAvailable(page, variant)) {
                logger.info("Variant not available.");
                return product;
            }

            logger.info("Variant available.");
            product.setPrice(getPrice(page));
            return product;
        }
    }

    private boolean isVariantAvailable(Page page, String variant) {
        return getAvailableVariantsAsString(page).stream().anyMatch(variant::equals);
    }

    private boolean isItemSoldOut(Page page) {
        return page.getByTestId(Tag.House.SOLD_OUT_TAB).isVisible();
    }

    private double getPrice(Page page) {
        String price = page.locator(Tag.House.PRICE).first().textContent();

        StringBuilder sbFinalPrice = new StringBuilder();
        for (char c : price.toCharArray())
            if (Character.isDigit(c) || c == ',' || c == '.')
                sbFinalPrice.append(c);

        String finalPrice = sbFinalPrice.toString();
        if (finalPrice.contains(","))
            finalPrice = finalPrice.replaceFirst(",", ".");

        return Double.parseDouble(finalPrice);
    }

    String getCurrency(Page page) {
        return page.locator(Tag.House.CURRENCY).first().textContent();
    }

    private String getName(Page page) {
        return page.locator(Tag.House.NAME).first().textContent().trim();
    }

    private String getPhotoUrl(Page page) {
        return page.locator(Tag.House.PHOTO_URL).getAttribute("src");
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
                throw new InvalidLinkException(Messages.scraper("invalidLink"), "User %s specified wrong link: %s".formatted(ActiveUser.get().getTelegramId(), ActiveUser.get().getText()));


            if (isMultiVariant(page))
                return getVariantsAsString(page);

            return List.of("-oneVariant " + getOneVariant(page));
        }
    }

    List<String> getAvailableVariants(String link) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(launchOptions);

            Page page = browser.newPage();
            page.setDefaultTimeout(14000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link);

            clickCookiesButton(page);

            return getAvailableVariantsAsString(page);
        }
    }

    List<String> getNonAvailableVariants(String link) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(launchOptions);

            Page page = browser.newPage();
            page.setDefaultTimeout(14000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link);

            clickCookiesButton(page);

            return getNonAvailableVariantsAsString(page);
        }
    }

    private String getOneVariant(Page page) {
        return page.getByTestId(Tag.House.ONE_VARIANT).first().textContent().trim();
    }

    private void clickCookiesButton(Page page) {
        page.waitForSelector(Tag.House.COOKIES_BUTTON).click();
    }

    private List<String> getVariantsAsString(Page page) {
        return page.locator(Tag.House.ALL_VARIANTS_TAB).all().stream()
                .map(l -> l.textContent().trim())
                .toList();
    }

    private List<String> getAvailableVariantsAsString(Page page) {
        return page.getByTestId(Tag.House.AVAILABLE_VARIANTS_TAB).all().stream()
                .map(l -> l.textContent().trim())
                .toList();
    }

    private List<String> getNonAvailableVariantsAsString(Page page) {
        return page.getByTestId(Tag.House.NON_AVAILABLE_VARIANTS_TAB).all().stream()
                .map(l -> l.textContent().trim())
                .toList();
    }

    private boolean isMultiVariant(Page page) {
        return !page.locator(Tag.House.ALL_VARIANTS_TAB).all().isEmpty();
    }

    private boolean isLinkValid(Page page) {
        Page.WaitForSelectorOptions wait = new Page.WaitForSelectorOptions();
        wait.setTimeout(4000);
        try {
            page.waitForSelector(Tag.House.PRODUCT_TAB, wait);
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
