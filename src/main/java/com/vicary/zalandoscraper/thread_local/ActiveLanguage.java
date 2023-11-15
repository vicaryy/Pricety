package com.vicary.zalandoscraper.thread_local;

import lombok.Data;

import java.util.ResourceBundle;

@Data
public class ActiveLanguage {

    private final static ThreadLocal<ActiveLanguage> activeLanguage = ThreadLocal.withInitial(ActiveLanguage::new);

    private ResourceBundle resourceBundle;

    public static ActiveLanguage get() {
        return activeLanguage.get();
    }

    public static void remove() {
        activeLanguage.remove();
    }

    public boolean isActive() {
        return resourceBundle != null;
    }
}
