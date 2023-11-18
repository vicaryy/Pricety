package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.api_telegram.api_object.bot.bot_command.BotCommand;
import com.vicary.zalandoscraper.api_telegram.api_request.commands.DeleteMyCommands;
import com.vicary.zalandoscraper.api_telegram.api_request.commands.GetMyCommands;
import com.vicary.zalandoscraper.api_telegram.api_request.commands.SetMyCommands;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.api_telegram.service.RequestService;
import com.vicary.zalandoscraper.service.entity.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminResponse {

    private final UserService userService;
    private final RequestService requestService = new RequestService();

    public void response(String text, String chatId) {
        if (text.startsWith("/set premium "))
            setPremium(removePrefix(text), chatId);

        else if (text.startsWith("/set standard "))
            setStandard(removePrefix(text), chatId);

        else if (text.startsWith("/set admin "))
            setAdmin(removePrefix(text), chatId);

        else if (text.startsWith("/set non-admin "))
            setNonAdmin(removePrefix(text), chatId);

        else if (text.startsWith("/set command "))
            setCommand(removePrefix(text), chatId);

        else if (text.startsWith("/remove command "))
            removeCommand(removePrefix(text), chatId);

        else if (text.startsWith("/remove commands all"))
            removeAllCommands(chatId);
    }

    private void setPremium(String userNick, String chatId) {
        if (userService.updateUserToPremiumByNick(userNick))
            QuickSender.message(chatId, String.format("User %s successfully updated to Premium.", userNick), false);
        else
            QuickSender.message(chatId, String.format("User %s does not exist.", userNick), false);
    }

    private void setStandard(String userNick, String chatId) {
        if (userService.updateUserToStandardByNick(userNick))
            QuickSender.message(chatId, String.format("User %s successfully updated to Standard.", userNick), false);
        else
            QuickSender.message(chatId, String.format("User %s does not exist.", userNick), false);
    }

    private void setAdmin(String userNick, String chatId) {
        if (userService.updateUserToAdminByNick(userNick))
            QuickSender.message(chatId, String.format("User %s successfully updated to Admin.", userNick), false);
        else
            QuickSender.message(chatId, String.format("User %s does not exist.", userNick), false);
    }

    private void setNonAdmin(String userNick, String chatId) {
        if (userService.updateUserToNonAdminByNick(userNick))
            QuickSender.message(chatId, String.format("User %s successfully updated to Non-Admin.", userNick), false);
        else
            QuickSender.message(chatId, String.format("User %s does not exist.", userNick), false);
    }


    private void setCommand(String text, String chatId) {
        String[] commandAndDescription = text.split(":");

        if (commandAndDescription[0] == null || commandAndDescription[0].isBlank()) {
            QuickSender.message(chatId, "Command not found.", false);
            return;
        }

        String command = commandAndDescription[0];
        String description = commandAndDescription.length > 1 ? commandAndDescription[1] : command;
        BotCommand botCommand = BotCommand.builder()
                .command(command)
                .description(description)
                .build();

        List<BotCommand> commandList = requestService.sendRequestList(new GetMyCommands());
        commandList.add(botCommand);

        SetMyCommands setMyCommands = new SetMyCommands(commandList);
        try {
            requestService.send(setMyCommands);
            QuickSender.message(chatId, "Successfully add " + command + " command.", false);
        } catch (Exception ex) {
            QuickSender.message(chatId, "Something goes wrong, check your command and try again.", false);
        }
    }

    private void removeCommand(String text, String chatId) {
        if (text.isBlank()) {
            QuickSender.message(chatId, "Command not found.", false);
            return;
        }

        String command = text.startsWith("/") ? text.substring(1) : text;
        List<BotCommand> commandList = requestService.sendRequestList(new GetMyCommands());

        for (BotCommand com : commandList) {
            if (com.getCommand().equals(command)) {
                commandList.remove(com);
                SetMyCommands setMyCommands = new SetMyCommands(commandList);
                try {
                    requestService.send(setMyCommands);
                    QuickSender.message(chatId, "Successfully remove " + command + " command.", false);
                } catch (Exception ex) {
                    QuickSender.message(chatId, "Something goes wrong, check your command and try again.", false);
                }
                return;
            }
        }

        QuickSender.message(chatId, "Command not found.", false);
    }

    private void removeAllCommands(String chatId) {
        requestService.send(new DeleteMyCommands());
        QuickSender.message(chatId, "Successfully removed all commands.", false);
    }


    private String removePrefix(String text) {
        String[] textArray = text.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < textArray.length; i++) {
            if (i > 1)
                sb.append(textArray[i]).append(" ");
        }
        return sb.toString().trim();
    }
}

