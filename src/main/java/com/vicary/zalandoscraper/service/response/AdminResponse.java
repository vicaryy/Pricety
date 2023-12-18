package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.api_telegram.api_object.bot.bot_command.BotCommand;
import com.vicary.zalandoscraper.api_telegram.api_request.commands.DeleteMyCommands;
import com.vicary.zalandoscraper.api_telegram.api_request.commands.GetMyCommands;
import com.vicary.zalandoscraper.api_telegram.api_request.commands.SetMyCommands;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.api_telegram.service.RequestService;
import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.exception.ZalandoScraperBotException;
import com.vicary.zalandoscraper.format.MarkdownV2;
import com.vicary.zalandoscraper.service.UpdateReceiverService;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import com.vicary.zalandoscraper.updater.AutoUpdater;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminResponse implements Responser {
    private final static Logger logger = LoggerFactory.getLogger(AdminResponse.class);
    private ActiveUser user;

    private final RequestService requestService;

    private final ResponseFacade responseFacade;

    private final QuickSender quickSender;

    private final AutoUpdater autoUpdater;

    private final UpdateReceiverService updateReceiverService;

    public void setActiveUser(ActiveUser activeUser) {
        this.user = activeUser;
    }

    public void response() {
        if (user.getText().startsWith("//set premium "))
            setPremium();

        else if (user.getText().startsWith("//set standard "))
            setStandard();

        else if (user.getText().startsWith("//set admin "))
            setAdmin();

        else if (user.getText().startsWith("//set non-admin "))
            setNonAdmin();

        else if (user.getText().startsWith("//set command "))
            setCommand();

        else if (user.getText().startsWith("//delete command "))
            deleteCommand();

        else if (user.getText().equals("//update start"))
            updateStart();

        else if (user.getText().equals("//update start once"))
            updateStartOnce();

        else if (user.getText().equals("//update stop"))
            updateStop();

        else if (user.getText().equals("//update state"))
            updateGetState();

        else if (user.getText().equals("//get all"))
            getAllCommands();

        else if (user.getText().startsWith("//get user"))
            getUser();

        else if (user.getText().startsWith("//send message"))
            sendMessage();

//        else if (user.getText().equals("//start"))
//            start();
//
//        else if (user.getText().equals("//stop"))
//            stop();
//
//        else if (user.getText().equals("//crash"))
//            crash();
    }

//    private void start() {
//        updateReceiverService.running(true);
//        quickSender.message(user.getChatId(), "Receiver started.", false);
//        logger.info("Receiver started");
//    }
//
//    private void stop() {
//        updateReceiverService.running(false);
//        quickSender.message(user.getChatId(), "Receiver stopped.", false);
//        logger.info("Receiver stopped");
//    }
//
//    private void crash() {
//        quickSender.message(user.getChatId(), "Crashing application...", false);
//        updateReceiverService.crash();
//    }

    private void getUser() {
        String userId = removePrefix(this.user.getText());
        if (userId.isBlank())
            throw new IllegalInputException("User cannot be empty", "Admin tries to get user but user is empty");

        if (userId.equals("all"))
            displayUsers(responseFacade.getAllUsers());
        else
            displayUsers(List.of(responseFacade.getUserByUserId(userId)));
    }

    private void displayUsers(List<UserEntity> users) {
        for (int i = 0; i < users.size(); i++) {
            UserEntity u = users.get(i);
            String user = MarkdownV2.applyWithManualBoldAndItalic("""
                    *User nr. %d*
                        id: %s
                        nick: %s
                        email: %s
                        nationality: %s
                        premium: %s
                        admin: %s
                        notifyByEmail: %s
                        verifiedEmail: %s"""
                    .formatted(
                            i + 1,
                            u.getUserId(),
                            u.getNick() == null ? "-" : u.getNick(),
                            u.getEmail() == null ? "-" : u.getEmail(),
                            u.getNationality(),
                            Boolean.toString(u.isPremium()),
                            Boolean.toString(u.isAdmin()),
                            Boolean.toString(u.isNotifyByEmail()),
                            Boolean.toString(u.isVerifiedEmail())
                    ));
            quickSender.message(this.user.getChatId(), user, true);
        }
    }

    private void sendMessage() {
        String[] textWithoutPrefix = removePrefix(user.getText()).split(" ");
        String to = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < textWithoutPrefix.length; i++) {
            if (i == 0) {
                to = textWithoutPrefix[i];
                continue;
            }
            sb.append(textWithoutPrefix[i]).append(" ");
        }
        final String text = MarkdownV2.applyWithManualBoldAndItalic(sb.toString().trim());

        if (to.isBlank() || text.isBlank())
            throw new IllegalInputException("User and text cannot be empty", "Admin tries to send message but user or text are empty");

        if (to.equals("all"))
            responseFacade.getAllUsers().forEach(user -> quickSender.message(user.getUserId(), text, true));
        else
            quickSender.message(responseFacade.getUserByUserId(to).getUserId(), text, true);
    }

    private void getAllCommands() {
        String commands = MarkdownV2.applyWithManualBoldAndItalic("""
                List of commands:
                                
                *Set:*
                //set premium userId
                //set standard userId
                //set admin userId
                //set non-admin userId
                //set command command:description
                               
                *Remove:*
                //delete command command
                //delete command all
                                
                *Update:*
                //update start
                //update start once
                //update stop
                //update state
                                
                *Send:*
                //send message all text
                //send message userId text
                                
                *Get:*
                //get user all
                //get user userId
                //get all - getting all commands
                
                *Application:*  -  not working yet
                //start
                //stop
                //crash
                """);
        quickSender.message(user.getChatId(), commands, true);
    }

    private void updateStart() {
        try {
            autoUpdater.start();
            quickSender.message(user.getChatId(), "Auto Updater started successfully.", false);
        } catch (ZalandoScraperBotException ex) {
            quickSender.message(user.getChatId(), ex.getMessage(), false);
            logger.info(ex.getLoggerMessage());
        }
    }

    private void updateStartOnce() {
        try {
            autoUpdater.startOnce();
            quickSender.message(user.getChatId(), "Auto Updater Once started successfully.", false);
        } catch (ZalandoScraperBotException ex) {
            quickSender.message(user.getChatId(), ex.getMessage(), false);
            logger.info(ex.getLoggerMessage());
        }
    }

    private void updateStop() {
        try {
            autoUpdater.stop();
            quickSender.message(user.getChatId(), "Auto Updater stopped.", false);
        } catch (ZalandoScraperBotException ex) {
            quickSender.message(user.getChatId(), ex.getMessage(), false);
            logger.info(ex.getLoggerMessage());
        }
    }

    private void updateGetState() {
        quickSender.message(user.getChatId(), "Current state: " + autoUpdater.getCurrentState(), false);
    }

    private void setPremium() {
        String userId = removePrefix(user.getText());
        if (responseFacade.updateUserToPremiumByUserId(userId))
            quickSender.message(user.getChatId(), String.format("User %s successfully updated to Premium.", userId), false);
        else
            throw new IllegalInputException(
                    String.format("User %s does not exist.", userId),
                    String.format("Admin typed invalid userId %s.", userId));
    }

    private void setStandard() {
        String userId = removePrefix(user.getText());
        if (responseFacade.updateUserToStandardByUserId(userId))
            quickSender.message(user.getChatId(), String.format("User %s successfully updated to Standard.", userId), false);
        else
            throw new IllegalInputException(
                    String.format("User %s does not exist.", userId),
                    String.format("Admin typed invalid userId %s.", userId));
    }

    private void setAdmin() {
        String userId = removePrefix(user.getText());
        if (responseFacade.updateUserToAdminByUserId(userId))
            quickSender.message(user.getChatId(), String.format("User %s successfully updated to Admin.", userId), false);
        else
            throw new IllegalInputException(
                    String.format("User %s does not exist.", userId),
                    String.format("Admin typed invalid userId %s.", userId));
    }

    void setNonAdmin() {
        String userId = removePrefix(user.getText());
        if (responseFacade.updateUserToNonAdminByUserId(userId))
            quickSender.message(user.getChatId(), String.format("User %s successfully updated to Non-Admin.", userId), false);
        else
            throw new IllegalInputException(
                    String.format("User %s does not exist.", userId),
                    String.format("Admin typed invalid userId %s.", userId));
    }


    private void setCommand() {
        String textWithoutPrefix = removePrefix(user.getText());
        String[] commandAndDescription = textWithoutPrefix.split(":");

        if (commandAndDescription.length < 2 || commandAndDescription[0].isBlank() || commandAndDescription[1].isBlank())
            throw new IllegalInputException(
                    "Invalid command, make sure you are using this pattern: command:description",
                    "Admin typed invalid command.");


        String command = commandAndDescription[0];
        String description = commandAndDescription[1];
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
            throw new IllegalInputException(
                    "Something goes wrong, check your command and try again.",
                    "Fail in setting a command - " + command);
        }
    }

    private void deleteCommand() {
        String textWithoutPrefix = removePrefix(user.getText());
        if (textWithoutPrefix.isBlank())
            throw new IllegalInputException(
                    "I don't see command.",
                    "Admin don't typed command to remove.");

        if (textWithoutPrefix.equals("all")) {
            requestService.send(new DeleteMyCommands());
            quickSender.message(user.getChatId(), "Successfully removed all commands.", false);
            return;
        }

        String command = textWithoutPrefix.startsWith("/") ? textWithoutPrefix.substring(1) : textWithoutPrefix;
        List<BotCommand> commandList = requestService.sendRequestList(new GetMyCommands());

        for (BotCommand com : commandList) {
            if (com.getCommand().equals(command)) {
                commandList.remove(com);
                SetMyCommands setMyCommands = new SetMyCommands(commandList);
                try {
                    requestService.send(setMyCommands);
                    quickSender.message(user.getChatId(), "Successfully removed " + command + " command.", false);
                } catch (Exception ex) {
                    throw new IllegalInputException(
                            "Something goes wrong.",
                            "Fail to send remove command.");
                }
                return;
            }
        }

        throw new IllegalInputException(
                "Command does not exist.",
                "Admin don't typed command to remove.");
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

