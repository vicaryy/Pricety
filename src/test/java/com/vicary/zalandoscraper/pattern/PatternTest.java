package com.vicary.zalandoscraper.pattern;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatternTest {

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



    private List<String> getValidURLs() {
        return List.of(
                "https://www.asd.pl/asd",
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