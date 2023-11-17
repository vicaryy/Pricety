package com.vicary.zalandoscraper.messages;

import com.vicary.zalandoscraper.thread_local.ActiveLanguage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {
    private final static String menu = "menu-";
    private final static String allProducts = "allProducts-";
    private final static String addProduct = "addProduct-";
    private final static String editPriceAlert = "editPriceAlert-";
    private final static String deleteProduct = "deleteProduct-";
    private final static String notification = "notifications-";
    private final static String other = "other-";
    private final static String email = "email-";
    private final static String chat = "chat-";
    private final static String command = "command-";


    public static String menu(String key) {
        return ActiveLanguage.get().getResourceBundle().getString(menu + key);
    }

    public static String allProducts(String key) {
        return ActiveLanguage.get().getResourceBundle().getString(allProducts + key);
    }

    public static String addProduct(String key) {
        return ActiveLanguage.get().getResourceBundle().getString(addProduct + key);
    }

    public static String editPriceAlert(String key) {
        return ActiveLanguage.get().getResourceBundle().getString(editPriceAlert + key);
    }

    public static String deleteProduct(String key) {
        return ActiveLanguage.get().getResourceBundle().getString(deleteProduct + key);
    }

    public static String notifications(String key) {
        return ActiveLanguage.get().getResourceBundle().getString(notification + key);
    }

    public static String other(String key) {
        return ActiveLanguage.get().getResourceBundle().getString(other + key);
    }

    public static String email(String key, String language) {
        return ResourceBundle.getBundle("messages", Locale.of(language)).getString(email + key);
    }

    public static String email(String key) {
        return ActiveLanguage.get().getResourceBundle().getString(email + key);
    }

    public static String chat(String key, String language) {
        return ResourceBundle.getBundle("messages", Locale.of(language)).getString(chat + key);
    }

    public static String command(String key) {
        return ActiveLanguage.get().getResourceBundle().getString(command + key);
    }

}
