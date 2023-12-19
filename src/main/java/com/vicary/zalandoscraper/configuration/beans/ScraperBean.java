package com.vicary.zalandoscraper.configuration.beans;

import com.vicary.zalandoscraper.scraper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScraperBean {

    @Bean
    public ZalandoScraper getZalandoScraper() {
        return new ZalandoScraper();
    }

    @Bean
    public HebeScraper getHebeScraper() {
        return new HebeScraper();
    }

    @Bean
    public NikeScraper getNikeScraper() {
        return new NikeScraper();
    }

    @Bean
    public HouseScraper getHouseScraper() {
        return new HouseScraper();
    }

    @Bean
    public ZaraScraper getZaraScraper() {
        return new ZaraScraper();
    }
}
