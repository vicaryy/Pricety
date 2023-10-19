package com.vicary.zalandoscraper.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
public class UpdateResponse<T> implements ApiObject {
    @JsonProperty("ok")
    private boolean ok;

    @JsonProperty("result")
    private List<T> result;
}
