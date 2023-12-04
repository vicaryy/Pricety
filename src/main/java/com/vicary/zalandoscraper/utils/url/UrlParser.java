package com.vicary.zalandoscraper.utils.url;

import com.vicary.zalandoscraper.exception.UrlParserException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class UrlParser {

    private final Set<String> urlExceptions = new HashSet<>(Arrays.asList("nike.sng.link/"));
    private final UrlExpander urlExpander;

    public UrlParser(UrlExpander urlExpander) {
        this.urlExpander = urlExpander;
    }

    public String parse(String URL) {
        if (isExceptional(URL))
            URL = expandURL(URL);

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

    private boolean isExceptional(String URL) {
        for (var u : urlExceptions)
            if (URL.contains(u))
                return true;
        return false;
    }

    private String expandURL(String URL) {
        return urlExpander.expand(URL).orElseThrow(() -> new UrlParserException("Cannot expand URL: " + URL));
    }

    public void setUrlException(String exception) {
        urlExceptions.add(exception);
    }
}
