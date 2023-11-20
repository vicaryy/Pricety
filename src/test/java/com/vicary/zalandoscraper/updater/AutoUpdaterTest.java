package com.vicary.zalandoscraper.updater;

import com.vicary.zalandoscraper.service.dto.ProductDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AutoUpdaterTest {

    @Autowired
    private AutoUpdater autoUpdater;

    @Test
    void divideListIntoScrapers() {
        ProductDTO productDTO = ProductDTO.builder()
                .link("https://www.zalando.pl/zalando1")
                .build();
        ProductDTO productDTO1 = ProductDTO.builder()
                .link("https://www.zalando.pl/zalando2")
                .build();
        ProductDTO productDTO2 = ProductDTO.builder()
                .link("https://www.zalando.pl/zalando3")
                .build();
        ProductDTO productDTO3 = ProductDTO.builder()
                .link("https://www.zalando.pl/zalando4")
                .build();
        ProductDTO productDTO4 = ProductDTO.builder()
                .link("https://www.zalando.pl/zalando5")
                .build();
        ProductDTO productDTO5 = ProductDTO.builder()
                .link("https://www.zalando.pl/zalando6")
                .build();
        ProductDTO productDTO6 = ProductDTO.builder()
                .link("https://www.zalando.pl/zalando7")
                .build();
        ProductDTO productDTO7 = ProductDTO.builder()
                .link("https://www.zalando.pl/zalando8")
                .build();

        ProductDTO productDTO8 = ProductDTO.builder()
                .link("https://www.hebe.pl/hebe1")
                .build();
        ProductDTO productDTO9 = ProductDTO.builder()
                .link("https://www.hebe.pl/hebe2")
                .build();
        ProductDTO productDTO10 = ProductDTO.builder()
                .link("https://www.something.pl/something1")
                .build();
        ProductDTO productDTO11 = ProductDTO.builder()
                .link("https://www.allegro.pl/allegro1")
                .build();
        ProductDTO productDTO12 = ProductDTO.builder()
                .link("https://www.hebe.pl/hebe3")
                .build();
        ProductDTO productDTO13 = ProductDTO.builder()
                .link("https://www.hebe.pl/hebe4")
                .build();

        List<ProductDTO> productDTOList = new ArrayList<>();
        productDTOList.add(productDTO);
        productDTOList.add(productDTO1);
        productDTOList.add(productDTO2);
        productDTOList.add(productDTO3);
        productDTOList.add(productDTO4);
        productDTOList.add(productDTO5);
        productDTOList.add(productDTO6);
        productDTOList.add(productDTO7);
        productDTOList.add(productDTO8);
        productDTOList.add(productDTO9);
        productDTOList.add(productDTO10);
        productDTOList.add(productDTO11);
        productDTOList.add(productDTO12);
        productDTOList.add(productDTO13);


//        long time = System.currentTimeMillis();
//        List<List<ProductDTO>> ppp = autoUpdater.divideListIntoScrapers(productDTOList);
//        System.out.println("Time: " + (System.currentTimeMillis() - time));
//        System.out.println("List size: " + ppp.size());
//
//        for (List<ProductDTO> p : ppp) {
//            System.out.println(p);
//            System.out.println();
//            System.out.println();
//        }
    }
}