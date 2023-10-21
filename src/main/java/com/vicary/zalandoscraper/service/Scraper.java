package com.vicary.zalandoscraper.service;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Scraper {

    private final ChromeOptions options;

    public List<String> getSizes(String URL) {
        WebDriver webDriver = null;
        List<String> sizes;
        try {
            webDriver = new ChromeDriver(options);
            WebDriverWait driverWait = new WebDriverWait(webDriver, Duration.ofSeconds(3));


            webDriver.get(URL);

            isLinkValidate(webDriver);

            clickCookiesButton(driverWait);

            if (isItemOneVariant(webDriver)) {
                return List.of(getItemOneVariant(webDriver) + "-oneVariant");
            }

            clickSizeButton(driverWait);

            sizes = getEuSizes(driverWait).stream()
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

    private String getPrice(WebDriverWait driverWait) {
        WebElement price = getElementFromDriver(driverWait, By.cssSelector(Tag.PRICE.get()));
        return price == null ? "" : price.getText();
    }

    private boolean isItemOneVariant(WebDriver webDriver) {
        return getElementFromDriver(webDriver, By.cssSelector(Tag.ONE_SIZE.get())) != null;
    }

    private String getItemOneVariant(WebDriver webDriver) {
        return getElementFromDriver(webDriver, By.cssSelector(Tag.ONE_SIZE.get())).getText();
    }

    private List<WebElement> getEuSizes(WebDriverWait driverWait) {
        WebElement element = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(Tag.ALL_SIZES_ELEMENTS.get())));
        return element.findElements(By.cssSelector(Tag.ALL_SIZES.get()));
    }

    private void isLinkValidate(WebDriver webDriver) {
        WebElement linkValidate = getElementFromDriver(webDriver, By.cssSelector(Tag.LINK_VALIDATION.get()));
        if (linkValidate == null) {
            throw new InvalidLinkException("It seems your link is incorrect, please check it and try again.", "User %s specified wrong link: %s".formatted(ActiveUser.get().getUserId(), ActiveUser.get().getText()));
        }
    }

    private void clickCookiesButton(WebDriverWait driverWait) {
        try {
            driverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(Tag.COOKIES_BUTTON.get()))).click();
        } catch (TimeoutException ignored) {
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





















