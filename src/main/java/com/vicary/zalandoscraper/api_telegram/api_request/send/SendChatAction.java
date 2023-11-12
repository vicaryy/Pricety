package com.vicary.zalandoscraper.api_telegram.api_request.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.zalandoscraper.api_telegram.api_object.Action;
import com.vicary.zalandoscraper.api_telegram.api_request.ApiRequest;
import com.vicary.zalandoscraper.api_telegram.service.EndPoint;
import lombok.*;

@ToString
@EqualsAndHashCode
public class SendChatAction implements ApiRequest<Boolean> {
    /**
     * Use this method when you need to tell the user that something is happening on the bot's side.
     * The status is set for 5 seconds or less (when a message arrives from your bot, Telegram clients clear its typing status).
     * Returns True on success.
     *
     * @param chatId                Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * @param messageThreadId       Unique identifier for the target message thread; supergroups only.
     * @param action                Type of action to broadcast.
     * Choose one, depending on what the user is about to receive:
     * - "typing" for text messages,
     * - "upload_photo" for photos,
     * - "record_video" or "upload_video" for videos,
     * - "record_voice" or "upload_voice" for voice notes,
     * - "upload_document" for general files,
     * - "choose_sticker" for stickers,
     * - "find_location" for location data,
     * - "record_video_note" or "upload_video_note" for video notes.
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
    private Action action;

    @JsonProperty("action")
    private String actionToSend;

    public SendChatAction(@NonNull String chatId, @NonNull Action action, Integer messageThreadId) {
        this.chatId = chatId;
        this.messageThreadId = messageThreadId;
        this.action = action;
    }

    public SendChatAction(@NonNull String chatId, @NonNull Action action) {
        this.chatId = chatId;
        this.action = action;
    }

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SEND_CHAT_ACTION.getPath();
    }

    @Override
    public void checkValidation() {
        actionToSend = action.toString().toLowerCase();
    }

    public static SendChatActionBuilder builder() {
        return new SendChatActionBuilder();
    }

    public static class SendChatActionBuilder {
        private @NonNull String chatId;
        private @NonNull Action action;
        private Integer messageThreadId;

        SendChatActionBuilder() {
        }

        @JsonProperty("chat_id")
        public SendChatActionBuilder chatId(@NonNull String chatId) {
            this.chatId = chatId;
            return this;
        }

        @JsonProperty("message_thread_id")
        public SendChatActionBuilder messageThreadId(Integer messageThreadId) {
            this.messageThreadId = messageThreadId;
            return this;
        }

        public SendChatActionBuilder action(@NonNull Action action) {
            this.action = action;
            return this;
        }

        public SendChatAction build() {
            return new SendChatAction(this.chatId, this.action, this.messageThreadId);
        }
    }
}
