package com.vicary.zalandoscraper.pattern;

import com.vicary.zalandoscraper.api_telegram.api_object.Update;
import org.springframework.stereotype.Component;

@Component
public class Pattern {

    private static final java.util.regex.Pattern emailPattern = java.util.regex.Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

    public static boolean isZalandoURL(String text) {
        return text.startsWith("https://www.zalando.pl/") || text.startsWith("https://zalando.pl/");
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

    public static boolean isReplyMarkup(Update update) {
        return update.getCallbackQuery() != null;
    }

    public static String getDatePattern() {
        return "yyyy-MM-dd HH:mm:ss";
    }

    public static boolean isAwaitedMessage(boolean awaitedMessage) {
        return awaitedMessage;
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
}
