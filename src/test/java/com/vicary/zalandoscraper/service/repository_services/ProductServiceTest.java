package com.vicary.zalandoscraper.service.repository_services;

import com.vicary.zalandoscraper.entity.ProductEntity;
import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.repository.ProductRepository;
import com.vicary.zalandoscraper.service.UpdateReceiverService;
import com.vicary.zalandoscraper.service.map.ProductMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository repository;

    @MockBean
    private UserService userService;

    @MockBean
    private ProductMapper mapper;

    @MockBean
    private UpdateReceiverService updateReceiverService;


    @Test
    void updateProductPrices_updateOnlyPrice_PriceAlertAUTO() {
        //given
        Product givenProduct = Product.builder()
                .productId(123L)
                .price(200)
                .newPrice(100)
                .priceAlert("AUTO")
                .build();

        //when
        productService.updateProductPrices(List.of(givenProduct));

        //then
        verify(repository, times(1)).updatePrice(givenProduct.getProductId(), givenProduct.getNewPrice());
        verify(repository, times(0)).updatePriceAndPriceAlert(anyLong(), anyDouble(), anyString());
    }

    @Test
    void updateProductPrices_updateOnlyPrice_PriceAlertOFF() {
        //given
        Product givenProduct = Product.builder()
                .productId(123L)
                .price(200)
                .newPrice(100)
                .priceAlert("OFF")
                .build();

        //when
        productService.updateProductPrices(List.of(givenProduct));

        //then
        verify(repository, times(1)).updatePrice(givenProduct.getProductId(), givenProduct.getNewPrice());
        verify(repository, times(0)).updatePriceAndPriceAlert(anyLong(), anyDouble(), anyString());
    }

    @Test
    void updateProductPrices_updateOnlyPrice_NewPriceIsZeroAndPriceAlertIsSpecified() {
        //given
        Product givenProduct = Product.builder()
                .productId(123L)
                .price(200)
                .newPrice(0)
                .priceAlert("100.00")
                .build();

        //when
        productService.updateProductPrices(List.of(givenProduct));

        //then
        verify(repository, times(1)).updatePrice(givenProduct.getProductId(), givenProduct.getNewPrice());
        verify(repository, times(0)).updatePriceAndPriceAlert(anyLong(), anyDouble(), anyString());
    }

    @Test
    void updateProductPrices_updateOnlyPrice_PriceAlertIsLowerThanNewPrice() {
        //given
        Product givenProduct = Product.builder()
                .productId(123L)
                .price(200)
                .newPrice(150)
                .priceAlert("100.00")
                .build();

        //when
        productService.updateProductPrices(List.of(givenProduct));

        //then
        verify(repository, times(1)).updatePrice(givenProduct.getProductId(), givenProduct.getNewPrice());
        verify(repository, times(0)).updatePriceAndPriceAlert(anyLong(), anyDouble(), anyString());
    }

    @Test
    void updateProductPrices_updatePriceAndDisablePriceAlert_PriceAlertIsHigherThanNewPrice() {
        //given
        Product givenProduct = Product.builder()
                .productId(123L)
                .price(200)
                .newPrice(50)
                .priceAlert("100.00")
                .build();

        //when
        productService.updateProductPrices(List.of(givenProduct));

        //then
        verify(repository, times(1)).updatePriceAndPriceAlert(givenProduct.getProductId(), givenProduct.getNewPrice(), "OFF");
        verify(repository, times(0)).updatePrice(anyLong(), anyDouble());
    }

    @Test
    void getAllProductsDtoByUserId_expectEmptyList_NoProducts() {
        //given
        String givenUserId = "123";
        Optional<UserEntity> givenUser = Optional.ofNullable(UserEntity.builder().build());

        //when
        when(userService.findByUserId(givenUserId)).thenReturn(givenUser);
        when(repository.findAllByUser(givenUser.get(), Sort.by("id"))).thenReturn(Collections.emptyList());

        List<Product> actualList = productService.getAllProductsByUserId(givenUserId);

        //then
        assertEquals(Collections.emptyList(), actualList);
        verify(mapper, times(0)).map(anyList());
    }

    @Test
    void getAllProductsDtoByUserId_expectList_NormalListOfProducts() {
        //given
        List<ProductEntity> givenEntityList = getNormalEntityList();
        String givenUserId = "123";
        Optional<UserEntity> givenUser = Optional.ofNullable(UserEntity.builder().build());

        //when
        when(userService.findByUserId(givenUserId)).thenReturn(givenUser);
        when(repository.findAllByUser(givenUser.get(), Sort.by("id"))).thenReturn(givenEntityList);

        productService.getAllProductsByUserId(givenUserId);

        //then
        verify(mapper, times(1)).map(givenEntityList);
    }

    private List<ProductEntity> getNormalEntityList() {
        return List.of(
                ProductEntity.builder().build(),
                ProductEntity.builder().build(),
                ProductEntity.builder().build());
    }
}











