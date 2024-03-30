package com.vicary.zalandoscraper.service.repository_services;

import com.vicary.zalandoscraper.entity.ProductHistoryEntity;
import com.vicary.zalandoscraper.repository.ProductHistoryRepository;
import com.vicary.zalandoscraper.service.UpdateReceiverService;
import com.vicary.zalandoscraper.service.dto.ProductHistoryDTO;
import com.vicary.zalandoscraper.service.map.ProductHistoryMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductHistoryServiceTest {

    @Autowired
    private ProductHistoryService service;

    @Autowired
    private ProductHistoryMapper mapper;

    @MockBean
    private ProductHistoryRepository repository;

    @MockBean
    private UpdateReceiverService updateReceiverService;


    @Test
    void getProductHistory_expectEquals_WithMapping() {
        //given
        long givenProductId = 100;
        List<ProductHistoryEntity> givenList = new ArrayList<>();
        givenList.add(getEntity(5, 500, 5));
        givenList.add(getEntity(4, 400, 4));
        givenList.add(getEntity(3, 300, 3));
        givenList.add(getEntity(2, 200, 2));
        givenList.add(getEntity(1, 100, 1));

        List<ProductHistoryDTO> expectedList = new ArrayList<>();
        expectedList.add(getDTO(500, 5));
        expectedList.add(getDTO(400, 4));
        expectedList.add(getDTO(300, 3));
        expectedList.add(getDTO(200, 2));
        expectedList.add(getDTO(100, 1));

        //when
        when(repository.findAllByProductId(givenProductId, Sort.by(Sort.Direction.DESC, "id"))).thenReturn(givenList);
        List<ProductHistoryDTO> actualList = service.getProductHistorySortedByIdReverse(givenProductId);

        //then
        assertEquals(expectedList, actualList);
    }

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
        List<ProductHistoryEntity> actualList = service.getReducedListToOneDay(givenList);

        //then
        assertEquals(expectedList, actualList);
    }

    @Test
    void getLastPositivePrice_expectEquals_NormalList() {
        //given
        double expectedPrice = 350;
        long givenId = 1;
        List<ProductHistoryEntity> givenList = List.of(
                getEntity(10, 0, 1),
                getEntity(9, 0, 1),
                getEntity(8, 0, 1),
                getEntity(7, 0, 1),
                getEntity(6, 0, 1),
                getEntity(5, 0, 1),
                getEntity(4, 0, 1),
                getEntity(3, 0, 1),
                getEntity(2, 350, 1),
                getEntity(1, 100, 1)
                );

        //when
        when(repository.findAllByProductId(givenId, Sort.by(Sort.Direction.DESC, "id"))).thenReturn(givenList);
        double actualPrice = service.getLastPositivePrice(givenId);

        //then
        assertEquals(expectedPrice, actualPrice);
    }

    @Test
    void getLastPositivePrice_expectEquals_EveryPriceIsZero() {
        //given
        double expectedPrice = 0;
        long givenId = 1;
        List<ProductHistoryEntity> givenList = List.of(
                getEntity(0, 1),
                getEntity(0, 1),
                getEntity(0, 1),
                getEntity(0, 1),
                getEntity(0, 1),
                getEntity(0, 1),
                getEntity(0, 1),
                getEntity(0, 1),
                getEntity(0, 1),
                getEntity(0, 1)
        );

        //when
        when(repository.findAllByProductId(givenId, Sort.by(Sort.Direction.DESC, "id"))).thenReturn(givenList);
        double actualPrice = service.getLastPositivePrice(givenId);

        //then
        assertEquals(expectedPrice, actualPrice);
    }

    @Test
    void getLastPositivePrice_expectEquals_NoZeroInList() {
        //given
        double expectedPrice = 350;
        long givenId = 1;
        List<ProductHistoryEntity> givenList = List.of(
                getEntity(350, 1),
                getEntity(350, 1),
                getEntity(350, 1),
                getEntity(350, 1),
                getEntity(350, 1),
                getEntity(350, 1),
                getEntity(350, 1),
                getEntity(720, 1),
                getEntity(350, 1),
                getEntity(120, 1)
        );

        //when
        when(repository.findAllByProductId(givenId, Sort.by(Sort.Direction.DESC, "id"))).thenReturn(givenList);
        double actualPrice = service.getLastPositivePrice(givenId);

        //then
        assertEquals(expectedPrice, actualPrice);
    }

    private ProductHistoryEntity getEntity(long id, double price, int day) {
        return ProductHistoryEntity.builder()
                .id(id)
                .productId(1L)
                .price(price)
                .lastUpdate(LocalDateTime.of(2023, 10, day, 10, 10, 10, 10))
                .build();
    }

    private ProductHistoryEntity getEntity(double price, int day) {
        return ProductHistoryEntity.builder()
                .productId(1L)
                .price(price)
                .lastUpdate(LocalDateTime.of(2023, 10, day, 10, 10, 10, 10))
                .build();
    }

    private ProductHistoryDTO getDTO(double price, int day) {
        return ProductHistoryDTO.builder()
                .price(price)
                .updateTime(LocalDateTime.of(2023, 10, day, 10, 10, 10, 10))
                .build();
    }
}
