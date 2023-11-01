package com.vicary.zalandoscraper.service;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScraperPlay {

    private final static Logger logger = LoggerFactory.getLogger(ScraperPlay.class);
    private final Map<String, String> extraHeaders = Map.of("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
    private final BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
    private final Page.NavigateOptions navigateOptions = new Page.NavigateOptions().setWaitUntil(WaitUntilState.COMMIT);


    @PostConstruct
    private void setup() {
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
    }

    @SneakyThrows
    protected List<ProductDTO> updateProducts(List<ProductDTO> DTOs) {
        try (Playwright playwright = Playwright.create()) {

            try (BrowserContext browser = playwright.chromium().launch(launchOptions).newContext()) {
                browser.newPage();
                List<Page> pages = new ArrayList<>();

                for (int i = 0; i < DTOs.size(); i++) {
                    Page newPage = browser.newPage();
                    newPage.setExtraHTTPHeaders(extraHeaders);
                    newPage.navigate(DTOs.get(i).getLink(), navigateOptions);
                    pages.add(newPage);

                    if (i == 0)
                        clickCookiesButton(newPage);

                    if (pages.size() == 10 || i == DTOs.size() - 1) {
                        for (int k = 0; k < pages.size(); k++) {
                            updateProduct(pages.get(k), DTOs.get(i - pages.size() + 1 + k));
                        }
                        pages.clear();
                    }
                }
                return DTOs;
            }
        }
    }

    private void updateProduct(Page page, ProductDTO dto) {
        try (page) {

//            waitForMainPage(page);

            dto.setNewPrice(0);

            if (!isLinkValid(page) || isItemSoldOut(page))
                return;

            if (dto.getVariant().startsWith("-oneVariant")) {
                dto.setNewPrice(getPrice(page));
                return;
            }

            if (isVariantAlreadyChosen(page, dto.getVariant())) {
                dto.setNewPrice(getPrice(page));
                return;
            }

            clickSizeButton(page);

            if (!clickAvailableVariant(getAvailableVariantsAsLocators(page), dto.getVariant()))
                return;

            dto.setNewPrice(getPrice(page));

        } catch (PlaywrightException ex) {
            ex.printStackTrace();
            logger.warn("Failed to update productId '{}'", dto.getProductId());
        }
    }

    private void waitForMainPage(Page page) {
        page.waitForSelector("div._5qdMrS.VHXqc_.rceRmQ._4NtqZU.mIlIve");
    }

    public Product getProduct(String link, String variant) {
        try (Playwright playwright = Playwright.create()) {
            try (Browser browser = playwright.chromium().launch()) {
                Page page = browser.newPage();
                page.setDefaultTimeout(3000);
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
            }
        }
    }


    public List<String> getAllVariants(String link) {
        try (Playwright playwright = Playwright.create()) {
            try (Browser browser = playwright.chromium().launch()) {
                Page page = browser.newPage();
                page.setDefaultTimeout(3000);
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


    private String getOneVariantName(Page page) {
        return page.locator("span.sDq_FX._2kjxJ6.dgII7d.Yb63TQ").innerText();
    }

    private String getName(Page page) {
        return page.locator("span._ZDS_REF_SCOPE_._5FHGm_").innerText();
    }

    private String getDescription(Page page) {
        return page.locator("span.EKabf7.R_QwOV").innerText();
    }

    private double getPrice(Page page) {
        String price = page.locator("span.sDq_FX._4sa1cA").textContent();
        //sDq_FX _4sa1cA dgII7d Km7l2y _65i7kZ

        if (price.contains(" "))
            price.replaceAll(" ", "");

        if (price.startsWith("od"))
            price = price.substring(3);

        price = price.substring(0, price.length() - 3);

        if (price.contains(" "))
            price = price.replaceAll(" ", "");

        return Double.parseDouble(price.replaceFirst(",", "."));
    }


    private boolean isLinkValid(Page page) {
        return page.locator("div._5qdMrS.VHXqc_.rceRmQ._4NtqZU.mIlIve").count() > 0;
    }


    private boolean isItemOneVariant(Page page) {
        return page.getByTestId("pdp-size-picker-trigger").isDisabled();
    }

    private boolean isItemSoldOut(Page page) {
        return page.getByText("Artykuł wyprzedany").count() == 1;
    }

    private void clickSizeButton(Page page) {
        page.getByTestId("pdp-size-picker-trigger").click();
        page.waitForSelector("span.hDNRPv.r9BRio.qXofat.ZkIJC-.pMa0tB.JCuRr_ > div._0xLoFW.FCIprz > span.sDq_FX._2kjxJ6.dgII7d");
    }

    private void clickCookiesButton(Page page) {
        try {
            page.waitForSelector("button.uc-btn").click();
        } catch (TimeoutError ignored) {
        }
    }


    private List<String> getAllVariants(Page page) {
        return page
                .locator("span.hDNRPv.r9BRio.qXofat.ZkIJC-.pMa0tB.JCuRr_ > div._0xLoFW.FCIprz > span.sDq_FX._2kjxJ6.dgII7d")
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
                .locator("span.hDNRPv.r9BRio.qXofat.ZkIJC-.pMa0tB.JCuRr_ > div._0xLoFW.FCIprz > span.sDq_FX._2kjxJ6.dgII7d.HlZ_Tf")
                .all();
    }

    private boolean isVariantAlreadyChosen(Page page, String variant) {
        return page.getByTestId("pdp-size-picker-trigger").innerText().startsWith(variant);
    }

    private boolean clickAvailableVariant(List<Locator> locators, String variant) {
        for (Locator l : locators)
            if (l.textContent().startsWith(variant)) {
                l.click();
                return true;
            }
        return false;
    }
}
