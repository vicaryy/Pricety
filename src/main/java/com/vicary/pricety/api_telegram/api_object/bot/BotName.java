package com.vicary.pricety.api_telegram.api_object.bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.pricety.api_telegram.api_object.ApiObject;
import lombok.*;

@Data
@NoArgsConstructor
public class BotName implements ApiObject {
    @JsonProperty("name")
    private String name;
}
