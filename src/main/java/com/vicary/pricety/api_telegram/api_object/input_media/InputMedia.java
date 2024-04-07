package com.vicary.pricety.api_telegram.api_object.input_media;

import com.vicary.pricety.api_telegram.api_object.ApiObject;

public interface InputMedia extends ApiObject {
    String getType();

    String getMedia();
}
