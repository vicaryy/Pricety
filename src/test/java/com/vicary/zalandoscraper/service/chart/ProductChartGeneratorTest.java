package com.vicary.zalandoscraper.service.chart;

import com.vicary.zalandoscraper.exception.ChartGeneratorException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.model.User;
import com.vicary.zalandoscraper.service.dto.ProductHistoryDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductChartGeneratorTest {

    @Test
    void asPng_expectThrows_FileDestinationEmpty() {
        //given
        Product givenProduct = getDefaultProduct();
        List<ProductHistoryDTO> givenDTOs = List.of(
                getDefaultDTO(100, 1),
                getDefaultDTO(200, 2)
        );
                ProductChartGenerator productChartGenerator = ProductChartGenerator.builder()
                .dimension(new ProductChartGenerator.Dimension(1600, 600))
                .fileDestination("")
                .build();

        //when
        //then
        assertThrows(ChartGeneratorException.class ,() -> productChartGenerator.asPng(givenProduct, givenDTOs));
    }

    @Test
    void asPng_expectThrows_FileDestinationNull() {
        //given
        Product givenProduct = getDefaultProduct();
        List<ProductHistoryDTO> givenDTOs = List.of(
                getDefaultDTO(100, 1),
                getDefaultDTO(200, 2)
        );
        ProductChartGenerator productChartGenerator = ProductChartGenerator.builder()
                .dimension(new ProductChartGenerator.Dimension(1600, 600))
                .fileDestination(null)
                .build();

        //when
        //then
        assertThrows(ChartGeneratorException.class, () -> productChartGenerator.asPng(givenProduct, givenDTOs));
    }

    @Test
    void asPng_expectThrows_DTOsIsEmpty() {
        //given
        Product givenProduct = getDefaultProduct();
        List<ProductHistoryDTO> givenDTOs = Collections.emptyList();
        ProductChartGenerator productChartGenerator = ProductChartGenerator.builder()
                .dimension(new ProductChartGenerator.Dimension(1600, 600))
                .fileDestination("/destination")
                .build();

        //when
        //then
        assertThrows(ChartGeneratorException.class, () -> productChartGenerator.asPng(givenProduct, givenDTOs));
    }

    @Test
    void asPng_expectThrows_DTOsOnlyContainsPriceZero() {
        //given
        Product givenProduct = getDefaultProduct();
        List<ProductHistoryDTO> givenDTOs = List.of(
                getDefaultDTO(0, 1),
                getDefaultDTO(0, 2),
                getDefaultDTO(0, 3),
                getDefaultDTO(0, 4)
        );
        ProductChartGenerator productChartGenerator = ProductChartGenerator.builder()
                .dimension(new ProductChartGenerator.Dimension(1600, 600))
                .fileDestination("/destination")
                .build();

        //when
        //then
        assertThrows(ChartGeneratorException.class, () -> productChartGenerator.asPng(givenProduct, givenDTOs));
    }



    @Test
    void setAxis_expectEquals_SoldOutFromStart() {
        //given
        ProductChartGenerator productChartGenerator = ProductChartGenerator.builder().build();
        List<List<Double>> expectedXSoldOut = List.of(
                List.of(0D, 1D),
                List.of(1D, 0D),
                List.of(2D, 1D)
        );
        List<List<Double>> expectedYSoldOut = List.of(
                List.of(200D, 200D),
                List.of(200D, 200D),
                List.of(200D, 200D)
        );
        List<Double> expectedX = List.of(0D, 1D, 2D, 3D, 4D, 5D, 6D, 7D, 8D);
        List<Double> expectedY = List.of(200D, 200D, 200D, 200D, 300D, 900D, 500D, 500D, 300D);
        List<ProductHistoryDTO> givenDTO = List.of(
                getDefaultDTO(0),
                getDefaultDTO(0),
                getDefaultDTO(0),
                getDefaultDTO(200),
                getDefaultDTO(300),
                getDefaultDTO(900),
                getDefaultDTO(500),
                getDefaultDTO(500),
                getDefaultDTO(300)
        );

        //when
        productChartGenerator.setAxis(givenDTO);
        //then
        assertEquals(expectedXSoldOut, productChartGenerator.getXSoldOut());
        assertEquals(expectedYSoldOut, productChartGenerator.getYSoldOut());
        assertEquals(expectedX, productChartGenerator.getX());
        assertEquals(expectedY, productChartGenerator.getY());
    }

    @Test
    void setAxis_expectEquals_SoldOutFromStartButOnlyOne() {
        //given
        ProductChartGenerator productChartGenerator = ProductChartGenerator.builder().build();
        List<List<Double>> expectedXSoldOut = List.of(
                List.of(0D, 1D)
        );
        List<List<Double>> expectedYSoldOut = List.of(
                List.of(1000D, 1000D)
        );
        List<Double> expectedX = List.of(0D, 1D, 2D, 3D, 4D, 5D, 6D, 7D, 8D);
        List<Double> expectedY = List.of(1000D, 1000D, 1000D, 200D, 300D, 900D, 500D, 500D, 300D);
        List<ProductHistoryDTO> givenDTO = List.of(
                getDefaultDTO(0),
                getDefaultDTO(1000),
                getDefaultDTO(1000),
                getDefaultDTO(200),
                getDefaultDTO(300),
                getDefaultDTO(900),
                getDefaultDTO(500),
                getDefaultDTO(500),
                getDefaultDTO(300)
        );

        //when
        productChartGenerator.setAxis(givenDTO);
        //then
        assertEquals(expectedXSoldOut, productChartGenerator.getXSoldOut());
        assertEquals(expectedYSoldOut, productChartGenerator.getYSoldOut());
        assertEquals(expectedX, productChartGenerator.getX());
        assertEquals(expectedY, productChartGenerator.getY());
    }

    @Test
    void setAxis_expectEquals_SoldOutInMiddle() {
        //given
        ProductChartGenerator productChartGenerator = ProductChartGenerator.builder().build();
        List<List<Double>> expectedXSoldOut = List.of(
                List.of(4D, 3D),
                List.of(5D, 4D),
                List.of(6D, 5D)
        );
        List<List<Double>> expectedYSoldOut = List.of(
                List.of(1000D, 1000D),
                List.of(1000D, 1000D),
                List.of(1000D, 1000D)
        );
        List<Double> expectedX = List.of(0D, 1D, 2D, 3D, 4D, 5D, 6D, 7D, 8D);
        List<Double> expectedY = List.of(500D, 600D, 700D, 1000D, 1000D, 1000D, 1000D, 500D, 300D);


        List<ProductHistoryDTO> givenDTO = List.of(
                getDefaultDTO(500),
                getDefaultDTO(600),
                getDefaultDTO(700),
                getDefaultDTO(1000),
                getDefaultDTO(0),
                getDefaultDTO(0),
                getDefaultDTO(0),
                getDefaultDTO(500),
                getDefaultDTO(300)
        );
        //when
        productChartGenerator.setAxis(givenDTO);
        //then
        assertEquals(expectedXSoldOut, productChartGenerator.getXSoldOut());
        assertEquals(expectedYSoldOut, productChartGenerator.getYSoldOut());
        assertEquals(expectedX, productChartGenerator.getX());
        assertEquals(expectedY, productChartGenerator.getY());
    }

    @Test
    void setAxis_expectEquals_SoldOutAtEnd() {
        //given
        ProductChartGenerator productChartGenerator = ProductChartGenerator.builder().build();
        List<List<Double>> expectedXSoldOut = List.of(
                List.of(8D, 7D)
        );
        List<List<Double>> expectedYSoldOut = List.of(
                List.of(500D, 500D)
        );
        List<Double> expectedX = List.of(0D, 1D, 2D, 3D, 4D, 5D, 6D, 7D, 8D);
        List<Double> expectedY = List.of(500D, 600D, 700D, 1000D, 200D, 700D, 700D, 500D, 500D);


        List<ProductHistoryDTO> givenDTO = List.of(
                getDefaultDTO(500),
                getDefaultDTO(600),
                getDefaultDTO(700),
                getDefaultDTO(1000),
                getDefaultDTO(200),
                getDefaultDTO(700),
                getDefaultDTO(700),
                getDefaultDTO(500),
                getDefaultDTO(0)
        );
        //when
        productChartGenerator.setAxis(givenDTO);
        //then
        assertEquals(expectedXSoldOut, productChartGenerator.getXSoldOut());
        assertEquals(expectedYSoldOut, productChartGenerator.getYSoldOut());
        assertEquals(expectedX, productChartGenerator.getX());
        assertEquals(expectedY, productChartGenerator.getY());
    }


    private ProductHistoryDTO getDefaultDTO(double price) {
        return ProductHistoryDTO.builder().price(price).updateTime(LocalDateTime.now()).build();
    }

    private ProductHistoryDTO getDefaultDTO(double price, int day) {
        return ProductHistoryDTO.builder()
                .price(price)
                .updateTime(LocalDateTime.of(2010, 10, day, 10, 10, 10, 10))
                .build();
    }

    private Product getDefaultProduct() {
        return Product.builder()
                .productId(123L)
                .name("name")
                .description("desc")
                .price(10)
                .newPrice(10)
                .currency("zł")
                .variant("M")
                .link("https://www.zalando.pl/123")
                .serviceName("zalando.pl")
                .user(User.builder()
                        .userId("123")
                        .email("email@email.com")
                        .nick("nick")
                        .language("en")
                        .build())
                .build();
    }
}










