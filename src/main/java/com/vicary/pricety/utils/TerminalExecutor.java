package com.vicary.pricety.utils;

import com.vicary.pricety.scraper.config.BrowserType;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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

    public static void deleteFile(File file) {
        try {
            FileUtils.forceDelete(file);
        } catch (IOException e) {
            logger.error("Failed to delete file '{}'", file.getAbsolutePath());
        }
    }
}
