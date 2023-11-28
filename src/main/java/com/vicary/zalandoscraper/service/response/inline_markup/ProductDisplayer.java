package com.vicary.zalandoscraper.service.response.inline_markup;

import com.vicary.zalandoscraper.service.dto.ProductDTO;

import java.util.List;

public interface ProductDisplayer {
    void display();
    void setProductDTOList(List<ProductDTO> DTOs);
    void setChatId(String chatId);
}
