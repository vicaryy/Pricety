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
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class HebeScraper implements Scraper {
    private final static Logger logger = LoggerFactory.getLogger(HebeScraper.class);
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
            waitForContent(page);

            if (!isLinkValid(page)) {
                logger.warn("Product '{}' - link invalid, probably needs to be deleted.", product.getProductId());
                product.setNewPrice(0);
                return;
            }

            if (isSoldOut(page)) {
                logger.debug("Product '{}' - item sold out", product.getProductId());
                product.setNewPrice(0);
                return;
            }

            if (product.getVariant().startsWith("-oneVariant")) {
                product.setNewPrice(getPrice(page));
                return;
            }


            if (!clickAvailableVariant(page, getAvailableVariants(page), product.getVariant())) {
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
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();
            page.setDefaultTimeout(10000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link, navigateOptions);

            waitForContent(page);

            Product product = Product.builder()
                    .name(getName(page))
                    .description(getDescription(page))
                    .photoUrl(getPhotoUrl(page))
                    .price(0)
                    .variant(variant)
                    .link(link)
                    .serviceName("hebe.pl")
                    .currency("zł")
                    .build();


            if (isSoldOut(page)) {
                return product;
            }

            if (variant.startsWith("-oneVariant")) {
                product.setPrice(getPrice(page));
                return product;
            }

            if (!clickAvailableVariant(page, getAvailableVariants(page), variant)) {
                return product;
            }

            product.setDescription(getDescription(page));
            product.setPrice(getPrice(page));

            return product;
        }
    }


    @Override
    public List<String> getAllVariants(String link) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(launchOptions);

            Page page = browser.newPage();
            page.setDefaultTimeout(10000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link, navigateOptions);


            if (!isLinkValid(page))
                throw new InvalidLinkException(Messages.scraper("invalidLink"), "User %s specified wrong link: %s".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

            waitForContent(page);

            if (isMultiVariant(page))
                return getAllVariantsAsString(page);

            return List.of("-oneVariant One Variant");
        }
    }

    List<String> getAvailableVariants(String link) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(launchOptions);

            Page page = browser.newPage();
            page.setDefaultTimeout(10000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link, navigateOptions);

            if (!isLinkValid(page))
                throw new InvalidLinkException(Messages.scraper("invalidLink"), "User %s specified wrong link: %s".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

            if (isMultiVariant(page))
                return getAvailableVariantsAsString(page);

            return List.of("-oneVariant One Variant");
        }
    }

    List<String> getNonAvailableVariants(String link) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(launchOptions);

            Page page = browser.newPage();
            page.setDefaultTimeout(10000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link, navigateOptions);

            if (!isLinkValid(page))
                throw new InvalidLinkException(Messages.scraper("invalidLink"), "User %s specified wrong link: %s".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

            if (isMultiVariant(page))
                return getNonAvailableVariantsAsString(page);

            return List.of("-oneVariant One Variant");
        }
    }

    @Override
    public void setBugged(boolean bugged) {
        launchOptions.setHeadless(!bugged);
    }

    @SneakyThrows
    private boolean clickAvailableVariant(Page page, List<Locator> availableVariants, String variant) {
        String description = getDescription(page);
        for (Locator l : availableVariants) {
            String locatorVariant = l.textContent().trim();
            if (variant.equals(locatorVariant)) {
                l.click();
                waitForPriceUpdate(page, description);
                return true;
            }
        }
        return false;
    }

    @SneakyThrows
    private void waitForPriceUpdate(Page page, String description) {
        long time = System.currentTimeMillis();
        while (getDescription(page).equals(description)) {
            Thread.sleep(30);
            if (System.currentTimeMillis() - time > 2000) {
                logger.info("Timeout in waiting for price update after click, probably item was already chosen: " + page.url());
                break;
            }
        }
    }


    private boolean isLinkValid(Page page) {
        Locator.WaitForOptions waitForOptions = new Locator.WaitForOptions();
        waitForOptions.setTimeout(2000);
        try {
            page.locator(Tag.Hebe.MAIN_TAB).waitFor(waitForOptions);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean isMultiVariant(Page page) {
        return page.isVisible(Tag.Hebe.MULTI_VARIANT_TAB);
    }

    private boolean isSoldOut(Page page) {
        Locator.WaitForOptions wait = new Locator.WaitForOptions();
        wait.setTimeout(2000);
        try {
            page.locator(".js-non-available.add-to-cart.add-product-detail.add-product-detail--outline.js-subscribe").waitFor(wait);
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    private String getName(Page page) {
        String name = page.locator(Tag.Hebe.NAME).innerText().replace("\n", " ");
        if (name.contains("\n"))
            name = name.replaceAll("\n", " ");
        return name;
    }

    private String getDescription(Page page) {
        return page.locator(Tag.Hebe.DESCRIPTION).innerText();
    }

    private String getPhotoUrl(Page page) {
        List<Locator> locators = page.locator(Tag.Hebe.PHOTO_URL).all();
        locators = locators.stream()
                .filter(e -> e.getAttribute("srcset") != null)
                .toList();
        return locators.get(0).getAttribute("srcset");
    }

    private double getPrice(Page page) {
        String price = page.locator(Tag.Hebe.PRICE).innerText().trim();

        String[] priceArray = price.split("\n");

        if (priceArray[0].contains("."))
            priceArray[0] = priceArray[0].replaceAll("\\.", "");

        return Double.parseDouble(priceArray[0] + "." + priceArray[1]);
    }


    private List<Locator> getAvailableVariants(Page page) {
        return page.locator(Tag.Hebe.AVAILABLE_VARIANTS).all();
    }

    private List<Locator> getNonAvailableVariants(Page page) {
        return page.locator(Tag.Hebe.NON_AVAILABLE_VARIANTS).all();
    }


    private List<String> getAllVariantsAsString(Page page) {
        return page.locator(Tag.Hebe.ALL_VARIANTS)
                .all()
                .stream()
                .map(e -> e.textContent().trim())
                .toList();
    }

    private List<String> getAvailableVariantsAsString(Page page) {
        return getAvailableVariants(page)
                .stream()
                .map(e -> e.textContent().trim())
                .toList();
    }

    private List<String> getNonAvailableVariantsAsString(Page page) {
        return getNonAvailableVariants(page)
                .stream()
                .map(e -> e.textContent().trim())
                .toList();
    }

    private void waitForContent(Page page) {
        page.waitForSelector(Tag.Hebe.WAIT_FOR_CONTENT);
    }
}
