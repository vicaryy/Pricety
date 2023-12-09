package com.vicary.zalandoscraper.pattern;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatternTest {

    @Test
    void isZalandoURL_expectTrue_ZalandoPL() {
        //given
        String givenLink = "https://www.zalando.pl/asd";

        //when
        //then
        assertTrue(Pattern.isZalandoURL(givenLink));
    }

    @Test
    void isZalandoURL_expectTrue_ZalandoCZ() {
        //given
        String givenLink = "https://www.zalando.cz/asd";

        //when
        //then
        assertTrue(Pattern.isZalandoURL(givenLink));
    }

    @Test
    void isZalandoURL_expectTrue_ZalandoCOUK() {
        //given
        String givenLink = "https://www.zalando.co.uk/asd";

        //when
        //then
        assertTrue(Pattern.isZalandoURL(givenLink));
    }

    @Test
    void isZalandoURL_expectFalse_WrongCountry() {
        //given
        String givenLink = "https://www.zalando.wrong/asd";

        //when
        //then
        assertFalse(Pattern.isZalandoURL(givenLink));
    }

    @Test
    void isZalandoURL_expectFalse_MainPage() {
        //given
        String givenLink = "https://www.zalando.pl/";

        //when
        //then
        assertFalse(Pattern.isZalandoURL(givenLink));
    }

    @Test
    void isZalandoURL_expectFalse_NoHttps() {
        //given
        String givenLink = "www.zalando.pl/asd";

        //when
        //then
        assertFalse(Pattern.isZalandoURL(givenLink));
    }

    @Test
    void isEmail_expectFalse_InvalidEmails() {
        getInvalidEmails().forEach(e -> assertFalse(Pattern.isEmail(e)));
    }

    @Test
    void isEmail_expectFalse_ValidEmails() {
        getValidEmails().forEach(e -> assertTrue(Pattern.isEmail(e)));
    }

    @Test
    void isURL_expectTrue_ValidURLs() {
        getValidURLs().forEach(u -> assertTrue(Pattern.isURL(u)));
    }

    @Test
    void isURL_expectFalse_InvalidURLs() {
        getInvalidURLs().forEach(u -> assertFalse(Pattern.isURL(u)));
    }


    @Test
    void isPrefixedURL_expectTrue_ValidPrefixedAtTheEnd() {
        //given
        String givenURL = "Sprawdź ten przedmiot na zalando: https://www.zalando.pl/asd";

        //when
        //then
        assertTrue(Pattern.isPrefixedURL(givenURL));
    }

    @Test
    void isPrefixedURL_expectFalse_ValidPrefixedInTheMiddle() {
        //given
        String givenURL = "Sprawdź ten https://www.zalando.pl/asd przedmiot na zalando.";

        //when
        //then
        assertFalse(Pattern.isPrefixedURL(givenURL));
    }

    @Test
    void isPrefixedURL_expectTrue_ValidPrefixedAtTheBeginning() {
        //given
        String givenURL = "https://www.zalando.pl/asd sprawdź ten przedmiot na zalando.";

        //when
        //then
        assertTrue(Pattern.isPrefixedURL(givenURL));
    }


    private List<String> getValidURLs() {
        return List.of(
                "https://www.asd.pl/asd",
                "https://www.asd.co.uk/asd",
                "https://www.asd.link/asd",
                "https://www.asd.com/asd",
                "https://www.asd-asd.com/asd",
                "https://www.asd_asd.com/asd",
                "https://www.asd.asd.com/asd",
                "4893759813HADFIJGB32929/:d.com/asd",
                "https://www.ASD.pl/asd",
                "https://www.asd.pl/ASD123-/",
                "https://www.asd.pl/",
                "https://asd.pl/asd",
                "https://asd.pl/",
                "www.asd.pl/asd",
                "www.asd.pl/",
                "asd.pl/asd",
                "asd.pl/",
                "s.pl/asd",
                "s.pl/"
        );
    }

    private List<String> getInvalidURLs() {
        return List.of(
                "https://www.asd.lengthMoreThanFour/asd",
                "https://www.asd.123/asd",
                "-.pl/asd",
                "asd.pl",
                "4893759813HADFIJGB32929/';;;;d.com/asd",
                "'d.com/asd",
                "https;;/asd.com/asd",
                "asd.plasd"
        );
    }

    private List<String> getInvalidEmails() {
        return List.of(
                "vicary@asd.",
                "@asd.com",
                "vicaryasd.pl",
                "dsfa/s///@asd.com",
                "invalid-email@.com",
                "missing-at-sign.com",
                "user@double..dot.com",
                "user@dot-ending.",
                "@start-with-at-sign.com",
                "user@-start-with-hyphen.com"
        );
    }

    private List<String> getValidEmails() {
        return List.of(
                "test@example.com",
                "john.doe123@test-domain.com",
                "user@company.co.uk"
        );
    }
}