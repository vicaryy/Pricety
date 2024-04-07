package com.vicary.pricety.exception;

import lombok.Getter;

@Getter
public class ScraperBotException extends RuntimeException {

    private String loggerMessage;

    public ScraperBotException() {
        super();
    }
    public ScraperBotException(String message, String loggerMessage) {
        super(message);
        this.loggerMessage = loggerMessage;
    }
    public ScraperBotException(String message) {
        super(message);
    }

}
