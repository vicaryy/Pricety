package com.vicary.zalandoscraper.configuration;

import com.vicary.zalandoscraper.scraper.Scraper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScraperConfiguration {

    @Bean
    public Scraper getScraper() {
        return new Scraper();
    }
}
