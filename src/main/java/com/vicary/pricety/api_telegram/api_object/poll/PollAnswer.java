package com.vicary.pricety.api_telegram.api_object.poll;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import com.vicary.pricety.api_telegram.api_object.ApiObject;
import com.vicary.pricety.api_telegram.api_object.User;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class PollAnswer implements ApiObject {
    @JsonProperty("poll_id")
    private String pollId;

    @JsonProperty("user")
    private User user;

    @JsonProperty("option_ids")
    private List<Integer> optionIds;

    private PollAnswer() {
    }
}
