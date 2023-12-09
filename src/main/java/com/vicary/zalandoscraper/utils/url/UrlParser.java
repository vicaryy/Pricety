package com.vicary.zalandoscraper.utils.url;

import com.vicary.zalandoscraper.exception.UrlParserException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class UrlParser {
    private final static Set<String> urlShortens = new HashSet<>(Arrays.asList(
            "nike.sng.link/"));
    private final static Set<String> zalandoExceptions = new HashSet<>(Arrays.asList(
            "https://en.zalando.de/",
            "https://fr.zalando.ch/",
            "https://it.zalando.ch/"));

    private final UrlExpander urlExpander;

    public UrlParser(UrlExpander urlExpander) {
        this.urlExpander = urlExpander;
    }

    public String parse(String URL) {
        if (isShortener(URL))
            URL = expandURL(URL);

        if (checkExceptions(URL))
            return URL;

        if (URL.startsWith("https://en.")
                || URL.startsWith("https://fr.")
                || URL.startsWith("https://it."))

            if (URL.startsWith("https://www."))
                return URL;

        if (URL.startsWith("http://www."))
            return URL.replaceFirst("http", "https");

        if (URL.startsWith("www."))
            return URL.replaceFirst("www", "https://www");

        if (URL.startsWith("https://"))
            return URL.replaceFirst("https://", "https://www.");

        if (URL.startsWith("http://"))
            return URL.replaceFirst("http://", "https://www.");

        return "https://www." + URL;
    }

    private boolean isShortener(String URL) {
        for (var u : urlShortens)
            if (URL.contains(u))
                return true;
        return false;
    }

    private String expandURL(String URL) {
        return urlExpander.expand(URL).orElseThrow(() -> new UrlParserException("Cannot expand URL: " + URL));
    }

    private boolean checkExceptions(String URL) {
        checkZalandoExceptions(URL);
        for (var u : zalandoExceptions)
            if (URL.startsWith(u))
                return true;
        return false;
    }

    public void setShortener(String shortener) {
        urlShortens.add(shortener);
    }

    public static Set<String> getAllShortens() {
        return urlShortens;
    }

    public static Set<String> getAllExceptions() {
        return zalandoExceptions;
    }

    public static void setException(String exception) {
        zalandoExceptions.add(exception);
    }
}
