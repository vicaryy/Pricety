package com.vicary.zalandoscraper.api_object.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import com.vicary.zalandoscraper.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class WebAppInfo implements ApiObject {
    @JsonProperty("url")
    private String url;

    private WebAppInfo() {
    }
}
