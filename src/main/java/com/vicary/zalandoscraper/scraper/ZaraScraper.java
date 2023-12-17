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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ZaraScraper implements Scraper {

    private final static Logger logger = LoggerFactory.getLogger(ZaraScraper.class);
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
                logger.warn("Product '{}' - link invalid, probably needs to be deleted.", product.getProductId());
                product.setNewPrice(0);
                return;
            }

            if (product.getVariant().startsWith("-oneVariant")) {
                if (isItemAvailable(page))
                    product.setNewPrice(getPrice(page));
                else {
                    logger.info("Product '{}' - -oneVariant item not available", product.getProductId());
                    product.setNewPrice(0);
                }
                return;
            }

            String color = product.getVariant().split("\\+")[0].trim();
            String productVariant = product.getVariant().split("\\+")[1].trim();

            if (color.equals("One Color")) {
                if (isVariantAvailable(page, productVariant))
                    product.setNewPrice(getPrice(page));
                else {
                    logger.info("Product '{}' - one color item not available", product.getProductId());
                    product.setNewPrice(0);
                }
                return;
            }

            if (color.equals("No Color")) {
                product.setNewPrice(getPrice(page));
                return;
            }

            try {
                clickColor(page, color);
            } catch (PlaywrightException ex) {
                logger.info("Product '{}' - multi-color and multi-variant, color not available", product.getProductId());
                product.setNewPrice(0);
                return;
            }

            if (!isItemAvailable(page)) {
                logger.info("Product '{}' - multi-color and multi-variant, color not available", product.getProductId());
                product.setNewPrice(0);
                return;
            }

            if (!productVariant.equals("One Variant") && !isVariantAvailable(page, productVariant)) {
                logger.info("Product '{}' - multi-color and multi-variant, variant not available", product.getProductId());
                product.setNewPrice(0);
                return;
            }

            product.setNewPrice(getPrice(page));
        } catch (PlaywrightException ex) {
            logger.warn("Failed to update product '{}'", product.getProductId());
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

            waitForMainContent(page);

            Product product = Product.builder()
                    .name(getName(page))
                    .description("-")
                    .price(0)
                    .variant(variant)
                    .link(link)
                    .serviceName("zara.com")
                    .currency(getCurrency(page))
                    .build();

            if (variant.startsWith("-oneVariant")) {
                product.setPrice(getPrice(page));
                return product;
            }

            String color = variant.split("\\+")[0].trim();
            String productVariant = variant.split("\\+")[1].trim();

            if (color.equals("One Color")) {
                if (isVariantAvailable(page, productVariant))
                    product.setPrice(getPrice(page));
                return product;
            }

            if (color.equals("No Color")) {
                product.setPrice(getPrice(page));
                return product;
            }

            clickColor(page, color);

            if (!isItemAvailable(page))
                return product;

            if (!productVariant.equals("One Variant") && !isVariantAvailable(page, productVariant))
                return product;

            product.setPrice(getPrice(page));

            return product;
        }
    }

    private boolean isItemAvailable(Page page) {
        return page.isVisible(Tag.Zara.ADD_TO_CART_BUTTON);
    }

    private boolean isVariantAvailable(Page page, String productVariant) {
        if (getAllMultiVariantsAsString(page).stream().noneMatch(productVariant::equals))
            return false;
        return getNonAvailableMultiVariantsAsString(page).stream().noneMatch(productVariant::equals);
    }

    private void clickColor(Page page, String color) {
        List<Locator> allMultiColors = getAllMultiColors(page);
        String currentColor = getCurrentColorName(page);
        if (color.equals(currentColor))
            return;

        for (Locator l : allMultiColors) {
            if (color.equals(l.textContent().trim())) {
                long timeout = System.currentTimeMillis();
                while (getCurrentColorName(page).equals(currentColor)) {
                    l.click();
                    sleep(20);
                    if (System.currentTimeMillis() - timeout > 10000)
                        throw new PlaywrightException("Color not found.");
                }
                return;
            }
        }
        throw new PlaywrightException("Color not found.");
    }

    private double getPrice(Page page) {
        String price = page.locator(Tag.Zara.PRICE).first().textContent().trim();

        StringBuilder sbFinalPrice = new StringBuilder();
        for (char c : price.toCharArray())
            if (Character.isDigit(c) || c == ',' || c == '.')
                sbFinalPrice.append(c);

        String finalPrice = sbFinalPrice.toString();
        if (finalPrice.contains(","))
            finalPrice = finalPrice.replaceFirst(",", ".");

        return Double.parseDouble(finalPrice);
    }

    private String getCurrency(Page page) {
        String[] currencyArray = page.locator(Tag.Zara.PRICE).first().textContent().trim().split(" ");
        return currencyArray[currencyArray.length - 1];
    }

    private String getName(Page page) {
        return page.locator(Tag.Zara.NAME).first().textContent().trim();
    }

    private void waitForMainContent(Page page) {
        page.waitForSelector(Tag.Zara.PRODUCT_TAB);
    }

    @Override
    public List<String> getAllVariants(String link) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(launchOptions);

            Page page = browser.newPage();
            page.setDefaultTimeout(10000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link);

            if (!isLinkValid(page))
                throw new InvalidLinkException(Messages.scraper("invalidLink"), "User %s specified wrong link: %s".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));

            boolean multiColor = isMultiColor(page);
            boolean multiVariant = isMultiVariant(page);

            if (multiColor && multiVariant)
                return getAllMultiColorsAndVariantsAsString(page);

            if (multiColor)
                return getAllMultiColorsWithOneVariantAsString(page);

            if (multiVariant)
                return getAllMultiVariantsWithOneColorAsString(page);


            logger.info("One Variant item");
            return List.of("-oneVariant One Variant");
        }
    }

    private void clickInternationalButton(Page page) {
        if (page.isVisible(Tag.Zara.INTERNATIONAL_EXIT_BUTTON)) {
            page.locator(Tag.Zara.INTERNATIONAL_EXIT_BUTTON).click();
        }
    }

    private List<String> getAllMultiColorsAndVariantsAsString(Page page) {
        List<String> all = new ArrayList<>();
        for (Locator l : getAllMultiColors(page)) {
            tryToClick(page, l, 10);
            String colorName = getColorName(l);
            for (String s : getAllMultiVariantsAsString(page))
                all.add(colorName + " + " + s);
        }
        return all;
    }

    private void tryToClick(Page page, Locator l, int timesOfTries) {
        Locator.ClickOptions clickOptions = new Locator.ClickOptions();
        clickOptions.setTimeout(100);
        for (int i = 0; i < timesOfTries; i++) {
            try {
                l.click(clickOptions);
                break;
            } catch (Exception ex) {
                clickInternationalButton(page);
            }
        }
    }

    private String getColorName(Locator l) {
        String s = l.textContent().trim();
        return !s.isEmpty() ? s : "No Color";
    }

    private String getCurrentColorName(Page page) {
        String currentColorName = page.locator(Tag.Zara.CURRENT_COLOR).innerText();
        StringBuilder sb = new StringBuilder();

        boolean start = false;
        for (char ch : currentColorName.toCharArray()) {
            if (ch == '|')
                break;

            if (start)
                sb.append(ch);

            if (ch == ':')
                start = true;
        }
        return sb.toString().trim();
    }

    private List<String> getAllMultiVariantsAsString(Page page) {
        return getAllMultiVariants(page).stream()
                .map(l -> l.locator(Tag.Zara.ALL_VARIANTS_TEXT_TAB).first().textContent().trim())
                .toList();
    }

    private List<String> getNonAvailableMultiVariantsAsString(Page page) {
        return getNonAvailableMultiVariants(page).stream()
                .map(l -> l.locator(Tag.Zara.ALL_VARIANTS_TEXT_TAB).first().textContent().trim())
                .toList();
    }

    private List<String> getAllMultiVariantsWithOneColorAsString(Page page) {
        return getAllMultiVariants(page).stream()
                .map(l -> "One Color" + " + " + l.locator(Tag.Zara.ALL_VARIANTS_TEXT_TAB).first().textContent().trim())
                .toList();
    }

    private List<Locator> getAllMultiVariants(Page page) {
        return page.locator(Tag.Zara.ALL_VARIANTS_TAB).all();
    }

    private List<Locator> getNonAvailableMultiVariants(Page page) {
        return page.locator(Tag.Zara.NON_AVAILABLE_VARIANTS_TAB).all();
    }

    private List<String> getAllMultiColorsWithOneVariantAsString(Page page) {
        return getAllMultiColors(page).stream()
                .map(l -> {
                    String s = l.innerText().trim();
                    return !s.isEmpty() ? s + " + " + "One Variant" : "No Color + One Variant";
                })
                .toList();
    }

    private List<Locator> getAllMultiColors(Page page) {
        return page.locator(Tag.Zara.ALL_COLORS_TAB).all();
    }

    private boolean isMultiColor(Page page) {
        return page.isVisible(Tag.Zara.ALL_COLORS_TAB);
    }

    private boolean isMultiVariant(Page page) {
        return page.isVisible(Tag.Zara.ALL_VARIANTS_TAB);
    }

    private boolean isLinkValid(Page page) {
        Page.WaitForSelectorOptions wait = new Page.WaitForSelectorOptions();
        wait.setTimeout(4000);
        try {
            page.waitForSelector(Tag.Zara.PRODUCT_TAB, wait);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setBugged(boolean bugged) {
        launchOptions.setHeadless(!bugged);
    }
}
