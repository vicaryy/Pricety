package com.vicary.zalandoscraper.exception;

public class IllegalInputException extends ZalandoScraperBotException {
    public IllegalInputException() {
        super();
    }

    public IllegalInputException(String message, String loggerMessage) {
        super(message, loggerMessage);
    }
}
