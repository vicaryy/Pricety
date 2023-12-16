package com.vicary.zalandoscraper.scraper;

import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
class ZaraScraperTest {
    /**
     * These tests should only be run manually.
     * Internet connection required and user have to update the links before.
     */

    private final static String INVALID_LINK = "https://www.zara.com/pl/pl/mezczyzna-odziez-wierzchnia-watowane-l722.html?v1=2299501";
    private final static String ONE_VARIANT_LINK = "https://www.zara.com/pl/pl/szeroka-bransoletka-ze-zdobieniem-w-fale-p01856010.html?v1=327249355&v2=2290613";
    private final static String MULTI_VARIANTS_ENABLED_AND_DISABLED_LINK = "https://www.zara.com/pl/pl/bawe%C5%82niana-koszulka-z-marszczeniem-p05644857.html?v1=316187473";
    private final static String SOLD_OUT_LINK = "https://www.housebrand.com/pl/pl/czapka-beanie-z-grubszej-dzianiny-une-vie-magnifique-blekitna-2643q-05x?algolia_query_id=272aa0179168948ebbe3b0c8f17ce2ac";
    private final static String NOT_AVAILABLE_LINK = "not found";

    private final ZaraScraper scraper = new ZaraScraper();

    @BeforeAll
    static void beforeAll() {
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages"));
    }

    @Test
    void getAllVariants_expectThrow_InvalidLink() {
        assertThrows(InvalidLinkException.class, () -> scraper.getAllVariants(INVALID_LINK));
    }

    @Test
    void getAllVariants() {
        //given
        //when
        //then
        String multiVariant = "https://www.zara.com/pl/pl/buty-z-dwoiny-skorzanej-z-ozdobnym-przeszyciem-p12505220.html?v1=299167602&v2=2299501";
        String multiColor = "https://www.zara.com/pl/pl/ultimatte-matte-liquid-lipstick-p24130328.html?v1=324597585&v2=2290613";
        String colorWithoutNames = "https://www.zara.com/pl/pl/kredka-kajal-p24790224.html?v1=323790424&v2=1881272";
        System.out.println(scraper.getAllVariants(multiVariant));
    }
}