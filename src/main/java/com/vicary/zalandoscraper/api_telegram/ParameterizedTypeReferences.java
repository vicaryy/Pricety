package com.vicary.zalandoscraper.api_telegram;

import com.vicary.zalandoscraper.api_telegram.api_object.File;
import com.vicary.zalandoscraper.api_telegram.api_object.RequestResponse;
import com.vicary.zalandoscraper.api_telegram.api_object.RequestResponseList;
import com.vicary.zalandoscraper.api_telegram.api_object.User;
import com.vicary.zalandoscraper.api_telegram.api_object.bot.BotShortDescription;
import com.vicary.zalandoscraper.api_telegram.api_object.bot.bot_command.BotCommand;
import com.vicary.zalandoscraper.api_telegram.api_object.chat.ChatAdministratorRights;
import com.vicary.zalandoscraper.api_telegram.api_object.chat.ChatInviteLink;
import com.vicary.zalandoscraper.api_telegram.api_object.games.GameHighScore;
import com.vicary.zalandoscraper.api_telegram.api_object.inline_query.SentWebAppMessage;
import com.vicary.zalandoscraper.api_telegram.api_object.message.Message;
import com.vicary.zalandoscraper.api_telegram.api_object.other.UserProfilePhotos;
import com.vicary.zalandoscraper.api_telegram.api_object.poll.Poll;
import com.vicary.zalandoscraper.api_telegram.api_object.stickers.StickerSet;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;

import java.util.HashMap;

class ParameterizedTypeReferences {
    private final HashMap<Class, ParameterizedTypeReference> types = new HashMap<>();

    ParameterizedTypeReferences() {
        types.put(Message.class, new ParameterizedTypeReference<RequestResponse<Message>>() {
        });
        types.put(User.class, new ParameterizedTypeReference<RequestResponse<User>>() {
        });
        types.put(Boolean.class, new ParameterizedTypeReference<RequestResponse<Boolean>>() {
        });
        types.put(String.class, new ParameterizedTypeReference<RequestResponse<String>>() {
        });
        types.put(Integer.class, new ParameterizedTypeReference<RequestResponse<Integer>>() {
        });
        types.put(File.class, new ParameterizedTypeReference<RequestResponse<File>>() {
        });
        types.put(UserProfilePhotos.class, new ParameterizedTypeReference<RequestResponse<UserProfilePhotos>>() {
        });
        types.put(ChatInviteLink.class, new ParameterizedTypeReference<RequestResponse<ChatInviteLink>>() {
        });
        types.put(BotShortDescription.class, new ParameterizedTypeReference<RequestResponse<BotShortDescription>>() {
        });
        types.put(ChatAdministratorRights.class, new ParameterizedTypeReference<RequestResponse<ChatAdministratorRights>>() {
        });
        types.put(Poll.class, new ParameterizedTypeReference<RequestResponse<Poll>>() {
        });
        types.put(StickerSet.class, new ParameterizedTypeReference<RequestResponse<StickerSet>>() {
        });
        types.put(SentWebAppMessage.class, new ParameterizedTypeReference<RequestResponse<SentWebAppMessage>>() {
        });
        types.put(GameHighScore.class, new ParameterizedTypeReference<RequestResponse<GameHighScore>>() {
        });
        types.put(BotCommand.class, new ParameterizedTypeReference<RequestResponseList<BotCommand>>() {
        });
    }

    public ParameterizedTypeReference get(Class clazz) {
        return types.get(clazz);
    }
}
