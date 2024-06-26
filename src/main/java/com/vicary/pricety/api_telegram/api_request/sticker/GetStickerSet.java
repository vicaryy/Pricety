package com.vicary.pricety.api_telegram.api_request.sticker;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import com.vicary.pricety.api_telegram.api_object.stickers.StickerSet;
import com.vicary.pricety.api_telegram.api_request.ApiRequest;
import com.vicary.pricety.api_telegram.service.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class GetStickerSet implements ApiRequest<StickerSet> {
    /**
     * Use this method to get a sticker set.
     *
     * @param name Name of the sticker set.
     */
    @NonNull
    @JsonProperty("name")
    private String name;

    @Override
    public StickerSet getReturnObject() {
        return new StickerSet();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.GET_STICKER_SET.getPath();
    }

    @Override
    public void checkValidation() {
    }
}
