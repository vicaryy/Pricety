package com.vicary.zalandoscraper.configuration;

import com.vicary.zalandoscraper.scraper.ZalandoScraper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScraperConfiguration {

    @Bean
    public ZalandoScraper getScraper() {
        return new ZalandoScraper();
    }
}
