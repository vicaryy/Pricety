package com.vicary.zalandoscraper.api_telegram.api_request.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.zalandoscraper.api_telegram.api_object.ParseMode;
import com.vicary.zalandoscraper.api_telegram.api_object.keyboard.ReplyKeyboardRemove;
import com.vicary.zalandoscraper.api_telegram.api_object.keyboard.ReplyMarkup;
import com.vicary.zalandoscraper.api_telegram.api_object.message.Message;
import com.vicary.zalandoscraper.api_telegram.api_object.message.MessageEntity;
import com.vicary.zalandoscraper.api_telegram.api_request.ApiRequest;
import com.vicary.zalandoscraper.api_telegram.service.EndPoint;
import lombok.*;

import java.util.List;

@EqualsAndHashCode
@ToString
public class SendMessage implements ApiRequest<Message> {
    /**
     * Use this method to send text messages. On success, the send Message is returned.
     *
     * @param chatId                 Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * @param messageThreadId        Unique identifier for the target message thread (topic) of the forum; for forum supergroups only.
     * @param text                   Text of the message to be sent, 1-4096 characters after entities parsing
     * @param parseMode              Mode for parsing entities in the message text. See formatting options for more details.
     * @param entities               A JSON-serialized list of special entities that appear in message text, which can be specified instead of parse_mode.
     * @param disableWebPagePreview  Disables link previews for links in this message
     * @param disableNotification    Sends the message silently. Users will receive a notification with no sound.
     * @param protectContent         Protects the contents of the sent message from forwarding and saving
     * @param replyToMessageId       If the message is a reply, ID of the original message
     * @param allowSendingWithoutReply Pass True if the message should be sent even if the specified replied-to message is not found
     * @param replyMarkup            Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
     */

    @Getter
    @Setter
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @Getter
    @Setter
    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @Getter
    @Setter
    @NonNull
    @JsonProperty("text")
    private String text;

    @JsonProperty("parse_mode")
    private String parseModeToSend;

    @Getter
    @Setter
    private ParseMode parseMode;

    @Getter
    @Setter
    @JsonProperty("entities")
    private List<MessageEntity> entities;

    @Getter
    @Setter
    @JsonProperty("disable_web_page_preview")
    private Boolean disableWebPagePreview;

    @Getter
    @Setter
    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    @Getter
    @Setter
    @JsonProperty("protect_content")
    private Boolean protectContent;

    @Getter
    @Setter
    @JsonProperty("reply_to_message_id")
    private Integer replyToMessageId;

    @Getter
    @Setter
    @JsonProperty("allow_sending_without_reply")
    private Boolean allowSendingWithoutReply;

    @Getter
    @Setter
    @JsonProperty("reply_markup")
    private ReplyMarkup replyMarkup;

    public SendMessage(@NonNull String chatId, Integer messageThreadId, @NonNull String text, ParseMode parseMode, List<MessageEntity> entities, Boolean disableWebPagePreview, Boolean disableNotification, Boolean protectContent, Integer replyToMessageId, Boolean allowSendingWithoutReply, ReplyMarkup replyMarkup) {
        this.chatId = chatId;
        this.messageThreadId = messageThreadId;
        this.text = text;
        this.parseMode = parseMode;
        this.entities = entities;
        this.disableWebPagePreview = disableWebPagePreview;
        this.disableNotification = disableNotification;
        this.protectContent = protectContent;
        this.replyToMessageId = replyToMessageId;
        this.allowSendingWithoutReply = allowSendingWithoutReply;
        this.replyMarkup = replyMarkup;
    }

    public SendMessage(@NonNull String chatId, @NonNull String text) {
        this.chatId = chatId;
        this.text = text;
    }


    @Override
    public void checkValidation() {

        if (parseMode != null)
            parseModeToSend = parseMode.toString();

        if (parseModeToSend == null)
            parseModeToSend = "";

        if (!parseModeToSend.equals("") && (entities != null && !entities.isEmpty()))
            throw new IllegalArgumentException("If entities are provided, the parse mode cannot be active.");

        if (replyMarkup != null)
            replyMarkup.checkValidation();

        if (replyMarkup == null)
            replyMarkup = new ReplyKeyboardRemove(true, true);
    }

    @Override
    public Message getReturnObject() {
        return new Message();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SEND_MESSAGE.getPath();
    }

    public static SendMessageBuilder builder() {
        return new SendMessageBuilder();
    }

    public static class SendMessageBuilder {
        private @NonNull String chatId;
        private Integer messageThreadId;
        private @NonNull String text;
        private ParseMode parseMode;
        private List<MessageEntity> entities;
        private Boolean disableWebPagePreview;
        private Boolean disableNotification;
        private Boolean protectContent;
        private Integer replyToMessageId;
        private Boolean allowSendingWithoutReply;
        private ReplyMarkup replyMarkup;

        SendMessageBuilder() {
        }

        @JsonProperty("chat_id")
        public SendMessageBuilder chatId(@NonNull String chatId) {
            this.chatId = chatId;
            return this;
        }

        @JsonProperty("message_thread_id")
        public SendMessageBuilder messageThreadId(Integer messageThreadId) {
            this.messageThreadId = messageThreadId;
            return this;
        }

        @JsonProperty("text")
        public SendMessageBuilder text(@NonNull String text) {
            this.text = text;
            return this;
        }

        public SendMessageBuilder parseMode(ParseMode parseMode) {
            this.parseMode = parseMode;
            return this;
        }

        @JsonProperty("entities")
        public SendMessageBuilder entities(List<MessageEntity> entities) {
            this.entities = entities;
            return this;
        }

        @JsonProperty("disable_web_page_preview")
        public SendMessageBuilder disableWebPagePreview(Boolean disableWebPagePreview) {
            this.disableWebPagePreview = disableWebPagePreview;
            return this;
        }

        @JsonProperty("disable_notification")
        public SendMessageBuilder disableNotification(Boolean disableNotification) {
            this.disableNotification = disableNotification;
            return this;
        }

        @JsonProperty("protect_content")
        public SendMessageBuilder protectContent(Boolean protectContent) {
            this.protectContent = protectContent;
            return this;
        }

        @JsonProperty("reply_to_message_id")
        public SendMessageBuilder replyToMessageId(Integer replyToMessageId) {
            this.replyToMessageId = replyToMessageId;
            return this;
        }

        @JsonProperty("allow_sending_without_reply")
        public SendMessageBuilder allowSendingWithoutReply(Boolean allowSendingWithoutReply) {
            this.allowSendingWithoutReply = allowSendingWithoutReply;
            return this;
        }

        @JsonProperty("reply_markup")
        public SendMessageBuilder replyMarkup(ReplyMarkup replyMarkup) {
            this.replyMarkup = replyMarkup;
            return this;
        }

        public SendMessage build() {
            return new SendMessage(this.chatId, this.messageThreadId, this.text, this.parseMode, this.entities, this.disableWebPagePreview, this.disableNotification, this.protectContent, this.replyToMessageId, this.allowSendingWithoutReply, this.replyMarkup);
        }
    }
}
