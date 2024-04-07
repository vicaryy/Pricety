package com.vicary.pricety.api_telegram.api_request.sticker;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import com.vicary.pricety.api_telegram.api_object.stickers.Sticker;
import com.vicary.pricety.api_telegram.api_request.ApiRequestList;
import com.vicary.pricety.api_telegram.service.EndPoint;

import java.util.List;

@Data
@RequiredArgsConstructor
@Builder
public class GetCustomEmojiStickers implements ApiRequestList<Sticker> {
    /**
     * Use this method to get information about custom emoji stickers by their identifiers.
     *
     * @param customEmojiIds List of custom emoji identifiers. At most 200 custom emoji identifiers can be specified.
     */
    @NonNull
    @JsonProperty("custom_emoji_ids")
    private List<String> customEmojiIds;

    @Override
    public Sticker getReturnObject() {
        return new Sticker();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.GET_CUSTOM_EMOJI_STICKERS.getPath();
    }

    @Override
    public void checkValidation() {
        if (customEmojiIds.size() > 200)
            throw new IllegalArgumentException("At most 200 custom emoji identifiers can be specified.");
    }
}
