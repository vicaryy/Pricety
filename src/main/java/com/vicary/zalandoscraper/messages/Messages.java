package com.vicary.zalandoscraper.messages;

import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {

    private static ResourceBundle resourceBundle;

    private static String baseName = "messages";

    public static void setLanguage(String language) {
        Locale locale = Locale.of(language);
        resourceBundle = ResourceBundle.getBundle(baseName, locale);
    }

    public static void setBaseName(String baseName) {
        Messages.baseName = baseName;
    }

    public static String get(String key) {
        return resourceBundle.getString(key);
    }

    public static String menu(String key) {
        return resourceBundle.getString("menu-" + key);
    }

    public static String notification(String key) {
        return resourceBundle.getString("notification-" + key);
    }

    public static String allProducts(String key) {
        return resourceBundle.getString("allProducts-" + key);
    }

    public static String editPriceAlert(String key) {
        return resourceBundle.getString("editPriceAlert-" + key);
    }
}
