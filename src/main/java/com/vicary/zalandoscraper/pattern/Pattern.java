package com.vicary.zalandoscraper.pattern;

import com.vicary.zalandoscraper.api_telegram.api_object.Update;
import org.springframework.stereotype.Component;

@Component
public class Pattern {

    private static final java.util.regex.Pattern emailPattern = java.util.regex.Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

    private static final java.util.regex.Pattern urlPattern = java.util.regex.Pattern.compile("^[a-zA-Z0-9\\./:_-]*[a-zA-Z]\\.([a-z]{0,4})/.*");
    private static final java.util.regex.Pattern zalandoPattern = java.util.regex.Pattern.compile("https://[^/]+\\.zalando\\.(pl|cz|no|se|ro|ch|co.uk|es|sk|nl|at|fi|ie|it|be|lt|si|de|fr)/.+");

    public static boolean isZalandoURL(String text) {
        return zalandoPattern.matcher(text).matches();
    }

    public static boolean isHebeURL(String text) {
        return text.startsWith("https://www.hebe.pl/");
    }

    public static boolean isNikeURL(String text) {
        return text.startsWith("https://www.nike.com/pl/");
    }

    public static boolean isHouseURL(String text) {
        return text.startsWith("https://www.housebrand.com/");
    }

    public static boolean isZaraURL(String text) {
        return text.startsWith("https://www.zara.com/");
    }

    public static boolean isURL(String text) {
        return urlPattern.matcher(text).matches();
    }

    public static boolean isPrefixedURL(String text) {
        String[] textArray = text.split(" ");
        if (textArray.length > 1)
            return urlPattern.matcher(textArray[0]).matches() || urlPattern.matcher(textArray[textArray.length - 1]).matches();
        return false;
    }

    public static String removePrefix(String text) {
        String[] textArray = text.split(" ");
        if (text.length() > 1) {
            if (urlPattern.matcher(textArray[0]).matches())
                return textArray[0];
            if (urlPattern.matcher(textArray[textArray.length - 1]).matches())
                return textArray[1];
        }
        return text;
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

        return emailPattern.matcher(text).matches();
    }

    public static boolean isEmailToken(String text) {
        return text.startsWith("v-");
    }

    public static boolean isAdminCommand(String text, boolean isAdmin) {
        return isAdmin && text.startsWith("//");
    }
}
