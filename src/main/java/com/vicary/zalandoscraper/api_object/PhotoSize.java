package com.vicary.zalandoscraper.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class PhotoSize implements ApiObject {
    @JsonProperty("file_id")
    private String fileId;

    @JsonProperty("file_unique_id")
    private String fileUniqueId;

    @JsonProperty("width")
    private Integer width;

    @JsonProperty("height")
    private Integer height;

    @JsonProperty("file_size")
    private Integer fileSize;

    private PhotoSize() {
    }
}
