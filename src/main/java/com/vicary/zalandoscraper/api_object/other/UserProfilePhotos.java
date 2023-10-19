package com.vicary.zalandoscraper.api_object.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import com.vicary.zalandoscraper.api_object.ApiObject;
import com.vicary.zalandoscraper.api_object.PhotoSize;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfilePhotos implements ApiObject {
    @JsonProperty("total_count")
    private Integer totalCount;

    @JsonProperty("photos")
    private List<List<PhotoSize>> photos;
}
