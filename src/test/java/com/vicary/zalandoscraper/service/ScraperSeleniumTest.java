package com.vicary.zalandoscraper.service;

import com.vicary.zalandoscraper.scraper.ZalandoScraper;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ScraperSeleniumTest {

    @Autowired
    private ZalandoScraper scraper;


    @Test
    void test() {
        //given

        ProductDTO givenProductDTO1 = ProductDTO.builder()
                .productId(1L)
                .userId("12345")
                .name("Buty Jordan sneakersy")
                .link("https://www.zalando.pl/jordan-air-jordan-1-sneakersy-niskie-blackuniversity-bluewhite-joc12o006-q25.html")
                .variant("44")
                .build();

        ProductDTO expectedProductDTO1 = ProductDTO.builder()
                .productId(1L)
                .userId("12345")
                .name("Buty Jordan sneakersy")
                .link("https://www.zalando.pl/jordan-air-jordan-1-sneakersy-niskie-blackuniversity-bluewhite-joc12o006-q25.html")
                .variant("44")
                .newPrice(599.99)
                .build();


        ProductDTO givenProductDTO2 = ProductDTO.builder()
                .productId(2L)
                .userId("123456")
                .name("Kurtka The north face")
                .link("https://www.zalando.pl/the-north-face-jacket-kurtka-zimowa-black-th322t04n-q11.html")
                .variant("L")
                .build();

        ProductDTO expectedProductDTO2 = ProductDTO.builder()
                .productId(2L)
                .userId("123456")
                .name("Kurtka The north face")
                .link("https://www.zalando.pl/the-north-face-jacket-kurtka-zimowa-black-th322t04n-q11.html")
                .variant("L")
                .newPrice(819)
                .build();

//
//        ProductDTO givenProductDTO3 = ProductDTO.builder()
//                .productId(3L)
//                .userId("12345678")
//                .name("Spodnie trekkingowe")
//                .link("https://www.zalando.pl/nike-sportswear-club-spodnie-treningowe-dark-grey-heathermatte-silverwhite-ni122e05h-c11.html")
//                .variant("L")
//                .build();
//
//        ProductDTO expectedProductDTO3 = ProductDTO.builder()
//                .productId(3L)
//                .userId("12345678")
//                .name("Spodnie trekkingowe")
//                .link("https://www.zalando.pl/nike-sportswear-club-spodnie-treningowe-dark-grey-heathermatte-silverwhite-ni122e05h-c11.html")
//                .variant("L")
//                .newPrice(249)
//                .build();


        // NIEDOSTEPNE
        ProductDTO givenProductDTO4 = ProductDTO.builder()
                .productId(4L)
                .userId("123345678")
                .name("spodnie trekingowe nike niedostepne")
                .link("https://www.zalando.pl/nike-sportswear-club-pant-revival-spodnie-treningowe-dark-driftwood-ni122e0cl-o11.html")
                .variant("XS")
                .build();

        ProductDTO expectedProductDTO4 = ProductDTO.builder()
                .productId(4L)
                .userId("123345678")
                .name("spodnie trekingowe nike niedostepne")
                .link("https://www.zalando.pl/nike-sportswear-club-pant-revival-spodnie-treningowe-dark-driftwood-ni122e0cl-o11.html")
                .variant("XS")
                .newPrice(0)
                .build();


        ProductDTO givenProductDTO5 = ProductDTO.builder()
                .productId(5L)
                .userId("123345678")
                .name("Zegarek")
                .link("https://www.zalando.pl/salvatore-ferragamo-gancini-gent-zegarek-blacksilver-3sf52m01p-q11.html")
                .variant("-oneVariant One Size")
                .build();

        ProductDTO expectedProductDTO5 = ProductDTO.builder()
                .productId(5L)
                .userId("123345678")
                .name("Zegarek")
                .link("https://www.zalando.pl/salvatore-ferragamo-gancini-gent-zegarek-blacksilver-3sf52m01p-q11.html")
                .variant("-oneVariant One Size")
                .newPrice(3509)
                .build();


        // wyprzedany
        ProductDTO givenProductDTO6 = ProductDTO.builder()
                .productId(6L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/vans-t-shirt-z-nadrukiem-black-va222o0p4-q11.html")
                .variant("M")
                .build();

        ProductDTO expectedProductDTO6 = ProductDTO.builder()
                .productId(6L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/vans-t-shirt-z-nadrukiem-black-va222o0p4-q11.html")
                .variant("M")
                .newPrice(0)
                .build();


        ProductDTO givenProductDTO7 = ProductDTO.builder()
                .productId(7L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/puma-sneakersy-wysokie-deep-olivedark-chocolateteam-gold-pu115n026-n11.html")
                .variant("36")
                .build();

        ProductDTO expectedProductDTO7 = ProductDTO.builder()
                .productId(7L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/puma-sneakersy-wysokie-deep-olivedark-chocolateteam-gold-pu115n026-n11.html")
                .variant("36")
                .newPrice(331)
                .build();



        ProductDTO givenProductDTO8 = ProductDTO.builder()
                .productId(8L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/tommy-hilfiger-logo-long-sleeve-tee-bluzka-z-dlugim-rekawem-to122o07a-k11.html")
                .variant("M")
                .build();

        ProductDTO expectedProductDTO8 = ProductDTO.builder()
                .productId(8L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/tommy-hilfiger-logo-long-sleeve-tee-bluzka-z-dlugim-rekawem-to122o07a-k11.html")
                .variant("M")
                .newPrice(202)
                .build();




        ProductDTO givenProductDTO9 = ProductDTO.builder()
                .productId(9L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/tommy-hilfiger-3-pack-panty-desert-skywhiteprimary-red-to182o070-k11.html")
                .variant("M")
                .build();

        ProductDTO expectedProductDTO9 = ProductDTO.builder()
                .productId(9L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/tommy-hilfiger-3-pack-panty-desert-skywhiteprimary-red-to182o070-k11.html")
                .variant("M")
                .newPrice(189)
                .build();



        ProductDTO givenProductDTO10 = ProductDTO.builder()
                .productId(10L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/jansport-plecak-graphite-grey-1js54o00k-c11.html")
                .variant("-oneVariant")
                .build();

        ProductDTO expectedProductDTO10 = ProductDTO.builder()
                .productId(10L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/jansport-plecak-graphite-grey-1js54o00k-c11.html")
                .variant("-oneVariant")
                .newPrice(219)
                .build();


        ProductDTO givenProductDTO11 = ProductDTO.builder()
                .productId(11L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/mcm-collection-others-unisex-inne-akcesoria-black-mc154t002-q11.html")
                .variant("-oneVariant onesize")
                .build();

        ProductDTO expectedProductDTO11 = ProductDTO.builder()
                .productId(11L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/mcm-collection-others-unisex-inne-akcesoria-black-mc154t002-q11.html")
                .variant("-oneVariant onesize")
                .newPrice(1919)
                .build();


        ProductDTO givenProductDTO12 = ProductDTO.builder()
                .productId(12L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/swarovski-dc-comics-catwoman-unisex-inne-akcesoria-gray-4sw54e003-c11.html")
                .variant("-oneVariant onesize")
                .build();

        ProductDTO expectedProductDTO12 = ProductDTO.builder()
                .productId(12L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/swarovski-dc-comics-catwoman-unisex-inne-akcesoria-gray-4sw54e003-c11.html")
                .variant("-oneVariant onesize")
                .newPrice(1599)
                .build();


        ProductDTO givenProductDTO13 = ProductDTO.builder()
                .productId(13L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/lancome-la-vie-est-belle-eau-de-parfum-perfumy-not-defined-l1v31i01c-s11.html")
                .variant("100 ml")
                .build();

        ProductDTO expectedProductDTO13 = ProductDTO.builder()
                .productId(13L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/lancome-la-vie-est-belle-eau-de-parfum-perfumy-not-defined-l1v31i01c-s11.html")
                .variant("100 ml")
                .newPrice(501)
                .build();


        ProductDTO givenProductDTO14 = ProductDTO.builder()
                .productId(14L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/mugler-fragrances-elixir-eau-de-parfum-perfumy-muu31i01d-s11.html")
                .variant("50 ml")
                .build();

        ProductDTO expectedProductDTO14 = ProductDTO.builder()
                .productId(14L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/mugler-fragrances-elixir-eau-de-parfum-perfumy-muu31i01d-s11.html")
                .variant("50 ml")
                .newPrice(383)
                .build();



        ProductDTO givenProductDTO15 = ProductDTO.builder()
                .productId(15L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/jordan-air-legacy-312-sneakersy-niskie-whiteblackwolf-grey-joc12o00b-a14.html")
                .variant("43")
                .build();

        ProductDTO expectedProductDTO15 = ProductDTO.builder()
                .productId(15L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/jordan-air-legacy-312-sneakersy-niskie-whiteblackwolf-grey-joc12o00b-a14.html")
                .variant("43")
                .newPrice(487)
                .build();


        ProductDTO givenProductDTO16 = ProductDTO.builder()
                .productId(16L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/nike-sportswear-dunk-remastered-sneakersy-niskie-blackwhite-ni112o0td-q11.html")
                .variant("39")
                .build();

        ProductDTO expectedProductDTO16 = ProductDTO.builder()
                .productId(16L)
                .userId("123345444678")
                .name("Tshirt VANS wyprzedany")
                .link("https://www.zalando.pl/nike-sportswear-dunk-remastered-sneakersy-niskie-blackwhite-ni112o0td-q11.html")
                .variant("39")
                .newPrice(0)
                .build();


        List<ProductDTO> givenListDTOs = List.of(
                givenProductDTO1,
                givenProductDTO2,
//                givenProductDTO3,
                givenProductDTO4,
                givenProductDTO5,
                givenProductDTO6,
                givenProductDTO7,
                givenProductDTO8,
                givenProductDTO9,
                givenProductDTO10,
                givenProductDTO11,
                givenProductDTO12,
                givenProductDTO13,
                givenProductDTO14,
                givenProductDTO15);

        List<ProductDTO> expectedListDTOs = List.of(
                expectedProductDTO1,
                expectedProductDTO2,
//                expectedProductDTO3,
                expectedProductDTO4,
                expectedProductDTO5,
                expectedProductDTO6,
                expectedProductDTO7,
                expectedProductDTO8,
                expectedProductDTO9,
                expectedProductDTO10,
                expectedProductDTO11,
                expectedProductDTO12,
                expectedProductDTO13,
                expectedProductDTO14,
                expectedProductDTO15);


        //when
//        List<ProductDTO> actualListDTOs = scraper.updateProducts(givenListDTOs);

//        assertEquals(expectedListDTOs, actualListDTOs);

    }
}
































