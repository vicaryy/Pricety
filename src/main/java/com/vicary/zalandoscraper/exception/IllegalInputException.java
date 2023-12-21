package com.vicary.zalandoscraper.exception;

public class IllegalInputException extends ScraperBotException {
    public IllegalInputException() {
        super();
    }

    public IllegalInputException(String message, String loggerMessage) {
        super(message, loggerMessage);
    }
}
