package com.vicary.pricety.api_telegram.api_request;

public interface ApiRequest<T> extends Validation {
    T getReturnObject();

    public String getEndPoint();
}
