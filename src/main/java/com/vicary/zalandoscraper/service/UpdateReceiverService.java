package com.vicary.zalandoscraper.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.api_object.message.Message;
import com.vicary.zalandoscraper.api_object.other.CallbackQuery;
import com.vicary.zalandoscraper.api_request.edit_message.DeleteMessage;
import com.vicary.zalandoscraper.api_request.edit_message.EditMessageReplyMarkup;
import com.vicary.zalandoscraper.api_request.edit_message.EditMessageText;
import com.vicary.zalandoscraper.api_request.send.SendMessage;
import com.vicary.zalandoscraper.entity.LinkRequestEntity;
import com.vicary.zalandoscraper.entity.ProductEntity;
import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.exception.ActiveUserException;
import com.vicary.zalandoscraper.exception.InvalidLinkException;
import com.vicary.zalandoscraper.exception.ZalandoScraperBotException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.pattern.Pattern;
import com.vicary.zalandoscraper.service.quick_sender.QuickSender;
import com.vicary.zalandoscraper.service.response.CommandResponse;
import com.vicary.zalandoscraper.service.response.LinkResponse;
import com.vicary.zalandoscraper.service.response.ReplyMarkupResponse;
import lombok.RequiredArgsConstructor;

import com.vicary.zalandoscraper.api_object.Update;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;


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


    public void updateReceiver(Update update) {
        int i = 1;
        if (i == 1) {
//            ActiveUser.get().setChatId("1935527130");
//            Product product = Product.builder()
//                    .name("Bluzza")
//                    .description("Zajebista")
//                    .price(99.99)
//                    .variant("M")
//                    .link("zalando.pl")
//                    .lastUpdate(LocalDateTime.now())
//                    .build();
//            productService.saveProduct(product);

//            ProductEntity product = productService.getProductById(9L);
//            System.out.println(product.getLastUpdate().format(DateTimeFormatter.ofPattern(Pattern.datePattern())));
            // YYYY-MM-DD HH:MM:SS.ssssss

            productService.updateProductPrice(9L, 1000, "NO ALERT");
        }
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
        try {
            if (Pattern.isReplyMarkup(update))
                replyMarkupResponse.response(text);

            else if (Pattern.isZalandoURL(text))
                linkResponse.response(text);

            else if (Pattern.isCommand(text))
                commandResponse.response(text, chatId);

        } catch (IllegalArgumentException ex) {
            logger.warn(ex.getMessage());
        } catch (InvalidLinkException ex) {
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










