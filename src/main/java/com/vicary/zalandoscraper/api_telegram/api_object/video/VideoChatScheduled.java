package com.vicary.zalandoscraper.api_telegram.api_object.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import com.vicary.zalandoscraper.api_telegram.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class VideoChatScheduled implements ApiObject {
    @JsonProperty("start_date")
    private Integer startDate;

    private VideoChatScheduled() {
    }
}
