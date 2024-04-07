package com.vicary.pricety.api_telegram.api_object.bot.bot_command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.pricety.api_telegram.api_object.ApiObject;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BotCommand implements ApiObject {
    @JsonProperty("command")
    private String command;

    @JsonProperty("description")
    private String description;
}
