package com.vicary.pricety.exception;

public class ActiveUserException extends ScraperBotException {
    public ActiveUserException() {
        super();
    }

    public ActiveUserException(String message, String loggerMessage) {
        super(message, loggerMessage);
    }
}
