package com.vicary.zalandoscraper.messages;

import com.vicary.zalandoscraper.format.MarkdownV2;
import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import com.vicary.zalandoscraper.thread_local.ActiveUser;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

public class Messages {
    private final static String menu = "menu-";
    private final static String allProducts = "allProducts-";
    private final static String addProduct = "addProduct-";
    private final static String editPriceAlert = "editPriceAlert-";
    private final static String deleteProduct = "deleteProduct-";
    private final static String generateProduct = "generateProduct-";
    private final static String notification = "notifications-";
    private final static String other = "other-";
    private final static String email = "email-";
    private final static String chat = "chat-";
    private final static String command = "command-";
    private final static String pretty = "pretty-";
    private final static String scraper = "scraper-";
    private final static String authenticate = "authenticate-";


    public static String menu(String key) {
        if (key.equals("welcome"))
            return "*Menu* ⚙️\n\n" + Messages.menu("how" + ThreadLocalRandom.current().nextInt(1, 10));

        return ActiveLanguage.get().getResourceBundle().getString(menu + key);
    }

    public static String allProducts(String key) {
        return ActiveLanguage.get().getResourceBundle().getString(allProducts + key);
    }

    public static String allProducts(String key, String language) {
        return ResourceBundle.getBundle("messages", Locale.of(language)).getString(allProducts + key);
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

    public static String generateProduct(String key) {
        return ActiveLanguage.get().getResourceBundle().getString(generateProduct + key);
    }

    public static String notifications(String key) {
        return ActiveLanguage.get().getResourceBundle().getString(notification + key);
    }

    public static String notifications(ActiveUser user) {
        String email = user.getEmail();
        String verified = "";
        if (email == null)
            email = Messages.notifications("notSpecified");

        else if (user.isVerifiedEmail())
            verified = "\n\n" + Messages.notifications("emailVerified");

        else
            verified = "\n\n" + Messages.notifications("emailNotVerified");

        return ActiveLanguage.get().getResourceBundle().getString(notification + "message")
                .formatted(
                        user.isNotifyByEmail() ? Messages.notifications("enabled") : Messages.notifications("disabled"),
                        MarkdownV2.apply(email).get(),
                        verified);
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

    public static String pretty(String key) {
        return ActiveLanguage.get().getResourceBundle().getString(pretty + key);
    }

    public static String scraper(String key) {
        return ActiveLanguage.get().getResourceBundle().getString(scraper + key);
    }

    public static String authenticate(String key) {
        return ActiveLanguage.get().getResourceBundle().getString(authenticate + key);
    }
}
