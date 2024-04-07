package com.vicary.pricety.scraper;

import com.vicary.pricety.model.Product;

import java.util.List;

public interface Scraper {
    void updateProducts(List<Product> products);

    Product getProduct(String link, String variant);

    List<String> getAllVariants(String link);

    /**
     *
     * Sometimes Scraper gets stuck and Updater force shutdown it.
     * setBugged have to set Scraper to non-headless.
     */
    void setBugged(boolean bugged);
}
