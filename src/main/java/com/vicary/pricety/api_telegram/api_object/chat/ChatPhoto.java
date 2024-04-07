package com.vicary.pricety.api_telegram.api_object.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.pricety.api_telegram.api_object.ApiObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class ChatPhoto implements ApiObject {
    @JsonProperty("small_file_id")
    private String smallFileId;

    @JsonProperty("small_file_unique_id")
    private String smallFileUniqueId;

    @JsonProperty("big_file_id")
    private String bigFileId;

    @JsonProperty("big_file_unique_id")
    private String bigFileUniqueId;

    private ChatPhoto() {
    }
}
