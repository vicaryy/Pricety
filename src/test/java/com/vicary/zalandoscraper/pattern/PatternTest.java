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