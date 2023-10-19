package com.vicary.zalandoscraper.api_object.bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.vicary.zalandoscraper.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class BotDescription implements ApiObject {
    @JsonProperty("description")
    private String description;
}
