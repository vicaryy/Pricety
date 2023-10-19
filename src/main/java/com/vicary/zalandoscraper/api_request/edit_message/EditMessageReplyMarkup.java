package com.vicary.zalandoscraper.api_request.edit_message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.zalandoscraper.api_object.keyboard.InlineKeyboardMarkup;
import com.vicary.zalandoscraper.api_object.keyboard.ReplyMarkup;
import com.vicary.zalandoscraper.api_request.ApiRequest;
import lombok.*;
import com.vicary.zalandoscraper.api_object.message.Message;
import com.vicary.zalandoscraper.end_point.EndPoint;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditMessageReplyMarkup implements ApiRequest<Message> {
    /**
     * Use this method to edit only the reply markup of messages.
     *
     * @param chatId              Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * Required if inlineMessageId is not specified.
     * @param messageId           Identifier of the message to edit. Required if inlineMessageId is not specified.
     * @param inlineMessageId     Identifier of the inline message. Required if chatId and messageId are not specified.
     * @param replyMarkup         A JSON-serialized object for an inline keyboard.
     */

    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_id")
    private Integer messageId;

    @JsonProperty("inline_message_id")
    private String inlineMessageId;

    @JsonProperty("reply_markup")
    private InlineKeyboardMarkup replyMarkup;

    @Override
    public Message getReturnObject() {
        return new Message();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.EDIT_MESSAGE_REPLY_MARKUP.getPath();
    }

    @Override
    public void checkValidation() {
        if (replyMarkup != null)
            replyMarkup.checkValidation();

        if (inlineMessageId != null && (chatId != null || messageId != null))
            throw new IllegalArgumentException("If inline message id specified, chat id and message id must be null.");

        if (chatId == null && messageId == null && inlineMessageId == null)
            throw new IllegalArgumentException("Chat id and message id or inline message id must be specified.");

        if (chatId != null && messageId == null)
            throw new IllegalArgumentException("Chat id and message id must be specified.");

        if (chatId == null && messageId != null)
            throw new IllegalArgumentException("Chat id and message id must be specified.");
    }
}
