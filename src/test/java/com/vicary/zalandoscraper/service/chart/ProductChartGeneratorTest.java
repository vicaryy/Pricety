package com.vicary.zalandoscraper.service.chart;

import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.repository.ProductHistoryRepository;
import com.vicary.zalandoscraper.service.UpdateReceiverService;
import com.vicary.zalandoscraper.service.dto.ProductHistoryDTO;
import com.vicary.zalandoscraper.service.map.ProductHistoryMapper;
import com.vicary.zalandoscraper.service.repository_services.ProductHistoryService;
import com.vicary.zalandoscraper.service.repository_services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductChartGeneratorTest {

    private ProductChartGenerator generator = ProductChartGenerator.builder()
            .dimension(new ProductChartGenerator.Dimension(1600, 600))
            .fileDestination("/Users/vicary/desktop")
            .build();

    @Autowired
    private ProductHistoryService service;

    @Autowired
    private ProductHistoryRepository repository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductHistoryMapper mapper;

    @MockBean
    private UpdateReceiverService updateReceiverService;

    @Test
    void asPng() {
        ProductChartGenerator productChartGenerator = ProductChartGenerator.builder()
                .dimension(new ProductChartGenerator.Dimension(1600, 600))
                .fileDestination("/Users/vicary/desktop")
                .build();
        Product p = productService.getProduct(226L);
        productChartGenerator.asPng(p, service.getProductHistory(226));
    }

//    @Test
//    void setAxis_expectEquals_SoldOutFromStart() {
//        //given
//        ProductChartGenerator productChartGenerator = ProductChartGenerator.builder().build();
//        List<Double> expectedXSoldOut = List.of(0D, 1D, 2D);
//        List<Double> expectedYSoldOut = List.of(200D, 200D, 200D);
//        List<Double> expectedX = List.of(0D, 1D, 2D, 3D, 4D, 5D, 6D, 7D, 8D);
//        List<Double> expectedY = List.of(200D, 200D, 200D, 200D, 300D, 900D, 500D, 500D, 300D);
//        List<ProductHistoryDTO> givenDTO = List.of(
//                getDefaultDTO(0),
//                getDefaultDTO(0),
//                getDefaultDTO(0),
//                getDefaultDTO(200),
//                getDefaultDTO(300),
//                getDefaultDTO(900),
//                getDefaultDTO(500),
//                getDefaultDTO(500),
//                getDefaultDTO(300)
//        );
//
//        //when
//        productChartGenerator.setAxis(givenDTO);
//        //then
//        assertEquals(expectedXSoldOut, productChartGenerator.getXSoldOut());
//        assertEquals(expectedYSoldOut, productChartGenerator.getYSoldOut());
//        assertEquals(expectedX, productChartGenerator.getX());
//        assertEquals(expectedY, productChartGenerator.getY());
//    }
//
//    @Test
//    void setAxis_expectEquals_SoldOutFromStartButOnlyOne() {
//        //given
//        ProductChartGenerator productChartGenerator = ProductChartGenerator.builder().build();
//        List<Double> expectedXSoldOut = List.of(0D);
//        List<Double> expectedYSoldOut = List.of(1000D);
//        List<Double> expectedX = List.of(0D, 1D, 2D, 3D, 4D, 5D, 6D, 7D, 8D);
//        List<Double> expectedY = List.of(1000D, 1000D, 1000D, 200D, 300D, 900D, 500D, 500D, 300D);
//        List<ProductHistoryDTO> givenDTO = List.of(
//                getDefaultDTO(0),
//                getDefaultDTO(1000),
//                getDefaultDTO(1000),
//                getDefaultDTO(200),
//                getDefaultDTO(300),
//                getDefaultDTO(900),
//                getDefaultDTO(500),
//                getDefaultDTO(500),
//                getDefaultDTO(300)
//        );
//
//        //when
//        productChartGenerator.setAxis(givenDTO);
//        //then
//        assertEquals(expectedXSoldOut, productChartGenerator.getXSoldOut());
//        assertEquals(expectedYSoldOut, productChartGenerator.getYSoldOut());
//        assertEquals(expectedX, productChartGenerator.getX());
//        assertEquals(expectedY, productChartGenerator.getY());
//    }
//
//    @Test
//    void setAxis_expectEquals_SoldOutInMiddle() {
//        //given
//        ProductChartGenerator productChartGenerator = ProductChartGenerator.builder().build();
//        List<Double> expectedXSoldOut = List.of(4D, 5D, 6D);
//        List<Double> expectedYSoldOut = List.of(1000D, 1000D, 1000D);
//        List<Double> expectedX = List.of(0D, 1D, 2D, 3D, 4D, 5D, 6D, 7D, 8D);
//        List<Double> expectedY = List.of(500D, 600D, 700D, 1000D, 1000D, 1000D, 1000D, 500D, 300D);
//
//
//        List<ProductHistoryDTO> givenDTO = List.of(
//                getDefaultDTO(500),
//                getDefaultDTO(600),
//                getDefaultDTO(700),
//                getDefaultDTO(1000),
//                getDefaultDTO(0),
//                getDefaultDTO(0),
//                getDefaultDTO(0),
//                getDefaultDTO(500),
//                getDefaultDTO(300)
//        );
//        //when
//        productChartGenerator.setAxis(givenDTO);
//        //then
//        assertEquals(expectedXSoldOut, productChartGenerator.getXSoldOut());
//        assertEquals(expectedYSoldOut, productChartGenerator.getYSoldOut());
//        assertEquals(expectedX, productChartGenerator.getX());
//        assertEquals(expectedY, productChartGenerator.getY());
//    }
//
//    @Test
//    void setAxis_expectEquals_SoldOutAtEnd() {
//        //given
//        ProductChartGenerator productChartGenerator = ProductChartGenerator.builder().build();
//        List<Double> expectedXSoldOut = List.of(8D);
//        List<Double> expectedYSoldOut = List.of(500D);
//        List<Double> expectedX = List.of(0D, 1D, 2D, 3D, 4D, 5D, 6D, 7D, 8D);
//        List<Double> expectedY = List.of(500D, 600D, 700D, 1000D, 200D, 700D, 700D, 500D, 500D);
//
//
//        List<ProductHistoryDTO> givenDTO = List.of(
//                getDefaultDTO(500),
//                getDefaultDTO(600),
//                getDefaultDTO(700),
//                getDefaultDTO(1000),
//                getDefaultDTO(200),
//                getDefaultDTO(700),
//                getDefaultDTO(700),
//                getDefaultDTO(500),
//                getDefaultDTO(0)
//        );
//        //when
//        productChartGenerator.setAxis(givenDTO);
//        //then
//        assertEquals(expectedXSoldOut, productChartGenerator.getXSoldOut());
//        assertEquals(expectedYSoldOut, productChartGenerator.getYSoldOut());
//        assertEquals(expectedX, productChartGenerator.getX());
//        assertEquals(expectedY, productChartGenerator.getY());
//    }
//
//
//    private ProductHistoryDTO getDefaultDTO(double price) {
//        return ProductHistoryDTO.builder().price(price).updateTime(LocalDateTime.now()).build();
//    }
}

//        System.out.println("xSoldOut:");
//        xSoldOut.forEach(System.out::println);
//        System.out.println("ySoldOut:");
//        ySoldOut.forEach(System.out::println);
//        System.out.println("x:");
//        x.forEach(System.out::println);
//        System.out.println("y:");
//        y.forEach(System.out::println);










