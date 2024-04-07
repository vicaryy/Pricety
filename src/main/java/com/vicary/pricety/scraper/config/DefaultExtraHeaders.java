package com.vicary.pricety.scraper.config;

import java.util.HashMap;

public class DefaultExtraHeaders extends HashMap<String, String> {
    {
        put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
        put("Upgrade-Insecure-Requests", "1");
        put("Sec-Fetch-User", "?1");
        put("Sec-Fetch-Site", "same-origin");
        put("Sec-Fetch-Mode", "navigate");
        put("Sec-Fetch-Dest", "dust");
        put("Sec-Ch-Ua-Platform", "\"macOS\"");
        put("Sec-Ch-Ua-Mobile", "?0");
        put("Sec-Ch-Ua", "\"Google Chrome\";v=\"119\", \"Chromium\";v=\"119\", \"Not?A_Brand\";v=\"24\"");
        put("Cache-Control", "max-age=0");
    }
}
