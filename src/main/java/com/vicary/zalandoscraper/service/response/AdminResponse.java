package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.api_telegram.api_object.bot.bot_command.BotCommand;
import com.vicary.zalandoscraper.api_telegram.api_request.commands.DeleteMyCommands;
import com.vicary.zalandoscraper.api_telegram.api_request.commands.GetMyCommands;
import com.vicary.zalandoscraper.api_telegram.api_request.commands.SetMyCommands;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.api_telegram.service.RequestService;
import com.vicary.zalandoscraper.thread_local.ActiveUser;

import java.util.List;

public class AdminResponse implements Responser {
    private final RequestService requestService = new RequestService();
    private final ResponseFacade responseFacade;
    private final ActiveUser user;
    private final QuickSender quickSender;

    public AdminResponse(ResponseFacade responseFacade, ActiveUser activeUser) {
        this.responseFacade = responseFacade;
        this.user = activeUser;
        this.quickSender = new QuickSender();
    }

    public AdminResponse(ResponseFacade responseFacade, ActiveUser activeUser, QuickSender quickSender) {
        this.responseFacade = responseFacade;
        this.user = activeUser;
        this.quickSender = quickSender;
    }

    public void response() {
        if (user.getText().startsWith("/set premium "))
            setPremium();

        else if (user.getText().startsWith("/set standard "))
            setStandard();

        else if (user.getText().startsWith("/set admin "))
            setAdmin();

        else if (user.getText().startsWith("/set non-admin "))
            setNonAdmin();

        else if (user.getText().startsWith("/set command "))
            setCommand();

        else if (user.getText().startsWith("/remove command "))
            removeCommand();

        else if (user.getText().startsWith("/remove commands all"))
            removeAllCommands();
    }

    private void setPremium() {
        String userNick = removePrefix(user.getText());
        if (responseFacade.updateUserToPremiumByNick(userNick))
            quickSender.message(user.getChatId(), String.format("User %s successfully updated to Premium.", userNick), false);
        else
            quickSender.message(user.getChatId(), String.format("User %s does not exist.", userNick), false);
    }

    private void setStandard() {
        String userNick = removePrefix(user.getText());
        if (responseFacade.updateUserToStandardByNick(userNick))
            quickSender.message(user.getChatId(), String.format("User %s successfully updated to Standard.", userNick), false);
        else
            quickSender.message(user.getChatId(), String.format("User %s does not exist.", userNick), false);
    }

    private void setAdmin() {
        String userNick = removePrefix(user.getText());
        if (responseFacade.updateUserToAdminByNick(userNick))
            quickSender.message(user.getChatId(), String.format("User %s successfully updated to Admin.", userNick), false);
        else
            quickSender.message(user.getChatId(), String.format("User %s does not exist.", userNick), false);
    }

    private void setNonAdmin() {
        String userNick = removePrefix(user.getText());
        if (responseFacade.updateUserToNonAdminByNick(userNick))
            quickSender.message(user.getChatId(), String.format("User %s successfully updated to Non-Admin.", userNick), false);
        else
            quickSender.message(user.getChatId(), String.format("User %s does not exist.", userNick), false);
    }


    private void setCommand() {
        String[] commandAndDescription = user.getText().split(":");

        if (commandAndDescription[0] == null || commandAndDescription[0].isBlank()) {
            quickSender.message(user.getChatId(), "Command not found.", false);
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
            quickSender.message(user.getChatId(), "Successfully add " + command + " command.", false);
        } catch (Exception ex) {
            quickSender.message(user.getChatId(), "Something goes wrong, check your command and try again.", false);
        }
    }

    private void removeCommand() {
        if (user.getText().isBlank()) {
            quickSender.message(user.getChatId(), "Command not found.", false);
            return;
        }

        String command = user.getText().startsWith("/") ? user.getText().substring(1) : user.getText();
        List<BotCommand> commandList = requestService.sendRequestList(new GetMyCommands());

        for (BotCommand com : commandList) {
            if (com.getCommand().equals(command)) {
                commandList.remove(com);
                SetMyCommands setMyCommands = new SetMyCommands(commandList);
                try {
                    requestService.send(setMyCommands);
                    quickSender.message(user.getChatId(), "Successfully remove " + command + " command.", false);
                } catch (Exception ex) {
                    quickSender.message(user.getChatId(), "Something goes wrong, check your command and try again.", false);
                }
                return;
            }
        }

        quickSender.message(user.getChatId(), "Command not found.", false);
    }

    private void removeAllCommands() {
        requestService.send(new DeleteMyCommands());
        quickSender.message(user.getChatId(), "Successfully removed all commands.", false);
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

