package com.vicary.zalandoscraper.api_object.bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import com.vicary.zalandoscraper.api_object.ApiObject;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotShortDescription implements ApiObject {
    @JsonProperty("short_description")
    private String shortDescription;
}
