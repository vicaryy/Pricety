package com.vicary.zalandoscraper.scraper;

import com.microsoft.playwright.*;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.options.WaitUntilState;
import com.vicary.zalandoscraper.entity.ProductEntity;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.scraper.config.DefaultExtraHeaders;
import com.vicary.zalandoscraper.scraper.config.DefaultLaunchOptions;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NikeScraper implements Scraper {
    private final static Logger logger = LoggerFactory.getLogger(NikeScraper.class);
    private final Map<String, String> extraHeaders = new DefaultExtraHeaders();
    private final BrowserType.LaunchOptions launchOptions = new DefaultLaunchOptions();
    private final Page.NavigateOptions navigateOptions = new Page.NavigateOptions().setWaitUntil(WaitUntilState.COMMIT);

    @Override
    public void updateProducts(List<Product> products) {
        try (Playwright playwright = Playwright.create()) {

            try (BrowserContext browser = playwright.chromium().launch(launchOptions).newContext()) {
                browser.newPage();
                browser.setDefaultTimeout(4000);

                for (Product dto : products) {
                    Page newPage = browser.newPage();
                    newPage.setExtraHTTPHeaders(extraHeaders);
                    newPage.navigate(dto.getLink(), navigateOptions);
                    newPage.setDefaultTimeout(4000);

                    updateProduct(newPage, dto);
                }
            }
        }
    }

    void updateProduct(Page page, Product product) {
        try (page) {
            boolean isSoldOut = false;
            boolean isMultiVariant = false;
            boolean isOneVariant = false;
            boolean isItemNotAvailable = false;

            while (!isSoldOut && !isMultiVariant && !isOneVariant && !isItemNotAvailable) {
                isMultiVariant = isMultiVariantVisible(page);
                isSoldOut = isSoldOutVisible(page);
                isOneVariant = isOneVariant(page);
                isItemNotAvailable = isItemNotAvailableVisible(page);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            if (isItemNotAvailable) {
                logger.debug("Product '{}' - is not longer available, delete it", product.getProductId());
                product.setNewPrice(0);
                return;
            }

            if (isSoldOut) {
                logger.debug("Product '{}' - item sold out", product.getProductId());
                product.setNewPrice(0);
                return;
            }

            if (isMultiVariant) {
                if (isVariantAvailable(page, product.getVariant()))
                    product.setNewPrice(getPrice(page));
                else {
                    product.setNewPrice(0);
                    logger.debug("Product '{}' - item variant not available", product.getProductId());
                }
                return;
            }

            product.setNewPrice(getPrice(page));

        } catch (PlaywrightException ex) {
            logger.warn("Failed to update productId '{}'", product.getProductId());
        }
    }

    private boolean isMultiVariantVisible(Page page) {
        return page.isVisible(Tag.Nike.MULTI_VARIANT_TAB);
    }

    private boolean isSoldOutVisible(Page page) {
        return page.isVisible(Tag.Nike.SOLD_OUT_TAB);
    }

    private boolean isOneVariant(Page page) {
        if (page.isVisible(Tag.Nike.ONE_VARIANT_TAB))
            return !page.locator(Tag.Nike.ONE_VARIANT_TAB).textContent().startsWith("Wybierz");

        return false;
    }

    private boolean isItemNotAvailableVisible(Page page) {
        return page.isVisible(Tag.Nike.NOT_AVAILABLE_TAB);
    }


    @Override
    public Product getProduct(String link, String variant) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(launchOptions);
            Page page = browser.newPage();
            page.setDefaultTimeout(10000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link, navigateOptions);

            waitForTitle(page);

            Product product = Product.builder()
                    .name(getName(page))
                    .description(getDescription(page))
                    .photoUrl(getPhotoUrl(page))
                    .price(0)
                    .variant(variant)
                    .link(link)
                    .serviceName("nike.com")
                    .currency("pl")
                    .build();

            if (variant.equals("-oneVariant Unknown"))
                return product;

            if (variant.startsWith("-oneVariant ")) {
                product.setPrice(getPrice(page));
                return product;
            }

            waitForSizes(page, 10000);

            if (!isVariantAvailable(page, variant))
                return product;

            product.setPrice(getPrice(page));
            return product;
        }
    }

    @Override
    public List<String> getAllVariants(String link) {
        try (Playwright playwright = Playwright.create()) {
            BrowserType.LaunchOptions l = new BrowserType.LaunchOptions();
            l.setHeadless(false);
            Browser browser = playwright.chromium().launch(launchOptions);

            Page page = browser.newPage();
            page.setDefaultTimeout(10000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link, navigateOptions);

            if (!isLinkValid(page))
                throw new InvalidLinkException(Messages.scraper("invalidLink"), "User %s specified wrong link: %s".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

            if (waitForSizes(page, 3500))
                return getAllVariantsAsString(page);

            if (isItemSoldOut(page))
                return List.of("-oneVariant Unknown");

            return List.of("-oneVariant " + getVariant(page));
        }
    }


    List<String> getAvailableVariants(String link) {
        try (Playwright playwright = Playwright.create()) {
            BrowserType.LaunchOptions l = new BrowserType.LaunchOptions();
            l.setHeadless(false);
            Browser browser = playwright.chromium().launch(launchOptions);

            Page page = browser.newPage();
            page.setDefaultTimeout(10000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link, navigateOptions);

            if (!isLinkValid(page))
                throw new InvalidLinkException(Messages.scraper("invalidLink"), "User %s specified wrong link: %s".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

            if (waitForSizes(page, 3500))
                return getAvailableVariantsAsString(page);

            if (isItemSoldOut(page))
                return List.of("-oneVariant Unknown");

            return List.of("-oneVariant " + getVariant(page));
        }
    }

    List<String> getNonAvailableVariants(String link) {
        try (Playwright playwright = Playwright.create()) {
            BrowserType.LaunchOptions l = new BrowserType.LaunchOptions();
            l.setHeadless(false);
            Browser browser = playwright.chromium().launch(launchOptions);

            Page page = browser.newPage();
            page.setDefaultTimeout(10000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link, navigateOptions);

            if (!isLinkValid(page))
                throw new InvalidLinkException(Messages.scraper("invalidLink"), "User %s specified wrong link: %s".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

            if (waitForSizes(page, 3500))
                return getNonAvailableVariantsAsString(page);

            if (isItemSoldOut(page))
                return List.of("-oneVariant Unknown");

            return List.of("-oneVariant " + getVariant(page));
        }
    }


    @Override
    public void setBugged(boolean bugged) {
        launchOptions.setHeadless(!bugged);
    }


    private String getVariant(Page page) {
        return page.locator(Tag.Nike.ONE_VARIANT_TAB).textContent();
    }


    private boolean waitForSizes(Page page, int howLong) {
        Page.WaitForSelectorOptions waitForOptions = new Page.WaitForSelectorOptions();
        waitForOptions.setTimeout(howLong);
        try {
            page.waitForSelector(Tag.Nike.MULTI_VARIANT_TAB, waitForOptions);
            return true;
        } catch (TimeoutError ex) {
            return false;
        }
    }


    private boolean isLinkValid(Page page) {
        Locator.WaitForOptions waitForOptions = new Locator.WaitForOptions();
        waitForOptions.setTimeout(4000);
        try {
            page.locator(Tag.Nike.MAIN_TAB).waitFor(waitForOptions);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    private boolean isItemSoldOut(Page page) {
        Locator.WaitForOptions waitForOptions = new Locator.WaitForOptions();
        waitForOptions.setTimeout(1000);
        try {
            page.locator(Tag.Nike.SOLD_OUT_TAB).waitFor(waitForOptions);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private String getName(Page page) {
        return page.locator(Tag.Nike.NAME).last().innerText();
    }

    private String getDescription(Page page) {
        return page.locator(Tag.Nike.DESCRIPTION).last().innerText();
    }

    private String getPhotoUrl(Page page) {
        return page.locator(Tag.Nike.PHOTO_URL).getAttribute("src");
    }

    private double getPrice(Page page) {
        String price = page.locator(Tag.Nike.PRICE).first().innerText().trim();
        String[] priceArray = price.split(" ");

        if (priceArray[0].contains(","))
            priceArray[0] = priceArray[0].replaceAll(",", ".");

        return Double.parseDouble(priceArray[0]);
    }

    private List<String> getAllVariantsAsString(Page page) {
        return page.locator(Tag.Nike.VARIANT_TAB)
                .all()
                .stream()
                .map(e -> e.textContent().trim())
                .toList();
    }

    private List<String> getAvailableVariantsAsString(Page page) {
        List<Locator> locators = page.locator(Tag.Nike.VARIANT_TAB).all();
        List<String> sizes = new ArrayList<>();
        for (Locator l : locators)
            if (l.isEnabled())
                sizes.add(l.textContent().trim());

        return sizes;
    }

    private List<String> getNonAvailableVariantsAsString(Page page) {
        List<Locator> locators = page.locator(Tag.Nike.VARIANT_TAB).all();
        List<String> sizes = new ArrayList<>();
        for (Locator l : locators)
            if (!l.isEnabled())
                sizes.add(l.textContent().trim());

        return sizes;
    }

    private boolean isVariantAvailable(Page page, String variant) {
        List<Locator> locators = page.locator(Tag.Nike.VARIANT_TAB).all();

        for (Locator l : locators) {
            if (l.textContent().trim().equals(variant)) {
                return l.isEnabled();
            }
        }
        return false;
    }

    private void waitForTitle(Page page) {
        Locator.WaitForOptions waitForOptions = new Locator.WaitForOptions();
        waitForOptions.setTimeout(10000);
        page.locator(Tag.Nike.NAME).last().waitFor(waitForOptions);
    }
}
