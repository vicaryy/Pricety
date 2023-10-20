package com.vicary.zalandoscraper.exception;

public class InvalidLinkException extends ZalandoScraperBotException {

    public InvalidLinkException() {
        super();
    }

    public InvalidLinkException(String message, String loggerMessage) {
        super(message, loggerMessage);
    }
}
