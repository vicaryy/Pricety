package com.vicary.pricety.service;

import com.vicary.pricety.exception.ScraperBotException;
import com.vicary.pricety.model.Product;
import com.vicary.pricety.scraper.*;
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