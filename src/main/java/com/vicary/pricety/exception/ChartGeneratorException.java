package com.vicary.pricety.exception;

public class ChartGeneratorException extends ScraperBotException {
    public ChartGeneratorException() {
        super();
    }

    public ChartGeneratorException(String message, String loggerMessage) {
        super(message, loggerMessage);
    }
}