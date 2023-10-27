package com.vicary.zalandoscraper.service;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.tag.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Scraper {

    private final static Logger logger = LoggerFactory.getLogger(Scraper.class);

    private final ChromeOptions options;


    @SneakyThrows
    protected List<ProductDTO> updateProducts(List<ProductDTO> DTOs) {

        long currentTime = System.currentTimeMillis();

        WebDriver webDriver = null;

        try {
            // setting webDriver
            webDriver = new ChromeDriver(options);
            WebDriverWait driverWait = new WebDriverWait(webDriver, Duration.ofSeconds(3L));

            // creating windowHandles IDs
            List<String> windowHandles = new ArrayList<>();
            String mainWindow = webDriver.getWindowHandle();
            for (int i = 0; i < DTOs.size(); i++) {

                // opening new windows and adding windows IDs to list
                webDriver.switchTo().newWindow(WindowType.TAB).get(DTOs.get(i).getLink());
                windowHandles.add(webDriver.getWindowHandle());

                if (i == 0)
                    clickCookiesButton(driverWait);

                // if list with IDs has 10 elements or DTOs is last element
                if (windowHandles.size() == 23 || i == DTOs.size() - 1) {

                    for (int k = 0; k < windowHandles.size(); k++) {
                        // switching window to first windows in list
                        webDriver.switchTo().window(windowHandles.get(k));

                        double newPrice = 0;
                        ProductDTO dto = DTOs.get(i - windowHandles.size() + 1 + k);
                        dto.setNewPrice(newPrice);

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
                    }
                    windowHandles.clear();
                    webDriver.switchTo().window(mainWindow);
                }
            }
        } catch (WebDriverException ex) {
            logger.error("Error while updating products: " + ex.getMessage());
            throw new RuntimeException();
        } finally {
            assert webDriver != null;
            webDriver.quit();
        }

        System.out.println("It takes me: " + (System.currentTimeMillis() - currentTime) / 1000 + " seconds");
        return DTOs;
    }


    public Product getProduct(String URL, String variant) {
        WebDriver webDriver = null;

        try {
            webDriver = new ChromeDriver(options);
            WebDriverWait driverWait = new WebDriverWait(webDriver, Duration.ofSeconds(3));

            webDriver.get(URL);

            clickCookiesButton(driverWait);

            String name = getName(webDriver);
            String description = getDescription(webDriver);
            double price = 0;

            Product product = new Product(name, description, price, variant, URL);
            if (isItemSoldOut(webDriver)) {
                logger.debug("Item sold out.");
                return product;
            }

            if (variant.contains("-oneVariant")) {
                logger.debug("One variant item.");
                product.setPrice(getPrice(driverWait));
                return product;
            }

            clickSizeButton(driverWait);

            List<WebElement> availableSizes = getAvailableSizes(driverWait);

            boolean sizeAvailable = false;
            for (WebElement e : availableSizes) {
                if (e.getText().equals(variant)) {
                    e.click();
                    sizeAvailable = true;
                    break;
                }
            }

            if (!sizeAvailable) {
                logger.debug("Size not available.");
                return product;
            }

            product.setPrice(getPrice(driverWait));
            logger.debug("Valid product.");
            return product;

        } catch (WebDriverException ex) {
            throw new WebDriverException(ex.getMessage());
        } finally {
            assert webDriver != null;
            webDriver.quit();
        }
    }

    public List<String> getSizes(String URL) {
        WebDriver webDriver = null;
        List<String> sizes;
        try {
            webDriver = new ChromeDriver(options);
            WebDriverWait driverWait = new WebDriverWait(webDriver, Duration.ofSeconds(3));

            webDriver.get(URL);

            if (!isLinkValidate(webDriver))
                throw new InvalidLinkException("It seems your link is incorrect, please check it and try again.", "User %s specified wrong link: %s".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));


            clickCookiesButton(driverWait);

            if (isItemOneVariant(webDriver)) {
                return List.of("-oneVariant " + getItemOneVariant(webDriver));
            }

            clickSizeButton(driverWait);

            sizes = getAllSizes(driverWait).stream()
                    .map(WebElement::getText)
                    .toList();
        } catch (WebDriverException ex) {
            throw new WebDriverException(ex.getMessage());
        } finally {
            assert webDriver != null;
            webDriver.quit();
        }
        return sizes;
    }


    private String getName(WebDriver webDriver) {
        WebElement name = getElementFromDriver(webDriver, By.cssSelector(Tag.NAME.get()));
        return name == null ? "" : name.getText();
    }

    private String getDescription(WebDriver webDriver) {
        WebElement description = getElementFromDriver(webDriver, By.cssSelector(Tag.DESCRIPTION.get()));
        return description == null ? "" : description.getText();
    }

    private double getPrice(WebDriverWait driverWait) {
        WebElement priceElement = getElementFromDriver(driverWait, By.cssSelector(Tag.PRICE.get()));
        String price = priceElement.getText();

        if (price.startsWith("od"))
            price = price.substring(3);

        price = price.substring(0, price.length() - 3);

        if (price.contains(" "))
            price = price.replaceAll(" ", "");

        return Double.parseDouble(price.replaceFirst(",", "."));
    }

    private boolean isVariantAlreadyChosen(WebDriver webDriver, String variant) {
        WebElement alreadyChosen = getElementFromDriver(webDriver, By.cssSelector(Tag.ALREADY_CHOSEN.get()));
        return alreadyChosen != null && alreadyChosen.getText().equals(variant);
    }

    private boolean isItemSoldOut(WebDriver webDriver) {
        WebElement soldOut = getElementFromDriver(webDriver, By.cssSelector(Tag.SOLD_OUT.get()));
        return soldOut != null && soldOut.getText().equals("Artykuł wyprzedany");
    }

    private List<WebElement> getAvailableSizes(WebDriverWait driverWait) {
        WebElement element = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(Tag.ALL_SIZES_ELEMENT.get())));
        return element.findElements(By.cssSelector(Tag.ALL_AVAILABLE_SIZES.get()));
    }

    private boolean isItemOneVariant(WebDriver webDriver) {
        return getElementFromDriver(webDriver, By.cssSelector(Tag.ONE_SIZE.get())) != null;
    }

    private String getItemOneVariant(WebDriver webDriver) {
        return getElementFromDriver(webDriver, By.cssSelector(Tag.ONE_SIZE.get())).getText();
    }

    private List<WebElement> getAllSizes(WebDriverWait driverWait) {
        WebElement element = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(Tag.ALL_SIZES_ELEMENT.get())));
        return element.findElements(By.cssSelector(Tag.ALL_SIZES.get()));
    }


    private boolean isLinkValidate(WebDriver webDriver) {
        WebElement linkValidate = getElementFromDriver(webDriver, By.cssSelector(Tag.LINK_VALIDATION.get()));
        return linkValidate != null;
    }

    private void clickCookiesButton(WebDriverWait driverWait) {
        try {
            driverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(Tag.COOKIES_BUTTON.get()))).click();
        } catch (TimeoutException | StaleElementReferenceException ignored) {
            if (getElementFromDriver(driverWait, By.cssSelector(Tag.COOKIES_BUTTON.get())) != null)
                clickCookiesButton(driverWait);
        }
    }

    private void clickSizeButton(WebDriverWait driverWait) {
        WebElement sizeButton = driverWait.until(ExpectedConditions.elementToBeClickable(By.xpath(Tag.SIZE_BUTTON.get())));
        sizeButton.click();
    }

    private WebElement getElementFromDriver(WebDriver webDriver, By by) {
        WebElement webElement = null;
        try {
            webElement = webDriver.findElement(by);
        } catch (NoSuchElementException ignored) {
        }
        return webElement;
    }

    private WebElement getElementFromDriver(WebDriverWait driverWait, By by) {
        WebElement webElement = null;
        try {
            webElement = driverWait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (NoSuchElementException ignored) {
        }
        return webElement;
    }
}





















