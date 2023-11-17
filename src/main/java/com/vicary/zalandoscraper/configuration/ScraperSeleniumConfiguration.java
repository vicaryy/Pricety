package com.vicary.zalandoscraper.configuration;

import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Deprecated
public class ScraperSeleniumConfiguration {
    private final ChromeOptions chromeOptions = new ChromeOptions();

    @Bean
    public ChromeOptions getChromeOptions() {
        chromeOptions.addArguments("--headless=new",
                "--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36",
                "--remote-allow-origins=*");
        return chromeOptions;
    }
}
