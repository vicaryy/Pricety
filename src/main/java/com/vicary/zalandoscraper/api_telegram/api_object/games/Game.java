package com.vicary.zalandoscraper.api_telegram.api_object.games;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.zalandoscraper.api_telegram.api_object.message.MessageEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import com.vicary.zalandoscraper.api_telegram.api_object.Animation;
import com.vicary.zalandoscraper.api_telegram.api_object.ApiObject;
import com.vicary.zalandoscraper.api_telegram.api_object.PhotoSize;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class Game implements ApiObject {
    /**
     * Represents a game.
     *
     * @param title        Title of the game.
     * @param description  Description of the game.
     * @param photo        Photo that will be displayed in the game message in chats.
     * @param text         Optional. Brief description of the game or high scores included in the game message.
     * @param textEntities Optional. Special entities that appear in text.
     * @param animation    Optional. Animation that will be displayed in the game message in chats.
     */
    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("photo")
    private List<PhotoSize> photo;

    @JsonProperty("text")
    private String text;

    @JsonProperty("text_entities")
    private List<MessageEntity> textEntities;

    @JsonProperty("animation")
    private Animation animation;

    private Game() {}
}
