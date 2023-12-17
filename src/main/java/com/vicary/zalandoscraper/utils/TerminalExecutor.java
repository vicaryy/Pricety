package com.vicary.zalandoscraper.utils;

import com.vicary.zalandoscraper.scraper.config.BrowserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TerminalExecutor {

    private final static Logger logger = LoggerFactory.getLogger(TerminalExecutor.class);

    public static void shutdownBrowser(BrowserType browser) {
        String br = browser.toString();

        ProcessBuilder processBuilder = new ProcessBuilder("pkill", br);
        logger.info("[Terminal Executor] Killed {} process", br);
        try {
            processBuilder.start();
        } catch (IOException e) {
            logger.error("[Terminal Executor] Failed in killing {} process", br);
            throw new RuntimeException(e);
        }
    }
}
