package com.vicary.zalandoscraper.scraper;

import com.vicary.zalandoscraper.model.Product;

import java.util.List;

public interface Scraper {
    void updateProducts(List<Product> products);

    Product getProduct(String link, String variant);

    List<String> getAllVariants(String link);

    void setBugged(boolean bugged);
}
