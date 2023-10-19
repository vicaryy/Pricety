package com.vicary.zalandoscraper.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vicary.zalandoscraper.api_object.inline_query.inline_query_result.InlineQueryResult;
import com.vicary.zalandoscraper.api_object.inline_query.inline_query_result.InlineQueryResultArticle;
import com.vicary.zalandoscraper.api_object.inline_query.input_message_content.InputTextMessageContent;
import com.vicary.zalandoscraper.api_object.keyboard.InlineKeyboardButton;
import com.vicary.zalandoscraper.api_object.keyboard.InlineKeyboardMarkup;
import com.vicary.zalandoscraper.api_object.keyboard.ReplyKeyboardRemove;
import com.vicary.zalandoscraper.api_object.message.Message;
import com.vicary.zalandoscraper.api_object.other.CallbackQuery;
import com.vicary.zalandoscraper.api_request.AnswerCallbackQuery;
import com.vicary.zalandoscraper.api_request.edit_message.DeleteMessage;
import com.vicary.zalandoscraper.api_request.edit_message.EditMessageReplyMarkup;
import com.vicary.zalandoscraper.api_request.edit_message.EditMessageReplyMarkupAsInlineMessage;
import com.vicary.zalandoscraper.api_request.edit_message.EditMessageText;
import com.vicary.zalandoscraper.api_request.inline_query.AnswerInlineQuery;
import com.vicary.zalandoscraper.api_request.send.SendMessage;
import com.vicary.zalandoscraper.service.RequestService;
import com.vicary.zalandoscraper.service.quick_sender.QuickSender;
import com.vicary.zalandoscraper.service.response.CommandResponse;
import lombok.RequiredArgsConstructor;

import com.vicary.zalandoscraper.api_object.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UpdateReceiverService {

    private final QuickSender quickSender;

    private final RequestService requestService;

    private final CommandResponse commandResponse;

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void updateReceiver(Update update) {
        String text = "";
        String chatId = "";
        if (update.getMessage() != null) {
            text = update.getMessage().getText();
            chatId = update.getMessage().getChat().getId().toString();
        }

//        if (update.getCallbackQuery() != null) {
//            queryResult(update);
//            return;
//        }
//
//
//        if (update.getMessage() != null)
//            chatId = update.getChatId();
//
//
//        InlineKeyboardButton button = InlineKeyboardButton.builder().text("siema").callbackData("siema_callback").build();
//        InlineKeyboardButton button1 = InlineKeyboardButton.builder().text("hej").callbackData("hej_callback").build();
//        InlineKeyboardButton button2 = InlineKeyboardButton.builder().text("elo").callbackData("elo_callback").build();
//
//        List<InlineKeyboardButton> buttonList = List.of(button, button1);
//        List<InlineKeyboardButton> buttonList1 = List.of(button, button1, button2);
//        var inline = new InlineKeyboardMarkup(List.of(buttonList, buttonList1));
//
//        SendMessage sendMessage = new SendMessage(chatId, "siema");
//        sendMessage.setReplyMarkup(inline);
//
//        try {
//            Message message = requestService.sendRequest(sendMessage);
//            System.out.println("------------------------------------------------");
//            System.out.println("Return message: \n" + gson.toJson(message));
//        } catch (WebClientRequestException ex) {
//            System.out.println(ex.getMessage());
//            System.out.println(ex.getHeaders());
//            System.out.println(ex.getMethod());
//        } catch (WebClientResponseException ex) {
//            System.out.println(ex.getStatusText());
//            System.out.println(ex.getResponseBodyAsString());
//        }


        if (text.startsWith("/"))
            commandResponse.response(text, chatId);
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










