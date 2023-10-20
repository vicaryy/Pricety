package com.vicary.zalandoscraper.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.api_object.other.CallbackQuery;
import com.vicary.zalandoscraper.api_request.edit_message.DeleteMessage;
import com.vicary.zalandoscraper.exception.ActiveUserException;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.pattern.Pattern;
import com.vicary.zalandoscraper.service.quick_sender.QuickSender;
import com.vicary.zalandoscraper.service.response.CommandResponse;
import com.vicary.zalandoscraper.service.response.LinkResponse;
import com.vicary.zalandoscraper.service.response.ReplyMarkupResponse;
import lombok.RequiredArgsConstructor;

import com.vicary.zalandoscraper.api_object.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;


@Service
@RequiredArgsConstructor
public class UpdateReceiverService {

    private final static Logger logger = LoggerFactory.getLogger(UpdateReceiverService.class);

    private final QuickSender quickSender;

    private final RequestService requestService;

    private final CommandResponse commandResponse;

    private final ReplyMarkupResponse replyMarkupResponse;

    private final LinkResponse linkResponse;

    private final UpdateVerification updateVerification;

    private final ActiveRequestService activeRequestService;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void updateReceiver(Update update) {
        if (update.getMessage() == null && update.getCallbackQuery() == null) {
            logger.info("Got update without message.");
            return;
        }

        try {
            updateVerification.verify(update);
        } catch (ActiveUserException ignored) {
            return;
        }


        String text = ActiveUser.get().getText();
        String userId = ActiveUser.get().getUserId();
        String chatId = ActiveUser.get().getChatId();
        try {
            if (update.getCallbackQuery() != null)
                replyMarkupResponse.response(update.getCallbackQuery());

            else if (Pattern.isZalandoURL(text))
                linkResponse.response(text);

            else if (Pattern.isCommand(text))
                commandResponse.response(text, chatId);

        } catch (IllegalArgumentException ex) {
            logger.warn(ex.getMessage());
        } catch (InvalidLinkException ex) {
            logger.warn(ex.getLoggerMessage());
            quickSender.message(chatId, ex.getMessage(), false);
        } finally {
            activeRequestService.deleteByUserId(userId);
            ActiveUser.remove();
        }
    }


    public void queryResult(Update update) {
        CallbackQuery query = update.getCallbackQuery();
        String queryId = query.getId();
//
//        InlineKeyboardButton inlineKeyboardButton = InlineKeyboardButton.builder()
//                .text("")
//                .build();
//        EditMessageText editMessageReplyMarkup = EditMessageText.builder()
//                .text("")
//                .chatId("1935527130")
//                .messageId(query.getMessage().getMessageId())
//                .replyMarkup(new InlineKeyboardMarkup(List.of(List.of(inlineKeyboardButton))))
//                .build();

        DeleteMessage deleteMessage = DeleteMessage.builder()
                .chatId("1935527130")
                .messageId(query.getMessage().getMessageId())
                .build();

        try {
            requestService.sendRequest(deleteMessage);
        } catch (WebClientRequestException ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.getHeaders());
            System.out.println(ex.getMethod());
        } catch (WebClientResponseException ex) {
            System.out.println(ex.getStatusText());
            System.out.println(ex.getResponseBodyAsString());
        }
    }
}










