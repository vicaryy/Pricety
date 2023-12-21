package com.vicary.zalandoscraper.service.map;

import com.vicary.zalandoscraper.entity.ProductHistoryEntity;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.dto.ProductHistoryDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductHistoryMapper {

    public ProductHistoryDTO map(ProductHistoryEntity ph) {
        return ProductHistoryDTO.builder()
                .price(ph.getPrice())
                .updateTime(ph.getLastUpdate())
                .build();
    }

    public List<ProductHistoryDTO> map(List<ProductHistoryEntity> phs) {
        return phs.stream().map(this::map).toList();
    }
}
