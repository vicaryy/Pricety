package com.vicary.pricety.api_telegram.api_object.poll;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import com.vicary.pricety.api_telegram.api_object.ApiObject;

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
