package com.vicary.zalandoscraper.exception;

import lombok.Getter;

@Getter
public class ZalandoScraperBotException extends RuntimeException {

    private String loggerMessage;

    public ZalandoScraperBotException() {
        super();
    }
    public ZalandoScraperBotException(String message, String loggerMessage) {
        super(message);
        this.loggerMessage = loggerMessage;
    }
    public ZalandoScraperBotException(String message) {
        super(message);
    }

}
