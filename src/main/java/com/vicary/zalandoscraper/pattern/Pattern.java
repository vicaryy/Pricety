package com.vicary.zalandoscraper.pattern;

import org.springframework.stereotype.Component;

@Component
public class Pattern {

    public static boolean isZalandoURL(String text) {
        return text.startsWith("https://www.zalando.pl/");
    }

    public static boolean isCommand(String text) {
        return text.startsWith("/");
    }
}
