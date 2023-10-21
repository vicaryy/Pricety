package com.vicary.zalandoscraper;

import lombok.Data;

@Data
public class ActiveUser {

    private static ThreadLocal<ActiveUser> activeUserThreadLocal = ThreadLocal.withInitial(ActiveUser::new);

    private String userId;

    private String chatId;

    private String messageId;

    private String text;

    private boolean premium;

    private boolean admin;

    public static ActiveUser get() {
        return activeUserThreadLocal.get();
    }

    public static void remove() {
        activeUserThreadLocal.remove();
    }

    public boolean isActive() {
        return userId != null;
    }
}
