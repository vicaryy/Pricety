package com.vicary.zalandoscraper.api_telegram;

import com.vicary.zalandoscraper.api_telegram.api_object.Action;
import com.vicary.zalandoscraper.api_telegram.api_object.ParseMode;
import com.vicary.zalandoscraper.api_telegram.api_object.message.Message;
import com.vicary.zalandoscraper.api_telegram.api_request.edit_message.DeleteMessage;
import com.vicary.zalandoscraper.api_telegram.api_request.edit_message.EditMessageText;
import com.vicary.zalandoscraper.api_telegram.api_request.send.SendChatAction;
import com.vicary.zalandoscraper.api_telegram.api_request.send.SendMessage;
import com.vicary.zalandoscraper.model.ChatNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuickSender {
    private static final Logger logger = LoggerFactory.getLogger(QuickSender.class);

    private final static RequestService requestService = new RequestService();

    private QuickSender(){
    }

    public static void message(String chatId, String text, boolean markdownV2) {
        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .disableWebPagePreview(true)
                    .build();
            if (markdownV2)
                sendMessage.setParseMode(ParseMode.MarkdownV2);
            requestService.sendAsync(sendMessage);
        } catch (Exception ex) {
            logger.warn("Error in sending message request, message: {}", ex.getMessage());
        }
    }

    public static void notification(ChatNotification notification) throws Exception {
        requestService.sendAsync(SendMessage.builder()
                .chatId(notification.getChatId())
                .text(notification.getMessage())
                .parseMode(notification.isMarkdownV2() ? ParseMode.MarkdownV2 : null)
                .build());
    }


    public static void message(SendMessage sendMessage) {
        try {
            requestService.sendAsync(sendMessage);
        } catch (Exception ex) {
            logger.warn("Error in sending message request, message: {}", ex.getMessage());
        }
    }

    public static void deleteMessage(String chatId, int messageId) {
        try {
            DeleteMessage deleteMessage = DeleteMessage.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .build();
            requestService.sendAsync(deleteMessage);
        } catch (Exception ex) {
            logger.warn("Error in deleting message request, message: {}", ex.getMessage());
        }
    }

    public static void popupMessage(String chatId, String text) {
        int messageId = messageWithReturn(chatId, text, false).getMessageId();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
        deleteMessage(chatId, messageId);
    }

    public static void popupMessage(String chatId, String text, long popupTime) {
        int messageId = messageWithReturn(chatId, text, false).getMessageId();
        try {
            Thread.sleep(popupTime);
        } catch (InterruptedException ignored) {
        }
        deleteMessage(chatId, messageId);
    }

    public static Message messageWithReturn(String chatId, String text, boolean markdownV2) {
        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .build();
            if (markdownV2)
                sendMessage.setParseMode(ParseMode.MarkdownV2);
            return requestService.send(sendMessage);
        } catch (Exception ex) {
            logger.warn("Error in sending message with return request, message: {}", ex.getMessage());
            return null;
        }
    }

    public static void editMessageText(EditMessageText editMessageText, String text) {
        try {
            editMessageText.setText(text);
            requestService.sendAsync(editMessageText);
        } catch (Exception ex) {
            logger.warn("Error in sending editMessageText request, message: {}", ex.getMessage());
        }
    }

    public static void chatAction(String chatId, Action action) {
        try {
            SendChatAction sendChatAction = SendChatAction.builder()
                    .chatId(chatId)
                    .action(action)
                    .build();
            requestService.sendAsync(sendChatAction);
        } catch (Exception ex) {
            logger.warn("Error in sending chatAction request, message: {}", ex.getMessage());
        }
    }
}
