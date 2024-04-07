package com.vicary.pricety.api_telegram.api_request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.pricety.api_telegram.api_object.other.UserProfilePhotos;
import lombok.*;
import com.vicary.pricety.api_telegram.service.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class GetUserProfilePhotos implements ApiRequest<UserProfilePhotos> {
    /**
     * Use this method to get a list of profile pictures for a user.
     * Returns a UserProfilePhotos object.
     *
     * @param userId                Unique identifier of the target user.
     * @param offset                Sequential number of the first photo to be returned.
     * By default, all photos are returned.
     * @param limit                 Limits the number of photos to be retrieved.
     * Values between 1-100 are accepted. Defaults to 100.
     */
    @NonNull
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("offset")
    private Integer offset;

    @JsonProperty("limit")
    private Integer limit;

    @Override
    public UserProfilePhotos getReturnObject() {
        return new UserProfilePhotos();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.GET_USER_PROFILE_PHOTOS.getPath();
    }

    @Override
    public void checkValidation() {
    }
}
