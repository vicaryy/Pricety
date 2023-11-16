package com.vicary.zalandoscraper.messages;

import com.vicary.zalandoscraper.thread_local.ActiveLanguage;

public class Messages {
    private final static String menu = "menu-";
    private final static String allProducts = "allProducts-";
    private final static String addProduct = "addProduct-";
    private final static String editPriceAlert = "editPriceAlert-";
    private final static String deleteProduct = "deleteProduct-";
    private final static String notification = "notifications-";
    private final static String other = "other-";


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
}
