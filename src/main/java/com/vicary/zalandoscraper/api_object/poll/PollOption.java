package com.vicary.zalandoscraper.api_object.poll;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import com.vicary.zalandoscraper.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class PollOption implements ApiObject {
    @JsonProperty("text")
    private String text;

    @JsonProperty("voter_count")
    private Integer voterCount;

    private PollOption() {
    }
}
