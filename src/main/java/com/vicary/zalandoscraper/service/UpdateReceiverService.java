package com.vicary.zalandoscraper.service;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.api_request.send.SendMessage;
import com.vicary.zalandoscraper.exception.ActiveUserException;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.exception.ZalandoScraperBotException;
import com.vicary.zalandoscraper.pattern.Pattern;
import com.vicary.zalandoscraper.service.entity.ActiveRequestService;
import com.vicary.zalandoscraper.service.entity.AwaitedMessageService;
import com.vicary.zalandoscraper.service.entity.ProductService;
import com.vicary.zalandoscraper.service.entity.UserService;
import com.vicary.zalandoscraper.service.quick_sender.QuickSender;
import com.vicary.zalandoscraper.service.response.AwaitedMessageResponse;
import com.vicary.zalandoscraper.service.response.CommandResponse;
import com.vicary.zalandoscraper.service.response.LinkResponse;
import com.vicary.zalandoscraper.service.response.ReplyMarkupResponse;
import lombok.RequiredArgsConstructor;

import com.vicary.zalandoscraper.api_object.Update;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UpdateReceiverService {

    private final static Logger logger = LoggerFactory.getLogger(UpdateReceiverService.class);

    private final QuickSender quickSender;

    private final RequestService requestService;

    private final CommandResponse commandResponse;

    private final ReplyMarkupResponse replyMarkupResponse;

    private final LinkResponse linkResponse;

    private final UserAuthentication userAuthentication;

    private final ActiveRequestService activeRequestService;

    private final UserService userService;

    private final ProductService productService;

    private final AwaitedMessageResponse awaitedMessageResponse;


    public void updateReceiver(Update update) {
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

        quickSender.message(userId, "[LINK](http://www.example.com/)\n", true);


        try {
            if (Pattern.isAwaitedMessage(ActiveUser.get().isAwaitedMessage()))
                awaitedMessageResponse.response();

            else if (Pattern.isReplyMarkup(update))
                replyMarkupResponse.response(text);

            else if (Pattern.isZalandoURL(text))
                linkResponse.response(text);

            else if (Pattern.isCommand(text))
                commandResponse.response(text, chatId);

        } catch (IllegalArgumentException ex) {
            logger.warn(ex.getMessage());
        } catch (InvalidLinkException | IllegalInputException ex) {
            logger.warn(ex.getLoggerMessage());
            quickSender.message(chatId, ex.getMessage(), false);
        } catch (WebDriverException ex) {
            logger.error("Web Driver exception: " + ex.getMessage());
            quickSender.message(chatId, "Sorry but something goes wrong.", false);
        } catch (ZalandoScraperBotException ex) {
            logger.error(ex.getLoggerMessage());
            quickSender.message(chatId, ex.getMessage(), false);
        } catch (Exception ex) {
            logger.error("Unexpected exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            activeRequestService.deleteByUserId(userId);
            ActiveUser.remove();
        }
    }
}










