package com.vicary.pricety.api_telegram.api_object.video;

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
public class VideoChatParticipantsInvited implements ApiObject {
    @JsonProperty("users")
    private List<User> users;

    private VideoChatParticipantsInvited() {
    }
}
