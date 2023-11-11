package com.vicary.zalandoscraper.api_telegram.api_object.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import com.vicary.zalandoscraper.api_telegram.api_object.ApiObject;
import com.vicary.zalandoscraper.api_telegram.api_object.User;

@Getter
@ToString
@EqualsAndHashCode
public class ProximityAlertTriggered implements ApiObject {
    @JsonProperty("traveler")
    private User traveler;

    @JsonProperty("watcher")
    private User watcher;

    @JsonProperty("distance")
    private Integer distance;

    private ProximityAlertTriggered() {
    }
}
