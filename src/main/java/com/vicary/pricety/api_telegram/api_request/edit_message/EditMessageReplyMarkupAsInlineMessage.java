package com.vicary.pricety.api_telegram.api_request.edit_message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.pricety.api_telegram.api_object.keyboard.InlineKeyboardMarkup;
import com.vicary.pricety.api_telegram.api_request.ApiRequest;
import lombok.*;
import com.vicary.pricety.api_telegram.service.EndPoint;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditMessageReplyMarkupAsInlineMessage implements ApiRequest<Boolean> {
    /**
     * Use this method to edit only the reply markup of messages.
     *
     * @param chatId              Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * Required if inlineMessageId is not specified.
     * @param messageId           Identifier of the message to edit. Required if inlineMessageId is not specified.
     * @param inlineMessageId     Identifier of the inline message. Required if chatId and messageId are not specified.
     * @param replyMarkup         A JSON-serialized object for an inline keyboard.
     */

    @NonNull
    @JsonProperty("inline_message_id")
    private String inlineMessageId;

    @JsonProperty("reply_markup")
    private InlineKeyboardMarkup replyMarkup;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.EDIT_MESSAGE_REPLY_MARKUP.getPath();
    }

    @Override
    public void checkValidation() {
        if (replyMarkup != null) {
            replyMarkup.checkValidation();
        }
    }
}
