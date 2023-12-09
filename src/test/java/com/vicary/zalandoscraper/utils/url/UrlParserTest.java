package com.vicary.zalandoscraper.utils.url;

import com.vicary.zalandoscraper.exception.UrlParserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UrlParserTest {

    private static UrlParser parser;
    private static UrlExpander expander;

    @BeforeEach
    void beforeAll() {
        expander = mock(UrlExpander.class);
        parser = new UrlParser(expander);
    }

    @Test
    void parse_expectEquals_HttpsAndWWW() {
        //given
        String givenURL = "https://www.asd.pl/example";
        String expectedURL = ("https://www.asd.pl/example");

        //when
        String actualURL = parser.parse(givenURL);

        //then
        assertEquals(expectedURL, actualURL);
        verify(expander, times(0)).expand(anyString());
    }

    @Test
    void parse_expectEquals_HttpAndWWW() {
        //given
        String givenURL = "http://www.asd.pl/example";
        String expectedURL = ("https://www.asd.pl/example");

        //when
        String actualURL = parser.parse(givenURL);

        //then
        assertEquals(expectedURL, actualURL);
        verify(expander, times(0)).expand(anyString());
    }

    @Test
    void parse_expectEquals_OnlyWWW() {
        //given
        String givenURL = "www.asd.pl/example";
        String expectedURL = ("https://www.asd.pl/example");

        //when
        String actualURL = parser.parse(givenURL);

        //then
        assertEquals(expectedURL, actualURL);
        verify(expander, times(0)).expand(anyString());
    }

    @Test
    void parse_expectEquals_OnlyHttps() {
        //given
        String givenURL = "https://asd.pl/example";
        String expectedURL = ("https://www.asd.pl/example");

        //when
        String actualURL = parser.parse(givenURL);

        //then
        assertEquals(expectedURL, actualURL);
        verify(expander, times(0)).expand(anyString());
    }

    @Test
    void parse_expectEquals_OnlyHttp() {
        //given
        String givenURL = "http://asd.pl/example";
        String expectedURL = ("https://www.asd.pl/example");

        //when
        String actualURL = parser.parse(givenURL);

        //then
        assertEquals(expectedURL, actualURL);
        verify(expander, times(0)).expand(anyString());
    }

    @Test
    void parse_expectEquals_Nothing() {
        //given
        String givenURL = "asd.pl/example";
        String expectedURL = "https://www.asd.pl/example";

        //when
        String actualURL = parser.parse(givenURL);

        //then
        assertEquals(expectedURL, actualURL);
        verify(expander, times(0)).expand(anyString());
    }

    @Test
    void parse_expectEquals_ShortenURL() {
        //given
        String givenURL = "https://nike.sng.link/asd";
        String expectedURL = "https://www.nike.com/pl/asd";
        Optional<String> givenOptionalURL = Optional.of("https://nike.com/pl/asd");

        //when
        when(expander.expand(givenURL)).thenReturn(givenOptionalURL);
        String actualURL = parser.parse(givenURL);

        //then
        assertEquals(expectedURL, actualURL);
        verify(expander, times(1)).expand(anyString());
    }

    @Test
    void parse_expectThrow_ShortenURLButItCannotBeExtended() {
        //given
        String givenURL = "https://nike.sng.link/asd";
        Optional<String> givenOptionalURL = Optional.empty();

        //when
        when(expander.expand(givenURL)).thenReturn(givenOptionalURL);

        //then
        assertThrows(UrlParserException.class, () -> parser.parse(givenURL));
        verify(expander, times(1)).expand(anyString());
    }

    @Test
    void parse_expectEquals_UncheckedURLs() {
        //given
        String givenURL = "notURL";
        String expectedURL = "https://www.notURL";

        //when
        String actualURL = parser.parse(givenURL);

        //then
        assertEquals(expectedURL, actualURL);
        verify(expander, times(0)).expand(anyString());
    }

    @Test
    void parse_expectEquals_UncheckedURLsEmpty() {
        //given
        String givenURL = "";
        String expectedURL = "https://www.";

        //when
        String actualURL = parser.parse(givenURL);

        //then
        assertEquals(expectedURL, actualURL);
        verify(expander, times(0)).expand(anyString());
    }

    @Test
    void parse_expectEquals_ExceptionalURL() {
        //given
        String givenURL = "https://en.zalando.de/asd";
        String expectedURL = "https://en.zalando.de/asd";

        //when
        String actualURL = parser.parse(givenURL);

        //then
        assertEquals(expectedURL, actualURL);
        verify(expander, times(0)).expand(anyString());
    }
}










