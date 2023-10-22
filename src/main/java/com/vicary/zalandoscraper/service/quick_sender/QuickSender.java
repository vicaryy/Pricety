package com.vicary.zalandoscraper.service.quick_sender;

import com.vicary.zalandoscraper.api_object.message.Message;
import com.vicary.zalandoscraper.api_request.edit_message.DeleteMessage;
import com.vicary.zalandoscraper.api_request.edit_message.EditMessageText;
import com.vicary.zalandoscraper.api_request.send.SendChatAction;
import com.vicary.zalandoscraper.api_request.send.SendMessage;
import com.vicary.zalandoscraper.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class QuickSender {
    private static final Logger logger = LoggerFactory.getLogger(QuickSender.class);

    private final RequestService requestService;

    public void message(String chatId, String text, boolean markdownV2) {
        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .parseMode(markdownV2 ? "MarkdownV2" : "")
                    .disableWebPagePreview(true)
                    .build();
            requestService.sendRequestAsync(sendMessage);
        } catch (Exception ex) {
            logger.warn("Error in sending message request, message: {}", ex.getMessage());
        }
    }

    public void deleteMessage(String chatId, int messageId) {
        try {
            DeleteMessage deleteMessage = DeleteMessage.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .build();
            requestService.sendRequestAsync(deleteMessage);
        } catch (Exception ex) {
            logger.warn("Error in deleting message request, message: {}", ex.getMessage());
        }
    }

    public Message messageWithReturn(String chatId, String text, boolean markdownV2) {
        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .parseMode(markdownV2 ? "MarkdownV2" : "")
                    .build();
            return requestService.sendRequest(sendMessage);
        } catch (Exception ex) {
            logger.warn("Error in sending message with return request, message: {}", ex.getMessage());
            return null;
        }
    }

    public void editMessageText(EditMessageText editMessageText, String text) {
        try {
            editMessageText.setText(text);
            requestService.sendRequestAsync(editMessageText);
        } catch (Exception ex) {
            logger.warn("Error in sending editMessageText request, message: {}", ex.getMessage());
        }
    }

    public void chatAction(String chatId, String action) {
        try {
            SendChatAction sendChatAction = SendChatAction.builder()
                    .chatId(chatId)
                    .action(action)
                    .build();
            requestService.sendRequestAsync(sendChatAction);
        } catch (Exception ex) {
            logger.warn("Error in sending chatAction request, message: {}", ex.getMessage());
        }
    }
}
