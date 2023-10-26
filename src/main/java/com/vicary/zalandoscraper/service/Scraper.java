package com.vicary.zalandoscraper.service;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Scraper {

    private final static Logger logger = LoggerFactory.getLogger(Scraper.class);

    private final ChromeOptions options;

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

            // TODO
            Product product = new Product(name, description, price, variant, URL, null);
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

            isLinkValidate(webDriver);

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


    protected List<ProductDTO> updateProducts(List<ProductDTO> productDTOS) {
        String zalandoURL = "https://www.zalando.pl/salvatore-ferragamo-gancini-gent-zegarek-blacksilver-3sf52m01p-q11.html";
        String givenSize = "42";

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new",
                "--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36",
                "--remote-allow-origins=*");

        List<String> windowHandles = new ArrayList<>();
        WebDriver webDriver = new ChromeDriver(options);
        WebDriverWait driverWait = new WebDriverWait(webDriver, Duration.ofSeconds(2L));

        for (String url : zalandoURLs) {
            webDriver.switchTo().newWindow(WindowType.WINDOW).get(url);
            windowHandles.add(webDriver.getWindowHandle());
        }

        int z = 1;
        for (String handle : windowHandles) {
            System.out.println("---------------------------------------");
            System.out.println("Item nr. " + z);
            z++;
            webDriver.switchTo().window(handle);

//            webDriver.navigate().to(zalandoURL);

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
                continue;
            }


            clickSizeButton(driverWait);

            List<WebElement> euSizes = getEuSizes(driverWait);

//            System.out.println("Available sizes: ");
//            euSizes.stream().forEach(e -> System.out.println(e.getText()));

            int sizeCount = -1;
            for (int i = 0; i < euSizes.size(); i++) {
                if (euSizes.get(i).getText().equals(givenSize)) {
                    euSizes.get(i).click();
                    sizeCount = i;
                }
            }

            if (sizeCount == -1) {
                System.out.println("Size is not available");
                continue;
            }

            String name = webDriver.findElement(By.cssSelector("span._ZDS_REF_SCOPE_._5FHGm_")).getText();
            String description = webDriver.findElement(By.cssSelector("span.EKabf7.R_QwOV")).getText();
            String price = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.sDq_FX._4sa1cA"))).getText();
            System.out.println("Size: " + givenSize);
            System.out.println(name);
            System.out.println(description);
            System.out.println(price);

        }

        webDriver.quit();
    }
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
        WebElement price = getElementFromDriver(driverWait, By.cssSelector(Tag.PRICE.get()));
        String[] priceArray = price.getText().split(" ");
        for (int i = 0; i < priceArray.length; i++) {
            if (priceArray[i].equals("zł")) {
                String priceInString = priceArray[i - 1];
                return Double.parseDouble(priceInString.replaceFirst(",", "."));
            }
        }
        return 0;
    }

    private boolean isItemSoldOut(WebDriver webDriver) {
        WebElement soldOut = getElementFromDriver(webDriver, By.cssSelector(Tag.SOLD_OUT.get()));
        if (soldOut != null && soldOut.getText().equals("Artykuł wyprzedany"))
            return true;
        return false;
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





















