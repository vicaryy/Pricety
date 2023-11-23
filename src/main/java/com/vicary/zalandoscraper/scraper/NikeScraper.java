package com.vicary.zalandoscraper.scraper;

import com.microsoft.playwright.*;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.options.WaitUntilState;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class NikeScraper implements Scraper {
    private final static Logger logger = LoggerFactory.getLogger(NikeScraper.class);
    private final Map<String, String> extraHeaders = Map.of("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
    private final BrowserType.LaunchOptions launchOptions = new DefaultLaunchOptions();
    private final Page.NavigateOptions navigateOptions = new Page.NavigateOptions().setWaitUntil(WaitUntilState.COMMIT);

    @Override
    public void updateProducts(List<ProductDTO> DTOs) {
        try (Playwright playwright = Playwright.create()) {

            try (BrowserContext browser = playwright.chromium().launch(launchOptions).newContext()) {
                browser.newPage();
                browser.setDefaultTimeout(4000);

                for (int i = 0; i < DTOs.size(); i++) {
                    Page newPage = browser.newPage();
                    newPage.setExtraHTTPHeaders(extraHeaders);
                    newPage.navigate(DTOs.get(i).getLink(), navigateOptions);
                    newPage.setDefaultTimeout(4000);

                    updateProduct(newPage, DTOs.get(i));
                }
            }
        }
    }

    private void updateProduct(Page page, ProductDTO dto) {
        try (page) {
//            waitForContent(page);

            if (!isLinkValid(page)) {
                logger.warn("Product '{}' - link invalid, probably needs to be deleted.", dto.getProductId());
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


            if (!clickAvailableVariant(page, getAllAvailableVariants(page), dto.getVariant())) {
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
                    .price(0)
                    .variant(variant)
                    .link(page.url())
                    .build();


            if (isItemSoldOut(page)) {
                return product;
            }

            if (variant.startsWith("-oneVariant")) {
                product.setPrice(getPrice(page));
                return product;
            }

            if (!clickAvailableVariant(page, getAllAvailableVariants(page), variant)) {
                return product;
            }

            product.setDescription(getDescription(page));
            product.setPrice(getPrice(page));

            return product;
        }
    }

    @Override
    @SneakyThrows
    public List<String> getAllVariants(String link) {
        System.out.println(ActiveLanguage.get().getResourceBundle());
        try (Playwright playwright = Playwright.create()) {
            BrowserType.LaunchOptions l = new BrowserType.LaunchOptions();
            l.setHeadless(false);
            Browser browser = playwright.chromium().launch(l);

            Page page = browser.newPage();
            page.setDefaultTimeout(10000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link);

            return getAllVariants(page);
        }
    }

    List<String> getAllVariants(Page page) {
        if (!isLinkValid(page))
            throw new InvalidLinkException(Messages.scraper("invalidLink"), "User %s specified wrong link: %s".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));
//
//
//            if (isMultiVariant(page))
//                return getAllVariantsAsString(page);

//            Thread.sleep(500);
        return List.of("-oneVariant One Variant");
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
            page.locator("div.css-mso6zd").waitFor(waitForOptions);
            return true;
        } catch (Exception ex) {
            return false;
        }
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

    private List<Locator> getAllAvailableVariants(Page page) {
        return page.locator("div.swatch__item--selectable").all();
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
