package com.vicary.zalandoscraper.service;

import com.microsoft.playwright.PlaywrightException;
import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.api_telegram.thread.UpdateFetcher;
import com.vicary.zalandoscraper.api_telegram.thread.UpdateReceiver;
import com.vicary.zalandoscraper.configuration.ApiBotConfiguration;
import com.vicary.zalandoscraper.configuration.BotInfo;
import com.vicary.zalandoscraper.exception.ActiveUserException;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.exception.ZalandoScraperBotException;
import com.vicary.zalandoscraper.pattern.Pattern;
import com.vicary.zalandoscraper.service.entity.ActiveRequestService;
import com.vicary.zalandoscraper.api_telegram.QuickSender;
import com.vicary.zalandoscraper.service.response.*;
import com.vicary.zalandoscraper.updater.ProductUpdater;
import lombok.RequiredArgsConstructor;

import com.vicary.zalandoscraper.api_telegram.api_object.Update;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UpdateReceiverService implements UpdateReceiver {

    private final static Logger logger = LoggerFactory.getLogger(UpdateReceiverService.class);

    private final CommandResponse commandResponse;

    private final ReplyMarkupResponse replyMarkupResponse;

    private final LinkResponse linkResponse;

    private final UserAuthentication userAuthentication;

    private final ActiveRequestService activeRequestService;

    private final AwaitedMessageResponse awaitedMessageResponse;

    private final EmailVerificationResponse emailVerificationResponse;
    private final ProductUpdater productUpdater = ProductUpdater.getInstance();

    private final UpdateFetcher updateFetcher = UpdateFetcher.getInstance(this);

    @Override
    public void receive(Update update) {
        if (update.getMessage() == null && update.getCallbackQuery() == null) {
            logger.info("Got update without message.");
            return;
        }

        try {
            userAuthentication.authenticate(update);
        } catch (ActiveUserException ignored) {
            return;
        }

        String text = ActiveUser.get().getText();
        String userId = ActiveUser.get().getUserId();
        String chatId = ActiveUser.get().getChatId();
        logger.info("Got message from user '{}'", userId);

        if (productUpdater.isRunning()) {
            activeRequestService.deleteByUserId(userId);
            QuickSender.message(userId, "Wait until updates.", false);
            return;
        }


        try {
            if (Pattern.isAwaitedMessage(ActiveUser.get().isAwaitedMessage()))
                awaitedMessageResponse.response();

            else if (Pattern.isReplyMarkup(update))
                replyMarkupResponse.response(text);

            else if (Pattern.isZalandoURL(text))
                linkResponse.response(text);

            else if (Pattern.isCommand(text))
                commandResponse.response(text, chatId);

            else if (Pattern.isEmailToken(text))
                emailVerificationResponse.response(text);

        } catch (IllegalArgumentException ex) {
            logger.warn(ex.getMessage());
        } catch (InvalidLinkException | IllegalInputException ex) {
            logger.warn(ex.getLoggerMessage());
            QuickSender.message(chatId, ex.getMessage(), false);
        } catch (WebDriverException | PlaywrightException ex) {
            logger.error("Web Driver exception: " + ex.getMessage());
            QuickSender.message(chatId, "Sorry but something goes wrong.", false);
        } catch (ZalandoScraperBotException ex) {
            logger.error(ex.getLoggerMessage());
            QuickSender.message(chatId, ex.getMessage(), false);
        } catch (Exception ex) {
            logger.error("Unexpected exception: " + ex.getMessage());
            QuickSender.message(chatId, "Sorry but something goes wrong.", false);
            ex.printStackTrace();
        } finally {
            activeRequestService.deleteByUserId(userId);
            ActiveUser.remove();
        }
    }

    @Override
    public String botToken() {
        return BotInfo.getBotToken();
    }
}










