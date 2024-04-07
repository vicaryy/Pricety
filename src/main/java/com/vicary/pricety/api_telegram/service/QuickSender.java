package com.vicary.pricety.api_telegram.service;

import com.vicary.pricety.api_telegram.api_object.Action;
import com.vicary.pricety.api_telegram.api_object.ParseMode;
import com.vicary.pricety.api_telegram.api_object.keyboard.InlineKeyboardMarkup;
import com.vicary.pricety.api_telegram.api_object.message.Message;
import com.vicary.pricety.api_telegram.api_request.InputFile;
import com.vicary.pricety.api_telegram.api_request.edit_message.DeleteMessage;
import com.vicary.pricety.api_telegram.api_request.edit_message.EditMessageText;
import com.vicary.pricety.api_telegram.api_request.send.SendChatAction;
import com.vicary.pricety.api_telegram.api_request.send.SendMessage;
import com.vicary.pricety.api_telegram.api_request.send.SendPhoto;
import com.vicary.pricety.model.ChatNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.File;

public class QuickSender {
    private final Logger logger = LoggerFactory.getLogger(QuickSender.class);
    private final static RequestService requestService = new RequestService();


    public void message(String chatId, String text, boolean markdownV2) {
        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .disableWebPagePreview(true)
                    .build();
            if (markdownV2)
                sendMessage.setParseMode(ParseMode.MarkdownV2);
            requestService.sendWithoutResponse(sendMessage);
        } catch (WebClientResponseException ex) {
            logger.warn("---------------------------");
            logger.warn("Error in sending message request to user '{}'.", chatId);
            logger.warn("Status code: " + ex.getStatusCode());
            logger.warn("Description: " + ex.getStatusText());
            logger.warn("---------------------------");
        } catch (Exception ex) {
            logger.warn("Error in sending message request to user '{}', message: {}", chatId, ex.getMessage());
        }
    }

    public void photo(String chatId, File generatedChart) {
        try {
            SendPhoto sendPhoto = SendPhoto.builder()
                    .chatId(chatId)
                    .photo(InputFile.builder().file(generatedChart).build())
                    .build();
            requestService.send(sendPhoto);
        } catch (WebClientResponseException ex) {
            logger.warn("---------------------------");
            logger.warn("Error in sending photo request to user '{}'.", chatId);
            logger.warn("Status code: " + ex.getStatusCode());
            logger.warn("Description: " + ex.getStatusText());
            logger.warn("---------------------------");
        } catch (Exception ex) {
            logger.warn("Error in sending message request to user '{}', message: {}", chatId, ex.getMessage());
        }
    }

    public void inlineMarkup(String chatId, String text, InlineKeyboardMarkup replyMarkup, boolean markdownV2) {
        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .replyMarkup(replyMarkup)
                    .disableWebPagePreview(true)
                    .build();
            if (markdownV2)
                sendMessage.setParseMode(ParseMode.MarkdownV2);
            requestService.sendWithoutResponse(sendMessage);
        } catch (WebClientResponseException ex) {
            logger.warn("---------------------------");
            logger.warn("Error in sending message request to user '{}'.", chatId);
            logger.warn("Status code: " + ex.getStatusCode());
            logger.warn("Description: " + ex.getStatusText());
            logger.warn("---------------------------");
        } catch (Exception ex) {
            logger.warn("Error in sending message request to user '{}', message: {}", chatId, ex.getMessage());
        }
    }

    public void inlineMarkup(String chatId, InlineKeyboardMarkup replyMarkup, boolean markdownV2) {
        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text("")
                    .replyMarkup(replyMarkup)
                    .disableWebPagePreview(true)
                    .build();
            if (markdownV2)
                sendMessage.setParseMode(ParseMode.MarkdownV2);
            requestService.sendWithoutResponse(sendMessage);
        } catch (WebClientResponseException ex) {
            logger.warn("---------------------------");
            logger.warn("Error in sending message request to user '{}'.", chatId);
            logger.warn("Status code: " + ex.getStatusCode());
            logger.warn("Description: " + ex.getStatusText());
            logger.warn("---------------------------");
        } catch (Exception ex) {
            logger.warn("Error in sending message request to user '{}', message: {}", chatId, ex.getMessage());
        }
    }

    public void inlineMarkup(String chatId, String text, InlineKeyboardMarkup replyMarkup) {
        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .replyMarkup(replyMarkup)
                    .disableWebPagePreview(true)
                    .build();
            requestService.sendWithoutResponse(sendMessage);
        } catch (WebClientResponseException ex) {
            logger.warn("---------------------------");
            logger.warn("Error in sending message request to user '{}'.", chatId);
            logger.warn("Status code: " + ex.getStatusCode());
            logger.warn("Description: " + ex.getStatusText());
            logger.warn("---------------------------");
        } catch (Exception ex) {
            logger.warn("Error in sending message request to user '{}', message: {}", chatId, ex.getMessage());
        }
    }


    public void notification(ChatNotification notification) throws Exception {
        try {
            requestService.sendWithoutResponse(SendMessage.builder()
                    .chatId(notification.getChatId())
                    .text(notification.getMessage())
                    .parseMode(notification.isMarkdownV2() ? ParseMode.MarkdownV2 : null)
                    .build());
        } catch (WebClientResponseException ex) {
            logger.warn("---------------------------");
            logger.warn("Error in sending message request to user '{}'.", notification.getChatId());
            logger.warn("Status code: " + ex.getStatusCode());
            logger.warn("Description: " + ex.getStatusText());
            logger.warn("---------------------------");
        } catch (Exception ex) {
            logger.warn("Error in sending message request to user '{}', message: {}", notification.getChatId(), ex.getMessage());
        }
    }


    public void message(SendMessage sendMessage) {
        try {
            requestService.sendWithoutResponse(sendMessage);
        } catch (WebClientResponseException ex) {
            logger.warn("---------------------------");
            logger.warn("Error in sending message request to user '{}'.", sendMessage.getChatId());
            logger.warn("Status code: " + ex.getStatusCode());
            logger.warn("Description: " + ex.getStatusText());
            logger.warn("---------------------------");
        } catch (Exception ex) {
            logger.warn("Error in sending message request to user '{}', message: {}", sendMessage.getChatId(), ex.getMessage());
        }
    }

    public void deleteMessage(String chatId, int messageId) {
        try {
            DeleteMessage deleteMessage = DeleteMessage.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .build();
            requestService.sendWithoutResponse(deleteMessage);
        } catch (WebClientResponseException ex) {
            logger.warn("---------------------------");
            logger.warn("Error in sending message request to user '{}'.", chatId);
            logger.warn("Status code: " + ex.getStatusCode());
            logger.warn("Description: " + ex.getStatusText());
            logger.warn("---------------------------");
        } catch (Exception ex) {
            logger.warn("Error in sending message request to user '{}', message: {}", chatId, ex.getMessage());
        }
    }

    public void popupMessage(String chatId, String text) {
        int messageId = messageWithReturn(chatId, text, false).getMessageId();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
        deleteMessage(chatId, messageId);
    }

    public void popupMessage(String chatId, String text, long popupTime) {
        int messageId = messageWithReturn(chatId, text, false).getMessageId();
        try {
            Thread.sleep(popupTime);
        } catch (InterruptedException ignored) {
        }
        deleteMessage(chatId, messageId);
    }

    public Message messageWithReturn(String chatId, String text, boolean markdownV2) {
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

    public void editMessageText(EditMessageText editMessageText, String text) {
        try {
            editMessageText.setText(text);
            requestService.sendWithoutResponse(editMessageText);
        } catch (Exception ex) {
            logger.warn("Error in sending editMessageText request, message: {}", ex.getMessage());
        }
    }

    public void chatAction(String chatId, Action action) {
        try {
            SendChatAction sendChatAction = SendChatAction.builder()
                    .chatId(chatId)
                    .action(action)
                    .build();
            requestService.sendWithoutResponse(sendChatAction);
        } catch (WebClientResponseException ex) {
            logger.warn("---------------------------");
            logger.warn("Error in sending message request to user '{}'.", chatId);
            logger.warn("Status code: " + ex.getStatusCode());
            logger.warn("Description: " + ex.getStatusText());
            logger.warn("---------------------------");
        } catch (Exception ex) {
            logger.warn("Error in sending message request to user '{}', message: {}", chatId, ex.getMessage());
        }
    }
}
