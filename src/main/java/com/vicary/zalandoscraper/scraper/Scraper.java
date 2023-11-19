package com.vicary.zalandoscraper.scraper;

import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.dto.ProductDTO;

import java.util.List;

public interface Scraper {
    void updateProducts(List<ProductDTO> DTOs);

    Product getProduct(String link, String variant);

    List<String> getAllVariants(String link);

    void setBugged(boolean bugged);
}
