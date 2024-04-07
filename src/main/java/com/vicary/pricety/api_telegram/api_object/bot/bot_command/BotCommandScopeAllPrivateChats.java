package com.vicary.pricety.api_telegram.api_object.bot.bot_command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
public class BotCommandScopeAllPrivateChats implements BotCommandScope {
    @JsonProperty("type")
    private final String type = "all_private_chats";
}
