package com.vicary.zalandoscraper.exception;

public class TimeoutException extends ZalandoScraperBotException {
    public TimeoutException() {
        super();
    }

    public TimeoutException(String message, String loggerMessage) {
        super(message, loggerMessage);
    }

    public TimeoutException(String message) {
        super(message);
    }
}
