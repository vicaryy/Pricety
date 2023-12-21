package com.vicary.zalandoscraper.service.repository_services;

import com.vicary.zalandoscraper.entity.ProductHistoryEntity;
import com.vicary.zalandoscraper.repository.ProductHistoryRepository;
import com.vicary.zalandoscraper.service.UpdateReceiverService;
import com.vicary.zalandoscraper.service.map.ProductHistoryMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductHistoryServiceTest {

    @Autowired
    private ProductHistoryService service;

    @Autowired
    private ProductHistoryMapper mapper;

    @MockBean
    private ProductHistoryRepository repository;

    @MockBean
    private ProductService productService;

    @MockBean
    private UpdateReceiverService updateReceiverService;



    @Test
    void reduceList_expectEquals_NormalList() {
        //given
        List<ProductHistoryEntity> givenList = List.of(
                getEntity(100, 1),
                getEntity(100, 2),
                getEntity(0, 2),
                getEntity(100, 2),
                getEntity(200, 3),
                getEntity(100, 4),
                getEntity(500, 5),
                getEntity(400, 5),
                getEntity(500, 5),
                getEntity(500, 5),
                getEntity(100, 6),
                getEntity(250, 7),
                getEntity(0, 8),
                getEntity(0, 8),
                getEntity(0, 8),
                getEntity(300, 9),
                getEntity(300, 10),
                getEntity(300, 10),
                getEntity(100, 10),
                getEntity(100, 13),
                getEntity(100, 13),
                getEntity(800, 14)
        );
        List<ProductHistoryEntity> expectedList = List.of(
                getEntity(100, 1),
                getEntity(0, 2),
                getEntity(200, 3),
                getEntity(100, 4),
                getEntity(400, 5),
                getEntity(100, 6),
                getEntity(250, 7),
                getEntity(0, 8),
                getEntity(300, 9),
                getEntity(100, 10),
                getEntity(100, 13),
                getEntity(800, 14)
        );

        //when
        List<ProductHistoryEntity> actualList = service.getReducedList(givenList);

        //then
        assertEquals(expectedList, actualList);

    }

    private ProductHistoryEntity getEntity(double price, int day) {
        return ProductHistoryEntity.builder()
                .productId(1L)
                .price(price)
                .lastUpdate(LocalDateTime.of(2023, 10, day, 10, 10, 10, 10))
                .build();
    }
}
