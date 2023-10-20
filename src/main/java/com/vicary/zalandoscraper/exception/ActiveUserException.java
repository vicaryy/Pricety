package com.vicary.zalandoscraper.exception;

public class ActiveUserException extends ZalandoScraperBotException{
    public ActiveUserException() {
        super();
    }

    public ActiveUserException(String message, String loggerMessage) {
        super(message, loggerMessage);
    }
}
