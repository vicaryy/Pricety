package com.vicary.pricety.api_telegram.api_object.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import com.vicary.pricety.api_telegram.api_object.ApiObject;
import com.vicary.pricety.api_telegram.api_object.PhotoSize;

@Getter
@ToString
@EqualsAndHashCode
public class VideoNote implements ApiObject {
    @JsonProperty("file_id")
    private String fileId;

    @JsonProperty("file_unique_id")
    private String fileUniqueId;

    @JsonProperty("length")
    private Integer length;

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("thumbnail")
    private PhotoSize thumbnail;

    @JsonProperty("file_size")
    private Integer fileSize;

    private VideoNote() {
    }
}
