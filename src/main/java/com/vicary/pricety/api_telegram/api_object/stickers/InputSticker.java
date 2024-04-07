package com.vicary.pricety.api_telegram.api_object.stickers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.pricety.api_telegram.api_object.ApiObject;
import com.vicary.pricety.api_telegram.api_request.InputFile;
import lombok.*;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class InputSticker implements ApiObject {
    /**
     * This object describes a sticker to be added to a sticker set.
     */
    @JsonProperty("sticker")
    public InputFile sticker;

    @JsonProperty("emoji_list")
    public List<String> emojiList;

    @JsonProperty("mask_position")
    public MaskPosition maskPosition;

    @JsonProperty("keywords")
    public List<String> keywords;
}
