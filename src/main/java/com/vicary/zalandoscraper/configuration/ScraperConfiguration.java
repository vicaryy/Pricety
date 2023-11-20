package com.vicary.zalandoscraper.configuration;

import com.vicary.zalandoscraper.scraper.HebeScraper;
import com.vicary.zalandoscraper.scraper.ZalandoScraper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScraperConfiguration {

    @Bean
    public ZalandoScraper getZalandoScraper() {
        return new ZalandoScraper();
    }

    @Bean
    public HebeScraper getHebeScraper() {
        return new HebeScraper();
    }
}
