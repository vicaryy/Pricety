package com.vicary.zalandoscraper.api_telegram.api_request.edit_message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.zalandoscraper.api_telegram.api_request.ApiRequest;
import lombok.*;
import com.vicary.zalandoscraper.api_telegram.service.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class EditMessageLiveLocationAsInlineMessage implements ApiRequest<Boolean> {
    /**
     Use this method to edit live location messages.
     A location can be edited until its live_period expires or editing is explicitly disabled by a call to stopMessageLiveLocation.
     On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned.
     *
     * @param chatId                   Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     *                                 Required if inlineMessageId is not specified.
     * @param messageId                Identifier of the message to edit. Required if inlineMessageId is not specified.
     * @param inlineMessageId          Identifier of the inline message. Required if chatId and messageId are not specified.
     * @param latitude                 Latitude of new location.
     * @param longitude                Longitude of new location.
     * @param horizontalAccuracy       The radius of uncertainty for the location, measured in meters; 0-1500.
     * @param heading                  Direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
     * @param proximityAlertRadius     The maximum distance for proximity alerts about approaching another chat member, in meters.
     *                                 Must be between 1 and 100000 if specified.
     * @param replyMarkup              A JSON-serialized object for a new inline keyboard.
     */

    @NonNull
    @JsonProperty("inline_message_id")
    private String inlineMessageId;

    @NonNull
    @JsonProperty("latitude")
    private Float latitude;

    @NonNull
    @JsonProperty("longitude")
    private Float longitude;

    @JsonProperty("horizontal_accuracy")
    private Float horizontalAccuracy;

    @JsonProperty("heading")
    private Integer heading;

    @JsonProperty("proximity_alert_radius")
    private Integer proximityAlertRadius;

//    @JsonProperty("reply_markup")
//    private InlineKeyboardMarkup replyMarkup;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.EDIT_MESSAGE_LIVE_LOCATION.getPath();
    }

    @Override
    public void checkValidation() {
        // Additional validation checks can be added as needed.
    }
}
