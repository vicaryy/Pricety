package com.vicary.zalandoscraper.service;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Scraper {

    private final ChromeOptions options;

    public List<String> getSizes(String URL) {
        WebDriver webDriver = new ChromeDriver(options);
        WebDriverWait driverWait = new WebDriverWait(webDriver, Duration.ofSeconds(3));
        List<String> sizes = new ArrayList<>();

        try {
            webDriver.navigate().to(URL);

            checkLinkValidation(webDriver);

            clickCookiesButton(driverWait);

            WebElement oneSizeButton = getElementFromDriver(webDriver, By.cssSelector("span.sDq_FX._2kjxJ6.dgII7d.Yb63TQ"));
            if (oneSizeButton != null && oneSizeButton.getText().equals("One Size")) {
                String name = webDriver.findElement(By.cssSelector("span._ZDS_REF_SCOPE_._5FHGm_")).getText();
                String description = webDriver.findElement(By.cssSelector("span.EKabf7.R_QwOV")).getText();
                String price = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.sDq_FX._4sa1cA"))).getText();
                System.out.println("Size: One size");
                System.out.println(name);
                System.out.println(description);
                System.out.println(price);
                webDriver.quit();
                return;
            }


            clickSizeButton(driverWait);

            List<WebElement> euSizes = getEuSizes(driverWait);

            System.out.println("Available sizes: ");
            euSizes.stream().forEach(e -> System.out.println(e.getText()));

            return sizes;
        }
    }


    public static List<WebElement> getEuSizes(WebDriverWait driverWait) {
        WebElement element = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[class='F8If-J mZoZK2 KLaowZ hj1pfK _8n7CyI JCuRr_'")));
        return element.findElements(By.cssSelector("span[class='sDq_FX _2kjxJ6 dgII7d HlZ_Tf'"));
    }

    public static void checkLinkValidation(WebDriver webDriver) {
        if (webDriver.findElement(By.cssSelector("div.I7OI1O.C3wGFf")).getText().isEmpty()) {
            System.out.println("Check your link and try again.");
            throw new InvalidLinkException("Check your link and try again.", "[scraper] Don't found item in link: %s".formatted(ActiveUser.get().getText()));
        }
    }

    public static void clickCookiesButton(WebDriverWait driverWait) {
        try {
            driverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[class='uc-btn uc-btn-default btn-deny'"))).click();
        } catch (TimeoutException ignored) {
        }
    }

    public static void clickSizeButton(WebDriverWait driverWait) {
        WebElement sizeButton = driverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"picker-trigger\"]")));
        sizeButton.click();
    }

    public static WebElement getElementFromDriver(WebDriver webDriver, By by) {
        WebElement webElement = null;
        try {
            webElement = webDriver.findElement(by);
        } catch (NoSuchElementException ignored) {
        }
        return webElement;
    }
}





















