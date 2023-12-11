package com.vicary.zalandoscraper.scraper;

import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class HouseScraperTest {

    private HouseScraper scraper = new HouseScraper();

    @BeforeAll
    static void beforeAll() {
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages", Locale.of("en")));
    }

    @Test
    void getAllVariants() {
        //given
        String oneVariant = "https://www.housebrand.com/pl/pl/skladany-portfel-z-imitacji-skory-czarny-0029y-99x";
        String multiVariant = "https://www.housebrand.com/pl/pl/koszulka-z-nadrukiem-wednesday-czarna-6525x-99x";

        //when
        //then
        System.out.println(scraper.getAllVariants(multiVariant));
    }
}