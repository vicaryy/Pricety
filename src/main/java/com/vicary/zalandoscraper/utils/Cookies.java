package com.vicary.zalandoscraper.utils;

import jakarta.servlet.http.Cookie;

public class Cookies {

    public static Cookie getJwtCookie(String jwt) {
        Cookie cookie = new Cookie("access_key", jwt);
        cookie.setPath("/");
        return cookie;
    }

    public static Cookie getEmptyJwtCookie() {
        Cookie cookie = new Cookie("access_key", "");
        cookie.setPath("/");
        return cookie;
    }

    public static void clearJwtCookies(Cookie[] cookies) {
        for (Cookie c : cookies)
            if (c.getName().equals("access_key"))
                c.setValue("");
    }

    public static String getJwtFromCookies(Cookie[] cookies) {
        for (Cookie c : cookies)
            if (c.getName().equals("access_key"))
                return c.getValue();
        return "";
    }
}
