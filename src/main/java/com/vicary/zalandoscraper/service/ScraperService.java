package com.vicary.zalandoscraper.service;

import com.vicary.zalandoscraper.exception.ScraperBotException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.scraper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScraperService {

    public List<String> scrapVariants(String url) {
        Scraper scraper = ScraperFactory.getScraperFromLink(url).orElseThrow(() -> new ScraperBotException("Invalid link."));
        return scraper.getAllVariants(url);
    }

    public Product scrapProduct(String url, String variant) {
        Scraper scraper = ScraperFactory.getScraperFromLink(url).orElseThrow(() -> new ScraperBotException("Invalid link."));
        return scraper.getProduct(url, variant);
    }
}