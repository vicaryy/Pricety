package com.vicary.zalandoscraper.service.response.admin;

import com.vicary.zalandoscraper.api_telegram.api_object.bot.bot_command.BotCommand;
import com.vicary.zalandoscraper.api_telegram.api_request.commands.DeleteMyCommands;
import com.vicary.zalandoscraper.api_telegram.api_request.commands.GetMyCommands;
import com.vicary.zalandoscraper.api_telegram.api_request.commands.SetMyCommands;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.api_telegram.service.RequestService;
import com.vicary.zalandoscraper.api_telegram.service.UpdateFetcher;
import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.exception.ScraperBotException;
import com.vicary.zalandoscraper.format.MarkdownV2;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.response.ResponseFacade;
import com.vicary.zalandoscraper.service.response.Responser;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import com.vicary.zalandoscraper.updater.AutoUpdater;
import com.vicary.zalandoscraper.utils.ApplicationCrasher;
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
    private UpdateFetcher updateFetcher;

    private final RequestService requestService;

    private final ResponseFacade responseFacade;

    private final QuickSender quickSender;

    private final AutoUpdater autoUpdater;

    private final ApplicationCrasher applicationCrasher;

    public void set(ActiveUser activeUser, UpdateFetcher updateFetcher) {
        this.user = activeUser;
        this.updateFetcher = updateFetcher;
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

        else if (user.getText().startsWith("//set nick "))
            setNick();

        else if (user.getText().startsWith("//delete command "))
            deleteCommand();

        else if (user.getText().startsWith("//delete user "))
            deleteUser();

        else if (user.getText().startsWith("//delete product "))
            deleteProduct();

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

        else if (user.getText().startsWith("//get user "))
            getUser();

        else if (user.getText().startsWith("//get product "))
            getProduct();

        else if (user.getText().startsWith("//get productUser"))
            getProductUser();

        else if (user.getText().startsWith("//send message"))
            sendMessage();

        else if (user.getText().equals("//start"))
            start();

        else if (user.getText().equals("//stop"))
            stop();

        else if (user.getText().equals("//crash"))
            crash();
    }

    private void setNick() {
        String[] textWithoutPrefix = removePrefix(user.getText()).split(" ");
        String userId = "";
        String nick = "";
        for (int i = 0; i < textWithoutPrefix.length; i++) {
            if (i == 0)
                userId = textWithoutPrefix[0];
            if (i == 1) {
                nick = textWithoutPrefix[1].toLowerCase();
                break;
            }
        }
        if (userId.isBlank() || nick.isBlank())
            throw new IllegalInputException("UserId and nick cannot be empty", "Admin tries to set nick but userId or nick are empty");

        responseFacade.updateUserNick(Long.parseLong(userId), nick);
        quickSender.message(user.getChatId(), "UserId: %s nick updated to %s successfully.".formatted(userId, nick), false);
    }

    private void setPremium() {
        String userId = removePrefix(user.getText());
        if (responseFacade.updateUserToPremiumByUserId(Long.parseLong(userId)))
            quickSender.message(user.getChatId(), String.format("User %s successfully updated to Premium.", userId), false);
        else
            throw new IllegalInputException(
                    String.format("User %s does not exist.", userId),
                    String.format("Admin typed invalid userId %s.", userId));
    }

    private void setStandard() {
        String userId = removePrefix(user.getText());
        if (responseFacade.updateUserToStandardByUserId(Long.parseLong(userId)))
            quickSender.message(user.getChatId(), String.format("User %s successfully updated to Standard.", userId), false);
        else
            throw new IllegalInputException(
                    String.format("User %s does not exist.", userId),
                    String.format("Admin typed invalid userId %s.", userId));
    }

    private void setAdmin() {
        String userId = removePrefix(user.getText());
        if (responseFacade.updateUserToAdminByUserId(Long.parseLong(userId)))
            quickSender.message(user.getChatId(), String.format("User %s successfully updated to Admin.", userId), false);
        else
            throw new IllegalInputException(
                    String.format("User %s does not exist.", userId),
                    String.format("Admin typed invalid userId %s.", userId));
    }

    void setNonAdmin() {
        String userId = removePrefix(user.getText());
        if (responseFacade.updateUserToNonAdminByUserId(Long.parseLong(userId)))
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

    private void deleteUser() {
        String userId = removePrefix(user.getText());
        if (userId.isBlank())
            throw new IllegalInputException("UserId cannot be empty.", "Admin tries to delete product but userId is empty");

        if (!responseFacade.isUserExists(Long.parseLong(userId)))
            throw new IllegalInputException("User not found.", "Admin tries to delete user but user not found, userId: " + userId);

        responseFacade.deleteUser(Long.parseLong(userId));
        quickSender.message(user.getChatId(), "User deleted.", false);
    }

    private void deleteProduct() {
        String productId = removePrefix(user.getText());
        if (productId.isBlank())
            throw new IllegalInputException("ProductId cannot be empty.", "Admin tries to delete user but userId is empty");

        if (!responseFacade.isProductExists(productId))
            throw new IllegalInputException("Product not found.", "Admin tries to delete product but product not found, productId: " + productId);

        responseFacade.deleteProduct(Long.parseLong(productId));
        quickSender.message(user.getChatId(), "Product deleted.", false);
    }

    private void updateStart() {
        try {
            autoUpdater.start();
            quickSender.message(user.getChatId(), "Auto Updater started successfully.", false);
        } catch (ScraperBotException ex) {
            quickSender.message(user.getChatId(), ex.getMessage(), false);
            logger.info(ex.getLoggerMessage());
        }
    }

    private void updateStartOnce() {
        try {
            autoUpdater.startOnce();
            quickSender.message(user.getChatId(), "Auto Updater Once started successfully.", false);
        } catch (ScraperBotException ex) {
            quickSender.message(user.getChatId(), ex.getMessage(), false);
            logger.info(ex.getLoggerMessage());
        }
    }

    private void updateStop() {
        try {
            autoUpdater.stop();
            quickSender.message(user.getChatId(), "Auto Updater stopped.", false);
        } catch (ScraperBotException ex) {
            quickSender.message(user.getChatId(), ex.getMessage(), false);
            logger.info(ex.getLoggerMessage());
        }
    }

    private void updateGetState() {
        quickSender.message(user.getChatId(), "Current state: " + autoUpdater.getCurrentState(), false);
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

        final String text = sb.toString().trim();
        boolean multiLanguage = text.contains("-en-");

        String[] textArray = text.split("-en-");
        String pl = MarkdownV2.applyWithManualBoldAndItalic(textArray[0].trim());
        String en = multiLanguage ? MarkdownV2.applyWithManualBoldAndItalic(textArray[1].trim()) : "";

        if (to.isBlank() || text.isBlank())
            throw new IllegalInputException("User and text cannot be empty", "Admin tries to send message but user or text are empty");

        if (to.equals("all"))
            sendMessageToAll(multiLanguage, pl, en);
        else
            quickSender.message(responseFacade.getUser(Long.parseLong(to)).getTelegramId(), MarkdownV2.applyWithManualBoldAndItalic(text).trim(), true);
    }

    private void sendMessageToAll(boolean multiLanguage, String pl, String en) {
        List<UserEntity> users = responseFacade.getAllUsers();
        if (multiLanguage) {
            users.forEach(u -> {
                if (u.getNationality().equals("pl"))
                    quickSender.message(u.getTelegramId(), pl, true);
                else
                    quickSender.message(u.getTelegramId(), en, true);
            });
        } else
            users.forEach(user -> quickSender.message(user.getTelegramId(), pl, true));
    }

    private void getUser() {
        String userId = removePrefix(this.user.getText());
        if (userId.isBlank())
            throw new IllegalInputException("User cannot be empty", "Admin tries to get user but user is empty");

        if (userId.equals("all"))
            displayUsers(responseFacade.getAllUsers());
        else
            displayUsers(List.of(responseFacade.getUser(Long.parseLong(userId))));
    }

    private void getProduct() {
        String product = removePrefix(this.user.getText());
        if (product.isBlank())
            throw new IllegalInputException("Product cannot be empty", "Admin tries to get product but product is empty");

        if (product.equals("all")) {
            displayProducts(responseFacade.getAllProducts());
            return;
        }
        if (!responseFacade.isProductExists(product))
            throw new IllegalInputException("Product does not exists.", "Admin tries to display product but product '%s' does not exists".formatted(product));

        displayProducts(List.of(responseFacade.getProductById(Long.parseLong(product))));
    }

    private void getProductUser() {
        String userId = removePrefix(user.getText());
        if (userId.isBlank())
            throw new IllegalInputException("UserId cannot be empty", "Admin tries to get products but userId is empty");

        if (!responseFacade.isUserExists(Long.parseLong(userId)))
            throw new IllegalInputException("User does not exists.", "Admin tries to display productUser but user '%s' does not exists".formatted(userId));

        displayProducts(responseFacade.getAllProductsByUserId(Long.parseLong(userId)));
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
                //set nick userId nick
                               
                *Delete:*
                //delete command command
                //delete command all
                //delete user userId
                //delete product productId
                                
                *Ban:*
                //ban userId  -  not yet
                                
                *Update:*
                //update start
                //update start once
                //update stop
                //update state
                                
                *Send:*
                //send message all text
                //send message all PL -en- ENG
                //send message userId text
                                
                *Get:*
                //get user all
                //get user userId
                //get product all
                //get product productId
                //get productUser userId
                //get all - getting all commands
                                
                *Application:*
                //start
                //stop
                //crash
                """);
        quickSender.message(user.getChatId(), commands, true);
    }

    private void start() {
        if (updateFetcher.isReceiverRunning()) {
            quickSender.message(user.getChatId(), "Receiver is already running.", false);
            return;
        }
        updateFetcher.setReceiverRunning(true);
        quickSender.message(user.getChatId(), "Receiver started.", false);
        logger.info("Receiver started");
    }

    private void stop() {
        if (!updateFetcher.isReceiverRunning()) {
            quickSender.message(user.getChatId(), "Receiver is already stopped.", false);
            return;
        }
        updateFetcher.setReceiverRunning(false);
        quickSender.message(user.getChatId(), "Receiver stopped.", false);
        logger.info("Receiver stopped");
    }

    private void crash() {
        quickSender.message(user.getChatId(), "Crashing application...", false);
        logger.error("Crashing application...");
        applicationCrasher.crash();
    }


    private void displayUsers(List<UserEntity> users) {
        for (int i = 0; i < users.size(); i++) {
            UserEntity u = users.get(i);
            String user = ("""
                    %s
                        id: %s
                        nick: %s
                        email: %s
                        nationality: %s
                        premium: %s
                        admin: %s
                        notifyByEmail: %s
                        verifiedEmail: %s"""
                    .formatted(
                            MarkdownV2.applyWithManualBoldAndItalic("*User nr. " + (i + 1) + "*"),
                            u.getTelegramId(),
                            MarkdownV2.apply(u.getNick() == null ? "-" : u.getNick()).get(),
                            MarkdownV2.apply(u.getEmail() == null ? "-" : u.getEmail()).get(),
                            u.getNationality(),
                            Boolean.toString(u.isPremium()),
                            Boolean.toString(u.isAdmin()),
                            Boolean.toString(u.isEmailNotifications()),
                            Boolean.toString(u.isVerifiedEmail())
                    ));
            quickSender.message(this.user.getChatId(), user, true);
        }
    }

    private void displayProducts(List<Product> products) {
        if (products.isEmpty()) {
            quickSender.message(user.getChatId(), "No products to display.", true);
            return;
        }
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            String product = ("""
                    %s
                        productId: %s
                        userId: %s
                        name: %s
                        description: %s
                        variant: %s
                        price: %s
                        priceAlert: %s
                        currency: %s
                        link: %s"""
                    .formatted(
                            MarkdownV2.applyWithManualBoldAndItalic("*Product nr. " + (i + 1) + "*"),
                            p.getProductId(),
                            p.getUserDTO().getUserId(),
                            MarkdownV2.apply(p.getName()).get(),
                            MarkdownV2.apply(p.getDescription()).get(),
                            MarkdownV2.apply(p.getVariant()).get(),
                            MarkdownV2.apply(String.format("%.2f", p.getPrice())).get(),
                            MarkdownV2.apply(p.getPriceAlert()).get(),
                            MarkdownV2.apply(p.getCurrency()).get(),
                            MarkdownV2.apply(p.getLink()).toURL(p.getServiceName()).get()
                    ));
            quickSender.message(user.getChatId(), product, true);
        }
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

