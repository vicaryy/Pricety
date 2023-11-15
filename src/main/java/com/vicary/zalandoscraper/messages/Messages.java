package com.vicary.zalandoscraper.messages;

import com.vicary.zalandoscraper.thread_local.ActiveLanguage;

public class Messages {
    private final static String menu = "menu-";
    private final static String allProducts = "allProducts-";
    private final static String addProduct = "addProduct-";
    private final static String editPriceAlert = "editPriceAlert-";
    private final static String notification = "notification-";


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
}
