package com.vicary.zalandoscraper.service.response.inline_markup;

import com.vicary.zalandoscraper.model.Product;

import java.util.List;

public interface ProductDisplayer {
    void display();
    void setProductDTOList(List<Product> products);
    void setChatId(String chatId);
}
