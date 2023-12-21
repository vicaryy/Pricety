package com.vicary.zalandoscraper.exception;

public class InvalidLinkException extends ScraperBotException {

    public InvalidLinkException() {
        super();
    }

    public InvalidLinkException(String message, String loggerMessage) {
        super(message, loggerMessage);
    }
}
