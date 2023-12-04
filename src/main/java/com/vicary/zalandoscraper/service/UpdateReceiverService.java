package com.vicary.zalandoscraper.service;

import com.microsoft.playwright.PlaywrightException;
import com.vicary.zalandoscraper.entity.WaitingUserEntity;
import com.vicary.zalandoscraper.exception.*;
import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.scraper.Scraper;
import com.vicary.zalandoscraper.scraper.ScraperFactory;
import com.vicary.zalandoscraper.service.repository_services.WaitingUserService;
import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import com.vicary.zalandoscraper.api_telegram.service.UpdateFetcher;
import com.vicary.zalandoscraper.api_telegram.service.UpdateReceiver;
import com.vicary.zalandoscraper.configuration.BotInfo;
import com.vicary.zalandoscraper.pattern.Pattern;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.service.response.*;
import com.vicary.zalandoscraper.service.response.inline_markup.InlineMarkupResponse;
import com.vicary.zalandoscraper.updater.AutoUpdater;
import com.vicary.zalandoscraper.utils.url.UrlParser;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import com.vicary.zalandoscraper.api_telegram.api_object.Update;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UpdateReceiverService implements UpdateReceiver {

    // TODO change waitingUser database in original database

    private final static Logger logger = LoggerFactory.getLogger(UpdateReceiverService.class);

    private final UserAuthentication userAuthentication;

    private final ResponseFacade facade;

    private final WaitingUserService waitingUserService;

    private final QuickSender quickSender;

    private final UrlParser urlParser;
    private final UpdateFetcher updateFetcher = new UpdateFetcher(this);

    @PostConstruct
    void setup() {
        facade.deleteAllActiveRequests();
    }

    @Override
    public void receive(Update update) {
        if (update.getMessage() == null && update.getCallbackQuery() == null) {
            logger.info("Got update without message.");
            return;
        }

        ActiveUser user;
        try {
            user = userAuthentication.authenticate(update);
        } catch (ActiveUserException ignored) {
            return;
        }

        String userId = user.getUserId();
        String chatId = user.getChatId();
        logger.info("Got message from user '{}'", userId);

        try {
            if (isUpdaterRunning())
                handleProductUpdaterRunning(userId);

            Responser responser = null;

            if (Pattern.isAdminCommand(user.getText(), user.isAdmin())) {
                responser = new AdminResponse(facade, user);
                responser.response();
            }

            if (user.isAwaitedMessage())
                responser = new AwaitedMessageResponse(facade, user);

            else if (Pattern.isInlineMarkup(update))
                responser = new InlineMarkupResponse(facade, user);

            else if (Pattern.isCommand(user.getText()))
                responser = new CommandResponse(facade, user);

            else if (Pattern.isEmailToken(user.getText()))
                responser = new EmailVerificationResponse(facade, user);

            else if (Pattern.isURL(user.getText())) {
                user.setText(urlParser.parse(user.getText()));
                Scraper scraper = ScraperFactory.getScraperFromLink(user.getText()).orElseThrow(() -> new UrlParserException("Cannot find Scraper for URL: " + user.getText()));
                responser = new LinkResponse(facade, user, scraper);
            }

            if (responser != null)
                responser.response();

        } catch (IllegalArgumentException | UrlParserException ex) {
            logger.warn(ex.getMessage());
        } catch (InvalidLinkException | IllegalInputException ex) {
            logger.warn(ex.getLoggerMessage());
            quickSender.message(chatId, ex.getMessage(), false);
        } catch (WebDriverException | PlaywrightException ex) {
            logger.error("Web Driver exception: " + ex.getMessage());
            quickSender.message(chatId, Messages.other("somethingGoesWrong"), false);
        } catch (ZalandoScraperBotException ex) {
            logger.error(ex.getLoggerMessage());
            quickSender.message(chatId, ex.getMessage(), false);
        } catch (Exception ex) {
            logger.error("Unexpected exception: " + ex.getMessage());
            quickSender.message(chatId, Messages.other("somethingGoesWrong"), false);
            ex.printStackTrace();
        } finally {
            facade.deleteActiveRequestById(userId);
            ActiveUser.remove();
            ActiveLanguage.remove();
        }
    }

    private boolean isUpdaterRunning() {
        return AutoUpdater.isActive();
    }

    private void handleProductUpdaterRunning(String userId) {
        String message = Messages.other("updatingProducts");
        quickSender.message(userId, message, true);
        checkAndSaveWaitingUser(userId);
        throw new IllegalArgumentException("User '%s' interact with bot while product updater is running.".formatted(userId));
    }

    private void checkAndSaveWaitingUser(String userId) {
        if (!waitingUserService.existsByUserId(facade.getUserByUserId(userId)))
            waitingUserService.saveWaitingUser(new WaitingUserEntity(facade.getUserByUserId(userId)));
    }

    @Override
    public String botToken() {
        return BotInfo.getBotToken();
    }
}