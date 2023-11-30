package com.vicary.zalandoscraper.pattern;

import com.vicary.zalandoscraper.api_telegram.api_object.Update;
import com.vicary.zalandoscraper.scraper.HebeScraper;
import com.vicary.zalandoscraper.scraper.NikeScraper;
import com.vicary.zalandoscraper.scraper.Scraper;
import com.vicary.zalandoscraper.scraper.ZalandoScraper;
import org.springframework.stereotype.Component;

@Component
public class Pattern {

    private static final java.util.regex.Pattern emailPattern = java.util.regex.Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

    public static boolean isZalandoURL(String text) {
        return text.startsWith("https://www.zalando.pl/") || text.startsWith("https://zalando.pl/");
    }

    public static boolean isHebeURL(String text) {
        return text.startsWith("https://www.hebe.pl/") || text.startsWith("https://hebe.pl/");
    }

    public static boolean isNikeURL(String text) {
        return text.startsWith("https://www.nike.com/pl/") || text.startsWith("https://nike.com/pl/");
    }

    public static boolean isURL(String text) {
        return isZalandoURL(text) || isHebeURL(text) || isNikeURL(text);
    }

    public static boolean isZalandoURLWithPrefix(String text) {
        String[] arrayText = text.split(" ");
        return arrayText[arrayText.length - 1].startsWith("https://www.zalando.pl/");
    }

    public static String removeZalandoPrefix(String text) {
        String[] arrayText = text.split(" ");
        return arrayText[arrayText.length - 1];
    }

    public static boolean isCommand(String text) {
        return text.startsWith("/");
    }

    public static boolean isInlineMarkup(Update update) {
        return update.getCallbackQuery() != null;
    }

    public static boolean isEmail(String text) {
        if (text.equalsIgnoreCase("delete"))
            return true;

        return emailPattern
                .matcher(text)
                .matches();
    }

    public static boolean isEmailToken(String text) {
        return text.startsWith("v-");
    }

    public static boolean isAdminCommand(String text, boolean isAdmin) {
        return isAdmin && text.startsWith("/");
    }
}
