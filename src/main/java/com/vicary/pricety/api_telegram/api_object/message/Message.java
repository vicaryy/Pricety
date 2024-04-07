package com.vicary.pricety.api_telegram.api_object.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.pricety.api_telegram.api_object.*;
import com.vicary.pricety.api_telegram.api_object.forum_topic.*;
import com.vicary.pricety.api_telegram.api_object.keyboard.InlineKeyboardMarkup;
import com.vicary.pricety.api_telegram.api_object.payments.Invoice;
import com.vicary.pricety.api_telegram.api_object.payments.SuccessfulPayment;
import com.vicary.pricety.api_telegram.api_object.stickers.Sticker;
import com.vicary.pricety.api_telegram.api_object.telegram_passport.PassportData;
import com.vicary.pricety.api_telegram.api_object.games.Game;
import com.vicary.pricety.api_telegram.api_object.other.*;
import com.vicary.pricety.api_telegram.api_object.poll.Poll;
import com.vicary.pricety.api_telegram.api_object.video.*;
import lombok.*;
import com.vicary.pricety.api_telegram.api_object.chat.Chat;
import com.vicary.pricety.api_telegram.api_object.chat.ChatShared;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Message implements ApiObject {
    @JsonProperty("message_id")
    private Integer messageId;

    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @JsonProperty("from")
    private User from;

    @JsonProperty("sender_chat")
    private Chat senderChat;

    @JsonProperty("date")
    private Integer date;

    @JsonProperty("chat")
    private Chat chat;

    @JsonProperty("forward_form")
    private User forwardFrom;

    @JsonProperty("forward_from_chat")
    private Chat forwardFromChat;

    @JsonProperty("forward_from_message")
    private Integer forwardFromMessage;

    @JsonProperty("forward_signature")
    private String forwardSignature;

    @JsonProperty("forward_sender_name")
    private String forwardSenderName;

    @JsonProperty("forward_date")
    private Integer forwardDate;

    @JsonProperty("is_topic_message")
    private Boolean isTopicMessage;

    @JsonProperty("is_automatic_forward")
    private Boolean isAutomaticForward;

    @JsonProperty("reply_to_message")
    private Message replyToMessage;

    @JsonProperty("via_bot")
    private User viaBot;

    @JsonProperty("edit_date")
    private Integer editDate;

    @JsonProperty("has_protected_content")
    private Boolean hasProtectedContent;

    @JsonProperty("media_group_id")
    private String mediaGroupId;

    @JsonProperty("author_signature")
    private String authorSignature;

    @JsonProperty("text")
    private String text;

//    @JsonProperty("entities")
//    private List<MessageEntity> entities;

    @JsonProperty("animation")
    private Animation animation;

    @JsonProperty("audio")
    private Audio audio;

    @JsonProperty("document")
    private Document document;

//    @JsonProperty("photo")
//    private List<PhotoSize> photo;

    @JsonProperty("sticker")
    private Sticker sticker;

    @JsonProperty("video")
    private Video video;

    @JsonProperty("video_note")
    private VideoNote videoNote;

    @JsonProperty("voice")
    private Voice voice;

    @JsonProperty("caption")
    private String caption;

//    @JsonProperty("caption_entities")
//    private List<MessageEntity> captionEntities;

    @JsonProperty("has_media_spoiler")
    private Boolean hasMediaSpoiler;

    @JsonProperty("contact")
    private Contact contact;

    @JsonProperty("dice")
    private Dice dice;

    @JsonProperty("game")
    private Game game;

    @JsonProperty("poll")
    private Poll poll;

    @JsonProperty("venue")
    private Venue venue;

    @JsonProperty("location")
    private Location location;

//    @JsonProperty("new_chat_members")
//    private List<User> newChatMembers;

    @JsonProperty("left_chat_member")
    private User leftChatMember;

    @JsonProperty("new_chat_title")
    private String newChatTitle;

//    @JsonProperty("new_chat_photo")
//    private List<PhotoSize> newChatPhoto;

    @JsonProperty("delete_chat_photo")
    private Boolean deleteChatPhoto;

    @JsonProperty("group_chat_created")
    private Boolean groupChatCreated;

    @JsonProperty("supergroup_chat_created")
    private Boolean supergroupChatCreated;

    @JsonProperty("channel_chat_created")
    private Boolean channelChatCreated;

    @JsonProperty("message_auto_delete_timer_changed")
    private MessageAutoDeleteTimerChanged messageAutoDeleteTimerChanged;

    @JsonProperty("migrate_to_chat_id")
    private Integer migrateToChatId;

    @JsonProperty("migrate_from_chat_id")
    private Integer migrateFromChatId;

    @JsonProperty("pinned_message")
    private Message pinnedMessage;

    @JsonProperty("invoice")
    private Invoice invoice;

    @JsonProperty("successful_payment")
    private SuccessfulPayment successfulPayment;

    @JsonProperty("user_shared")
    private UserShared userShared;

    @JsonProperty("chat_shared")
    private ChatShared chatShared;

    @JsonProperty("connected_website")
    private String connectedWebsite;

    @JsonProperty("write_access_allowed")
    private WriteAccessAllowed writeAccessAllowed;

    @JsonProperty("passport_data")
    private PassportData passportData;

    @JsonProperty("proximity_alert_triggered")
    private ProximityAlertTriggered proximityAlertTriggered;

    @JsonProperty("forum_topic_created")
    private ForumTopicCreated forumTopicCreated;

    @JsonProperty("forum_topic_edited")
    private ForumTopicEdited forumTopicEdited;

    @JsonProperty("forum_topic_closed")
    private ForumTopicClosed forumTopicClosed;

    @JsonProperty("forum_topic_reopened")
    private ForumTopicReopened forumTopicReopened;

    @JsonProperty("general_forum_topic_hidden")
    private GeneralForumTopicHidden generalForumTopicHidden;

    @JsonProperty("general_forum_topic_unhidden")
    private GeneralForumTopicUnhidden generalForumTopicUnhidden;

    @JsonProperty("video_chat_scheduled")
    private VideoChatScheduled videoChatScheduled;

    @JsonProperty("video_chat_started")
    private VideoChatStarted videoChatStarted;

    @JsonProperty("video_chat_ended")
    private VideoChatEnded videoChatEnded;

    @JsonProperty("video_chat_participants_invited")
    private VideoChatParticipantsInvited videoChatParticipantsInvited;

    @JsonProperty("web_app_data")
    private WebAppData webAppData;

    @JsonProperty("inline_keyboard_markup")
    private InlineKeyboardMarkup inlineKeyboardMarkup;
}
