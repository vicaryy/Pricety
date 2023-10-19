package com.vicary.zalandoscraper.api_object.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import com.vicary.zalandoscraper.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class WebAppData implements ApiObject {
    @JsonProperty("data")
    private String data;

    @JsonProperty("button_text")
    private String buttonText;

    private WebAppData() {
    }
}
