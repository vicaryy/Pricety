package com.vicary.pricety.api_telegram.api_object.bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.pricety.api_telegram.api_object.ApiObject;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotShortDescription implements ApiObject {
    @JsonProperty("short_description")
    private String shortDescription;
}
