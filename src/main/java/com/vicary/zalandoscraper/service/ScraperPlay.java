package com.vicary.zalandoscraper.service;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class ScraperPlay {

    private final Playwright playwright = Playwright.create();
    private final Map<String, String> extraHeaders = Map.of("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @SneakyThrows
    public void playwright() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page itemPage = browser.newPage();
        itemPage.navigate("https://www.zalando.pl/vans-t-shirt-z-nadrukiem-light-beige-va222o0pr-b11.html");
        clickCookiesButton(itemPage);
        System.out.println("Is link valid: " + isLinkValid(itemPage));
        System.out.println("Sold out: " + isItemSoldOut(itemPage));
        System.out.println("One variant: " + isItemOneVariant(itemPage));
        System.out.println("Is already chosen: " + isVariantAlreadyChosen(itemPage, "L"));

        clickSizeButton(itemPage);
        System.out.println(getAllVariants(itemPage));
        System.out.println(getAvailableVariants(itemPage));
        System.out.println("Name: " + getName(itemPage));
        System.out.println("Desc: " + getDescription(itemPage));
        System.out.println("Price: " + getPrice(itemPage));

        Thread.sleep(3000);
        browser.close();
        playwright.close();
    }

    @SneakyThrows
    protected List<ProductDTO> updateProducts(List<ProductDTO> DTOs) {
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
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
        launchOptions.setHeadless(false);
        try (BrowserContext browser = playwright.chromium().launch(launchOptions).newContext()) {
            List<Page> pages = new ArrayList<>();

            Page page = browser.newPage();
            page.setDefaultTimeout(5000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(DTOs.get(0).getLink());
            pages.add(page);

            clickCookiesButton(page);

            for (ProductDTO p : DTOs) {
                if (p.getProductId().equals(DTOs.get(0).getProductId()))
                    continue;

                Page newPage = browser.newPage();
                newPage.setDefaultTimeout(10000);
                newPage.setExtraHTTPHeaders(extraHeaders);
                newPage.navigate(p.getLink(), new Page.NavigateOptions().setWaitUntil(WaitUntilState.COMMIT));
                pages.add(newPage);
            }


            for (int i = 0; i < DTOs.size(); i++) {
                // opening new windows and adding windows IDs to list
                webDriver.switchTo().newWindow(WindowType.TAB).get(DTOs.get(i).getLink());
                windowHandles.add(webDriver.getWindowHandle());


                if (i == 0)
                    clickCookiesButton(driverWait);

                // if list with IDs has 10 elements or DTOs is last element
                if (windowHandles.size() == 10 || i == DTOs.size() - 1) {

                    for (int k = 0; k < windowHandles.size(); k++) {

                        double newPrice = 0;
                        ProductDTO dto = DTOs.get(i - windowHandles.size() + 1 + k);
                        dto.setNewPrice(newPrice);

                        try {

                            // switching window to first windows in list
                            webDriver.switchTo().window(windowHandles.get(k));

                            if (!isLinkValidate(webDriver) || isItemSoldOut(webDriver)) {
                                webDriver.close();
                                continue;
                            }

                            if (dto.getVariant().contains("-oneVariant")) {
                                dto.setNewPrice(getPrice(driverWait));
                                webDriver.close();
                                continue;
                            }

                            if (isVariantAlreadyChosen(webDriver, dto.getVariant())) {
                                dto.setNewPrice(getPrice(driverWait));
                                webDriver.close();
                                continue;
                            }

                            clickSizeButton(driverWait);

                            List<WebElement> availableSizes = getAvailableSizes(driverWait);

                            boolean sizeAvailable = false;
                            for (WebElement e : availableSizes) {
                                if (e.getText().equals(dto.getVariant())) {
                                    e.click();
                                    sizeAvailable = true;
                                    break;
                                }
                            }

                            if (!sizeAvailable) {
                                webDriver.close();
                                continue;
                            }

                            dto.setNewPrice(getPrice(driverWait));
                            webDriver.close();

                        } catch (WebDriverException ex) {
                            logger.warn("Error while updating productId '{}': {}", dto.getProductId(), ex.getMessage());
                            dto.setNewPrice(dto.getPrice());
                        }
                    }
                    windowHandles.clear();
                    webDriver.switchTo().window(mainWindow);
                }
            }



            Thread.sleep(80000);



            return null;
        }
    }

    public void waitForMainPage(Page page) {
        page.waitForSelector("div._5qdMrS.VHXqc_.rceRmQ._4NtqZU.mIlIve");
    }

    public Product getProduct(String link, String variant) {
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


    public List<String> getAllVariants(String link) {
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


    public String getOneVariantName(Page page) {
        return page.locator("span.sDq_FX._2kjxJ6.dgII7d.Yb63TQ").innerText();
    }

    public String getName(Page page) {
        return page.locator("span._ZDS_REF_SCOPE_._5FHGm_").innerText();
    }

    public String getDescription(Page page) {
        return page.locator("span.EKabf7.R_QwOV").innerText();
    }

    public double getPrice(Page page) {
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


    public boolean isLinkValid(Page page) {
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("/Users/vicary/desktop/screenshot1.png")));
        return page.locator("div._5qdMrS.VHXqc_.rceRmQ._4NtqZU.mIlIve").count() > 0;
    }


    public boolean isItemOneVariant(Page page) {
        return page.getByTestId("pdp-size-picker-trigger").isDisabled();
    }

    public boolean isItemSoldOut(Page page) {
        return page.getByText("Artykuł wyprzedany").count() == 1;
    }

    @SneakyThrows
    public void clickSizeButton(Page page) {
        page.getByTestId("pdp-size-picker-trigger").click();
        page.waitForSelector("span.hDNRPv.r9BRio.qXofat.ZkIJC-.pMa0tB.JCuRr_ > div._0xLoFW.FCIprz > span.sDq_FX._2kjxJ6.dgII7d");
    }

    public void clickCookiesButton(Page page) {
        try {
            page.waitForSelector("button.uc-btn").click();
        } catch (TimeoutError ignored) {
        }
    }


    public List<String> getAllVariants(Page page) {
        return page
                .locator("span.hDNRPv.r9BRio.qXofat.ZkIJC-.pMa0tB.JCuRr_ > div._0xLoFW.FCIprz > span.sDq_FX._2kjxJ6.dgII7d")
                .allTextContents();
    }

    public List<String> getAvailableVariants(Page page) {
        return getAvailableVariantsAsLocators(page)
                .stream()
                .map(Locator::textContent)
                .collect(Collectors.toList());
    }

    public List<Locator> getAvailableVariantsAsLocators(Page page) {
        return page
                .locator("span.hDNRPv.r9BRio.qXofat.ZkIJC-.pMa0tB.JCuRr_ > div._0xLoFW.FCIprz > span.sDq_FX._2kjxJ6.dgII7d.HlZ_Tf")
                .all();
    }

    public boolean isVariantAlreadyChosen(Page page, String variant) {
        return page.getByTestId("pdp-size-picker-trigger").innerText().startsWith(variant);
    }

    public boolean clickAvailableVariant(List<Locator> locators, String variant) {
        for (Locator l : locators)
            if (l.textContent().startsWith(variant)) {
                l.click();
                return true;
            }
        return false;
    }
}
