package com.vicary.zalandoscraper.api_telegram.api_request.games;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.zalandoscraper.api_telegram.api_object.games.GameHighScore;
import lombok.*;
import com.vicary.zalandoscraper.api_telegram.api_request.ApiRequestList;
import com.vicary.zalandoscraper.api_telegram.service.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class GetGameHighScores implements ApiRequestList<GameHighScore> {
    /**
     * Use this method to get data for high score tables.
     *
     * @param userId            Target user id.
     * @param chatId            Unique identifier for the target chat. Required if inlineMessageId is not specified.
     * @param messageId         Identifier of the sent message. Required if inlineMessageId is not specified.
     * @param inlineMessageId   Identifier of the inline message. Required if chatId and messageId are not specified.
     */

    @NonNull
    @JsonProperty("user_id")
    private Integer userId;

    @NonNull
    @JsonProperty("chat_id")
    private Integer chatId;

    @NonNull
    @JsonProperty("message_id")
    private Integer messageId;

    @Override
    public GameHighScore getReturnObject() {
        return new GameHighScore();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.GET_GAME_HIGH_SCORES.getPath();
    }

    @Override
    public void checkValidation() {
    }
}
