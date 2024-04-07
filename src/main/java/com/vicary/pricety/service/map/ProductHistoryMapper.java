package com.vicary.pricety.service.map;

import com.vicary.pricety.entity.ProductHistoryEntity;
import com.vicary.pricety.service.dto.ProductHistoryDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
        List<ProductHistoryDTO> DTOs = new ArrayList<>();
        for (ProductHistoryEntity e : phs)
            DTOs.add(map(e));
        return DTOs;
    }
}
