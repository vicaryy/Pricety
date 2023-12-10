package com.vicary.zalandoscraper.scraper;

import com.microsoft.playwright.BrowserType;

import java.util.List;

public class DefaultLaunchOptions extends BrowserType.LaunchOptions {
    public DefaultLaunchOptions() {
        super();
        setArgs(List.of(
                "--no-sandbox",
                "--disable-setuid-sandbox",
                "--disable-dev-shm-usage",
                "--disable-accelerated-2d-canvas",
                "--no-first-run",
                "--no-zygote",
                "--disable-gpu"
        ));
        setHeadless(false);
    }
}
