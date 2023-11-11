package com.vicary.zalandoscraper.api_telegram.api_request.bot_info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.vicary.zalandoscraper.api_telegram.api_object.bot.BotDescription;
import com.vicary.zalandoscraper.api_telegram.api_request.ApiRequest;
import com.vicary.zalandoscraper.api_telegram.end_point.EndPoint;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetMyDescription implements ApiRequest<BotDescription> {
    /**
     * Use this method to get the current bot description for the given user language.
     *
     * @param languageCode  A two-letter ISO 639-1 language code or an empty string.
     */
    @JsonProperty("language_code")
    private String languageCode;

    @Override
    public BotDescription getReturnObject() {
        return new BotDescription();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.GET_MY_DESCRIPTION.getPath();
    }

    @Override
    public void checkValidation() {
    }
}
