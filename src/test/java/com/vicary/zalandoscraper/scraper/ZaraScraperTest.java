package com.vicary.zalandoscraper.scraper;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.scraper.config.DefaultLaunchOptions;
import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    void testPhotoUrl() {
        String givenLink = "https://www.zara.com/pl/pl/buty-skorzane-z-marszczeniem-przy-zapietku-p12429320.html?v1=318993484&v2=2351464";
        scraper.getProduct(givenLink, "-oneVariant");
    }

    @Test
    void getAllVariants_expectThrow_InvalidLink() {
        assertThrows(InvalidLinkException.class, () -> scraper.getAllVariants(INVALID_LINK));
    }

    @Test
    void getAllVariants_multiVariantOnly() {
        //given
        List<String> expectedList = List.of(
                "One Color + 39",
                "One Color + 40",
                "One Color + 41",
                "One Color + 42",
                "One Color + 43",
                "One Color + 44",
                "One Color + 45");
        //when
        //then
        String multiVariant = "https://www.zara.com/pl/pl/buty-z-dwoiny-skorzanej-z-ozdobnym-przeszyciem-p12505220.html?v1=299167602&v2=2299501";
        assertEquals(expectedList, scraper.getAllVariants(multiVariant));
    }

    @Test
    void getAllVariants_oneColorAndOneVariant() {
        //given
        List<String> expectedList = List.of("-oneVariant One Variant");
        //when
        //then
        String oneColorOneVariant = "https://www.zara.com/pl/pl/red-vanilla-30-ml-p20120841.html?v1=311297848";
        assertEquals(expectedList, scraper.getAllVariants(oneColorOneVariant));
    }

    @Test
    void getAllVariants_multiColorOnly() {
        //given
        List<String> expectedList = List.of(
                "114 NOBLE + One Variant",
                "602 REIGN + One Variant",
                "618 COUNTESS + One Variant",
                "739 REGAL + One Variant");
        //when
        //then
        String multiColor = "https://www.zara.com/pl/pl/ultimatte-matte-liquid-lipstick-p24130328.html?v1=324597585&v2=2290613";
        assertEquals(expectedList, scraper.getAllVariants(multiColor));
    }

    @Test
    void getAllVariants_multiColorOnlyWithoutColorName() {
        //given
        List<String> expectedList = List.of(
                "No Color + One Variant",
                "No Color + One Variant",
                "No Color + One Variant",
                "No Color + One Variant",
                "No Color + One Variant",
                "No Color + One Variant");
        //when
        //then
        String colorWithoutNames = "https://www.zara.com/pl/pl/kredka-kajal-p24790224.html?v1=323790424&v2=1881272";
        assertEquals(expectedList, scraper.getAllVariants(colorWithoutNames));
    }

    @Test
    void getAllVariants_multiColorAndVariants() {
        //given
        List<String> expectedList = List.of(
                "Biały + XS",
                "Biały + S",
                "Biały + M",
                "Biały + L",
                "Biały + XL",
                "pudrowy róż + XS",
                "pudrowy róż + S",
                "pudrowy róż + M",
                "pudrowy róż + L",
                "pudrowy róż + XL",
                "Czarny + XS",
                "Czarny + S",
                "Czarny + M",
                "Czarny + L",
                "Czarny + XL",
                "gepard + XS",
                "gepard + S",
                "gepard + M",
                "gepard + L",
                "gepard + XL",
                "Czerwono-biały + XS",
                "Czerwono-biały + S",
                "Czerwono-biały + M",
                "Czerwono-biały + L",
                "Czerwono-biały + XL",
                "Szaro-antracytowy + XS",
                "Szaro-antracytowy + S",
                "Szaro-antracytowy + M",
                "Szaro-antracytowy + L",
                "Szaro-antracytowy + XL",
                "Brązowo-niebieski + XS",
                "Brązowo-niebieski + S",
                "Brązowo-niebieski + M",
                "Brązowo-niebieski + L",
                "Brązowo-niebieski + XL",
                "Limonkowy + XS",
                "Limonkowy + S",
                "Limonkowy + M",
                "Limonkowy + L",
                "Limonkowy + XL",
                "Różowo-kredowy + XS",
                "Różowo-kredowy + S",
                "Różowo-kredowy + M",
                "Różowo-kredowy + L",
                "Różowo-kredowy + XL"
        );
        //when
        //then
        String multiColorsAndVariants = "https://www.zara.com/pl/pl/elastyczna-koszulka-o-krotszym-kroju-p04424159.html?v1=324864509";
        assertEquals(expectedList, scraper.getAllVariants(multiColorsAndVariants));
    }

    @Test
    void getProduct_oneVariant() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/retractable-kabuki-brush-p24140514.html?v1=324598033&v2=1881272";
        String givenVariant = "-oneVariant One Variant";
        Product expectedProduct = Product.builder()
                .name("RETRACTABLE KABUKI BRUSH")
                .description("-")
                .serviceName("zara.com")
                .price(65.9)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();

        //when
        //then
        assertEquals(expectedProduct, scraper.getProduct(givenLink, givenVariant));
    }

    @Test
    void getProduct_MultiVariantOnly() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/kurtka-skorzana-z-b%C5%82yszczacym-wykonczeniem-p05479380.html?v1=300169052&v2=2297897";
        String givenVariant = "No Color + L";
        Product expectedProduct = Product.builder()
                .name("KURTKA SKÓRZANA Z BŁYSZCZĄCYM WYKOŃCZENIEM")
                .description("-")
                .serviceName("zara.com")
                .price(849.0)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();

        //when
        //then
        assertEquals(expectedProduct, scraper.getProduct(givenLink, givenVariant));
    }

    @Test
    void getProduct_MultiVariantOnlyButWeirdVariant() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/kurtka-skorzana-z-b%C5%82yszczacym-wykonczeniem-p05479380.html?v1=300169052&v2=2297897";
        String givenVariant = "One Color + K";
        Product expectedProduct = Product.builder()
                .name("KURTKA SKÓRZANA Z BŁYSZCZĄCYM WYKOŃCZENIEM")
                .description("-")
                .serviceName("zara.com")
                .price(0)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();

        //when
        //then
        assertEquals(expectedProduct, scraper.getProduct(givenLink, givenVariant));
    }

    @Test
    void getProduct_MultiVariantOnlyButNonAvailable() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/komplet-3-par-bokserek-bieliznianych-z-kolekcji-basic-p03661300.html?v1=267186188&v2=2297897";
        String givenVariant = "One Color + S";
        Product expectedProduct = Product.builder()
                .name("KOMPLET 3 PAR BOKSEREK BIELIŹNIANYCH Z KOLEKCJI BASIC")
                .description("-")
                .serviceName("zara.com")
                .price(0)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();

        //when
        //then
        assertEquals(expectedProduct, scraper.getProduct(givenLink, givenVariant));
    }

    @Test
    void getProduct_MultiColorOnly() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/lakier-do-paznokci-p24400463.html?v1=317106936&v2=2298139";
        String givenVariant = "802 THUNDER + One Variant";
        Product expectedProduct = Product.builder()
                .name("LAKIER DO PAZNOKCI")
                .description("-")
                .serviceName("zara.com")
                .price(29.9)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();

        //when
        //then
        assertEquals(expectedProduct, scraper.getProduct(givenLink, givenVariant));
    }

    @Test
    void getProduct_MultiColorOnlyButNoColor() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/lakier-do-paznokci-p24400463.html?v1=317106936&v2=2298139";
        String givenVariant = "No Color + One Variant";
        Product expectedProduct = Product.builder()
                .name("LAKIER DO PAZNOKCI")
                .description("-")
                .serviceName("zara.com")
                .price(29.9)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();

        //when
        //then
        assertEquals(expectedProduct, scraper.getProduct(givenLink, givenVariant));
    }

    @Test
    void getProduct_MultiColorOnlyButColorNotAvailable() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/roz-do-policzkow-o-pudrowej-konsystencji-p24130119.html?v1=323790670&v2=1881272";
        String givenVariant = "629 OUTRAGED + One Variant";
        Product expectedProduct = Product.builder()
                .name("RÓŻ DO POLICZKÓW O PUDROWEJ KONSYSTENCJI")
                .description("-")
                .serviceName("zara.com")
                .price(0)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();

        //when
        //then
        assertEquals(expectedProduct, scraper.getProduct(givenLink, givenVariant));
    }

    @Test
    void getProduct_MultiColorOnlyButWeirdColor() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/lakier-do-paznokci-p24400463.html?v1=317106936&v2=2298139";
        String givenVariant = "Kolorek + One Variant";

        //when
        //then
        assertThrows(PlaywrightException.class, () -> scraper.getProduct(givenLink, givenVariant));
    }

    @Test
    void getProduct_MultiColorAndMultiVariantValid() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/dwuwarstwowy-p%C5%82aszcz-tkaniny-z-domieszka-we%C5%82ny-p08491301.html?v1=303479880&v2=2299139";
        String givenVariant = "Szary + XL";
        Product expectedProduct = Product.builder()
                .name("DWUWARSTWOWY PŁASZCZ TKANINY Z DOMIESZKĄ WEŁNY")
                .description("-")
                .serviceName("zara.com")
                .price(229.0)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();

        //when
        //then
        assertEquals(expectedProduct, scraper.getProduct(givenLink, givenVariant));
    }

    @Test
    void getProduct_MultiColorAndMultiVariantButVariantNotAvailable() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/dwuwarstwowy-p%C5%82aszcz-tkaniny-z-domieszka-we%C5%82ny-p08491301.html?v1=303479880&v2=2299139";
        String givenVariant = "Szary + M";
        Product expectedProduct = Product.builder()
                .name("DWUWARSTWOWY PŁASZCZ TKANINY Z DOMIESZKĄ WEŁNY")
                .description("-")
                .serviceName("zara.com")
                .price(0)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();

        //when
        //then
        assertEquals(expectedProduct, scraper.getProduct(givenLink, givenVariant));
    }

    @Test
    void getProduct_MultiColorAndMultiVariantButWeirdVariant() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/dwuwarstwowy-p%C5%82aszcz-tkaniny-z-domieszka-we%C5%82ny-p08491301.html?v1=303479880&v2=2299139";
        String givenVariant = "Szary + Mka";
        Product expectedProduct = Product.builder()
                .name("DWUWARSTWOWY PŁASZCZ TKANINY Z DOMIESZKĄ WEŁNY")
                .description("-")
                .serviceName("zara.com")
                .price(0)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();

        //when
        //then
        assertEquals(expectedProduct, scraper.getProduct(givenLink, givenVariant));
    }


    //
    //
    //
    @Test
    void updateProduct_oneVariant() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/retractable-kabuki-brush-p24140514.html?v1=324598033&v2=1881272";
        String givenVariant = "-oneVariant One Variant";
        Product givenProduct = Product.builder()
                .name("RETRACTABLE KABUKI BRUSH")
                .description("-")
                .serviceName("zara.com")
                .price(100)
                .newPrice(100)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();
        double expectedPrice = 65.9;

        //when
        runPlaywrightAndUpdateProduct(givenLink, givenProduct);

        //then
        assertEquals(expectedPrice, givenProduct.getNewPrice());
    }

    @Test
    void updateProduct_MultiVariantOnly() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/kurtka-skorzana-z-b%C5%82yszczacym-wykonczeniem-p05479380.html?v1=300169052&v2=2297897";
        String givenVariant = "One Color + L";
        Product givenProduct = Product.builder()
                .name("KURTKA SKÓRZANA Z BŁYSZCZĄCYM WYKOŃCZENIEM")
                .description("-")
                .serviceName("zara.com")
                .price(100)
                .newPrice(100)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();
        double expectedPrice = 849.0;

        //when
        runPlaywrightAndUpdateProduct(givenLink, givenProduct);

        //then
        assertEquals(expectedPrice, givenProduct.getNewPrice());
    }

    @Test
    void updateProduct_MultiVariantOnlyButWeirdVariant() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/kurtka-skorzana-z-b%C5%82yszczacym-wykonczeniem-p05479380.html?v1=300169052&v2=2297897";
        String givenVariant = "One Color + K";
        Product givenProduct = Product.builder()
                .name("KURTKA SKÓRZANA Z BŁYSZCZĄCYM WYKOŃCZENIEM")
                .description("-")
                .serviceName("zara.com")
                .price(100)
                .newPrice(100)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();
        double expectedPrice = 0;

        //when
        runPlaywrightAndUpdateProduct(givenLink, givenProduct);

        //then
        assertEquals(expectedPrice, givenProduct.getNewPrice());
    }

    @Test
    void updateProduct_MultiVariantOnlyButNonAvailable() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/komplet-3-par-bokserek-bieliznianych-z-kolekcji-basic-p03661300.html?v1=267186188&v2=2297897";
        String givenVariant = "One Color + S";
        Product givenProduct = Product.builder()
                .name("KOMPLET 3 PAR BOKSEREK BIELIŹNIANYCH Z KOLEKCJI BASIC")
                .description("-")
                .serviceName("zara.com")
                .price(100)
                .newPrice(100)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();
        double expectedPrice = 0;

        //when
        runPlaywrightAndUpdateProduct(givenLink, givenProduct);

        //then
        assertEquals(expectedPrice, givenProduct.getNewPrice());
    }

    @Test
    void updateProduct_MultiColorOnly() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/lakier-do-paznokci-p24400463.html?v1=317106936&v2=2298139";
        String givenVariant = "802 THUNDER + One Variant";
        Product givenProduct = Product.builder()
                .name("LAKIER DO PAZNOKCI")
                .description("-")
                .serviceName("zara.com")
                .price(100)
                .newPrice(100)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();
        double expectedPrice = 29.9;

        //when
        runPlaywrightAndUpdateProduct(givenLink, givenProduct);

        //then
        assertEquals(expectedPrice, givenProduct.getNewPrice());
    }

    @Test
    void updateProduct_MultiColorOnlyButNoColor() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/lakier-do-paznokci-p24400463.html?v1=317106936&v2=2298139";
        String givenVariant = "No Color + One Variant";
        Product givenProduct = Product.builder()
                .name("LAKIER DO PAZNOKCI")
                .description("-")
                .serviceName("zara.com")
                .price(100)
                .newPrice(100)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();
        double expectedPrice = 29.9;

        //when
        runPlaywrightAndUpdateProduct(givenLink, givenProduct);

        //then
        assertEquals(expectedPrice, givenProduct.getNewPrice());
    }

    @Test
    void updateProduct_MultiColorOnlyButColorNotAvailable() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/roz-do-policzkow-o-pudrowej-konsystencji-p24130119.html?v1=323790670&v2=1881272";
        String givenVariant = "629 OUTRAGED + One Variant";
        Product givenProduct = Product.builder()
                .name("RÓŻ DO POLICZKÓW O PUDROWEJ KONSYSTENCJI")
                .description("-")
                .serviceName("zara.com")
                .price(100)
                .newPrice(100)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();
        double expectedPrice = 0;

        //when
        runPlaywrightAndUpdateProduct(givenLink, givenProduct);

        //then
        assertEquals(expectedPrice, givenProduct.getNewPrice());
    }

    @Test
    void updateProduct_MultiColorOnlyButWeirdColor() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/lakier-do-paznokci-p24400463.html?v1=317106936&v2=2298139";
        String givenVariant = "Kolorek + One Variant";

        Product givenProduct = Product.builder()
                .name("LAKIER DO PAZNOKCI")
                .description("-")
                .serviceName("zara.com")
                .price(100)
                .newPrice(100)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();
        double expectedPrice = 0;

        //when
        runPlaywrightAndUpdateProduct(givenLink, givenProduct);

        //then
        assertEquals(expectedPrice, givenProduct.getNewPrice());
    }

    @Test
    void updateProduct_MultiColorAndMultiVariantValid() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/dwuwarstwowy-p%C5%82aszcz-tkaniny-z-domieszka-we%C5%82ny-p08491301.html?v1=303479880&v2=2299139";
        String givenVariant = "Szary + XL";
        Product givenProduct = Product.builder()
                .name("DWUWARSTWOWY PŁASZCZ TKANINY Z DOMIESZKĄ WEŁNY")
                .description("-")
                .serviceName("zara.com")
                .price(100)
                .newPrice(100)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();
        double expectedPrice = 229.0;

        //when
        runPlaywrightAndUpdateProduct(givenLink, givenProduct);

        //then
        assertEquals(expectedPrice, givenProduct.getNewPrice());
    }

    @Test
    void updateProduct_MultiColorAndMultiVariantButVariantNotAvailable() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/dwuwarstwowy-p%C5%82aszcz-tkaniny-z-domieszka-we%C5%82ny-p08491301.html?v1=303479880&v2=2299139";
        String givenVariant = "Szary + M";
        Product givenProduct = Product.builder()
                .name("DWUWARSTWOWY PŁASZCZ TKANINY Z DOMIESZKĄ WEŁNY")
                .description("-")
                .serviceName("zara.com")
                .price(100)
                .newPrice(100)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();
        double expectedPrice = 0;

        //when
        runPlaywrightAndUpdateProduct(givenLink, givenProduct);

        //then
        assertEquals(expectedPrice, givenProduct.getNewPrice());
    }

    @Test
    void updateProduct_MultiColorAndMultiVariantButWeirdVariant() {
        //given
        String givenLink = "https://www.zara.com/pl/pl/dwuwarstwowy-p%C5%82aszcz-tkaniny-z-domieszka-we%C5%82ny-p08491301.html?v1=303479880&v2=2299139";
        String givenVariant = "Szary + Mka";
        Product givenProduct = Product.builder()
                .name("DWUWARSTWOWY PŁASZCZ TKANINY Z DOMIESZKĄ WEŁNY")
                .description("-")
                .serviceName("zara.com")
                .price(100)
                .newPrice(100)
                .link(givenLink)
                .currency("PLN")
                .variant(givenVariant)
                .build();
        double expectedPrice = 0;

        //when
        runPlaywrightAndUpdateProduct(givenLink, givenProduct);

        //then
        assertEquals(expectedPrice, givenProduct.getNewPrice());
    }

    @Test
    void test() {
        scraper.getProduct("https://www.zara.com/pl/pl/kamizelka-pikowana-p06985454.html?v1=280465776&v2=2299526", "Jasnoszary + L");
    }


    void runPlaywrightAndUpdateProduct(String link, Product givenProduct) {
        Map<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
        extraHeaders.put("Upgrade-Insecure-Requests", "1");
        extraHeaders.put("Sec-Fetch-User", "?1");
        extraHeaders.put("Sec-Fetch-Site", "same-origin");
        extraHeaders.put("Sec-Fetch-Mode", "navigate");
        extraHeaders.put("Sec-Fetch-Dest", "dust");
        extraHeaders.put("Sec-Ch-Ua-Platform", "\"macOS\"");
        extraHeaders.put("Sec-Ch-Ua-Mobile", "?0");
        extraHeaders.put("Sec-Ch-Ua", "\"Google Chrome\";v=\"119\", \"Chromium\";v=\"119\", \"Not?A_Brand\";v=\"24\"");
        extraHeaders.put("Cache-Control", "max-age=0");
        BrowserType.LaunchOptions launchOptions = new DefaultLaunchOptions();
        Page.NavigateOptions navigateOptions = new Page.NavigateOptions().setWaitUntil(WaitUntilState.COMMIT);

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(launchOptions);
            Page page = browser.newPage();
            page.setDefaultTimeout(10000);
            page.setExtraHTTPHeaders(extraHeaders);
            page.navigate(link, navigateOptions);

            scraper.updateProduct(page, givenProduct);
        }
    }

}











