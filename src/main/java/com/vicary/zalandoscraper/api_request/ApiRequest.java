package com.vicary.zalandoscraper.api_request;

public interface ApiRequest<T> extends Validation {
    T getReturnObject();

    public String getEndPoint();
}
