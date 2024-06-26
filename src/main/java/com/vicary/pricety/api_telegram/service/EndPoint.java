package com.vicary.pricety.api_telegram.service;

public enum EndPoint {
    GET_UPDATES("/getUpdates"),
    GET_UPDATES_OFFSET("/getUpdates?offset="),
    GET_ME("/getMe"),
    GET_FILE("/getFile"),
    LOG_OUT("/logOut"),
    CLOSE("/close"),
    SEND_MESSAGE("/sendMessage"),
    FORWARD_MESSAGE("/forwardMessage"),
    COPY_MESSAGE("/copyMessage"),
    SEND_PHOTO("/sendPhoto"),
    SEND_AUDIO("/sendAudio"),
    SEND_DOCUMENT("/sendDocument"),
    SEND_VIDEO("/sendVideo"),
    SEND_ANIMATION("/sendAnimation"),
    SEND_VOICE("/sendVoice"),
    SEND_VIDEO_NOTE("/sendVideoNote"),
    SEND_MEDIA_GROUP("/sendMediaGroup"),
    SEND_VENUE("/sendVenue"),
    SEND_CONTACT("/sendContact"),
    SEND_POLL("/sendPoll"),
    SEND_DICE("/sendDice"),
    SEND_CHAT_ACTION("/sendChatAction"),
    GET_USER_PROFILE_PHOTOS("/getUserProfilePhotos"),
    BAN_CHAT_MEMBER("/banChatMember"),
    UNBAN_CHAT_MEMBER("/unbanChatMember"),
    RESTRICT_CHAT_MEMBER("/restrictChatMember"),
    PROMOTE_CHAT_MEMBER("/promoteChatMember"),
    SET_CHAT_ADMINISTRATOR_CUSTOM_TITLE("/setChatAdministratorCustomTitle"),
    BAN_CHAT_SENDER_CHAT("/banChatSenderChat"),
    UNBAN_CHAT_SENDER_CHAT("/unbanChatSenderChat"),
    SET_CHAT_PERMISSIONS("/setChatPermissions"),
    EXPORT_CHAT_INVITE_LINK("/exportChatInviteLink"),
    CREATE_CHAT_INVITE_LINK("/createChatInviteLink"),
    EDIT_CHAT_INVITE_LINK("/editChatInviteLink"),
    REVOKE_CHAT_INVITE_LINK("/revokeChatInviteLink"),
    APPROVE_CHAT_JOIN_REQUEST("/approveChatJoinRequest"),
    DECLINE_CHAT_JOIN_REQUEST("/declineChatJoinRequest"),
    SET_CHAT_PHOTO("/setChatPhoto"),
    DELETE_CHAT_PHOTO("/deleteChatPhoto"),
    SET_CHAT_TITLE("/setChatTitle"),
    SET_CHAT_DESCRIPTION("/setChatDescription"),
    PIN_CHAT_MESSAGE("/pinChatMessage"),
    UNPIN_CHAT_MESSAGE("/unpinChatMessage"),
    UNPIN_ALL_CHAT_MESSAGES("/unpinAllChatMessages"),
    LEAVE_CHAT("/leaveChat"),
    GET_CHAT_ADMINISTRATORS("/getChatAdministrators"),
    GET_CHAT_MEMBER_COUNT("/getChatMemberCount"),
    GET_CHAT_MEMBER("/getChatMember"),
    SET_CHAT_STICKER_SET("/setChatStickerSet"),
    DELETE_CHAT_STICKER_SET("/deleteChatStickerSet"),
    GET_FORUM_TOPIC_ICON_STICKERS("/getForumTopicIconStickers"),
    CREATE_FORUM_TOPIC("/createForumTopic"),
    EDIT_FORUM_TOPIC("/editForumTopic"),
    CLOSE_FORUM_TOPIC("/closeForumTopic"),
    REOPEN_FORUM_TOPIC("/reopenForumTopic"),
    DELETE_FORUM_TOPIC("/deleteForumTopic"),
    UNPIN_ALL_FORUM_TOPIC_MESSAGES("/unpinAllForumTopicMessages"),
    EDIT_GENERAL_FORUM_TOPIC("/editGeneralForumTopic"),
    CLOSE_GENERAL_FORUM_TOPIC("/closeGeneralForumTopic"),
    REOPEN_GENERAL_FORUM_TOPIC("/reopenGeneralForumTopic"),
    HIDE_GENERAL_FORUM_TOPIC("/hideGeneralForumTopic"),
    UNHIDE_GENERAL_FORUM_TOPIC("/unhideGeneralForumTopic"),
    ANSWER_CALLBACK_QUERY("/answerCallbackQuery"),
    SET_MY_COMMANDS("/setMyCommands"),
    GET_MY_COMMANDS("/getMyCommands"),
    DELETE_MY_COMMANDS("/deleteMyCommands"),
    SET_MY_NAME("/setMyName"),
    GET_MY_NAME("/getMyName"),
    SET_MY_DESCRIPTION("/setMyDescription"),
    GET_MY_DESCRIPTION("/getMyDescription"),
    SET_MY_SHORT_DESCRIPTION("/setMyShortDescription"),
    GET_MY_SHORT_DESCRIPTION("/getMyShortDescription"),
    SET_CHAT_MENU_BUTTON("/setChatMenuButton"),
    GET_CHAT_MENU_BUTTON("/getChatMenuButton"),
    SET_MY_DEFAULT_ADMINISTRATOR_RIGHTS("/setMyDefaultAdministratorRights"),
    GET_MY_DEFAULT_ADMINISTRATOR_RIGHTS("/getMyDefaultAdministratorRights"),
    EDIT_MESSAGE_TEXT("/editMessageText"),
    EDIT_MESSAGE_CAPTION("/editMessageCaption"),
    EDIT_MESSAGE_MEDIA("/editMessageMedia"),
    EDIT_MESSAGE_LIVE_LOCATION("editMessageLiveLocation"),
    EDIT_MESSAGE_REPLY_MARKUP("/editMessageReplyMarkup"),
    STOP_POLL("/stopPoll"),
    DELETE_MESSAGE("/deleteMessage"),
    SEND_STICKER("/sendSticker"),
    GET_STICKER_SET("/getStickerSet"),
    GET_CUSTOM_EMOJI_STICKERS("/getCustomEmojiStickers"),
    UPLOAD_STICKER_FILE("/uploadStickerFile"),
    CREATE_NEW_STICKER_SET("/createNewStickerSet"),
    ANSWER_INLINE_QUERY("/answerInlineQuery"),
    ANSWER_WEBAPP_QUERY("/answerWebAppQuery"),
    SEND_INVOICE("/sendInvoice"),
    CREATE_INVOICE_LINK("createInvoiceLink"),
    ANSWER_SHIPPING_QUERY("answerShippingQuery"),
    ANSWER_PRE_CHECKOUT_QUERY("/answerPreCheckoutQuery"),
    SET_PASSPORT_DATA_ERRORS("/setPassportDataErrors"),
    SEND_GAME("/sendGame"),
    SET_GAME_SCORE("/setGameScore"),
    GET_GAME_HIGH_SCORES("/getGameHighScores"),
    SEND_LOCATION("/sendLocation");

    private final String path;

    EndPoint(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
