package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.api_object.ParseMode;
import com.vicary.zalandoscraper.api_object.keyboard.InlineKeyboardButton;
import com.vicary.zalandoscraper.api_object.keyboard.InlineKeyboardMarkup;
import com.vicary.zalandoscraper.api_request.send.SendMessage;
import com.vicary.zalandoscraper.format.MarkdownV2;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class InlineBlock {
    private final static List<String> HI_MESSAGES = List.of(
            "How are you?",
            "What can i do for you?",
            "How's your day going?",
            "How can I help you today?",
            "Are you doing well?",
            "Is there something I can do to assist you?",
            "What's on your mind?",
            "Need assistance?",
            "What's up?",
            "Can I help?");

    private final static InlineKeyboardButton allProducts = new InlineKeyboardButton("All Products", "-allProducts");

    private final static InlineKeyboardButton addProduct = new InlineKeyboardButton("Add Product", "-addProduct");

    private final static InlineKeyboardButton editPriceProduct = new InlineKeyboardButton("Edit Price Alert", "-editPriceAlert");

    private final static InlineKeyboardButton deleteProduct = new InlineKeyboardButton("Delete Product", "-deleteProduct");

    private final static InlineKeyboardButton notification = new InlineKeyboardButton("Notification", "-notification");

    private final static InlineKeyboardButton deleteAll = new InlineKeyboardButton("Delete All", "-deleteAll");

//    private final static InlineKeyboardButton lastUpdate = new InlineKeyboardButton("", "unknown");

    private final static InlineKeyboardButton back = new InlineKeyboardButton("Back To Menu", "-back");

    private final static InlineKeyboardButton exit = new InlineKeyboardButton("Exit", "-exit");

    private final static InlineKeyboardButton deleteAllYes = new InlineKeyboardButton("Yes", "-deleteAllYes");

    private final static InlineKeyboardButton deleteAllNo = new InlineKeyboardButton("No", "-deleteAllNo");

    private final static InlineKeyboardButton enableOrDisable = new InlineKeyboardButton("", "");

    private final static InlineKeyboardButton setEmail = new InlineKeyboardButton("", "");

    private final static List<InlineKeyboardButton> listOfButtons = List.of(allProducts, addProduct);

    private final static List<InlineKeyboardButton> listOfButtons1 = List.of(editPriceProduct, deleteProduct);

    private final static List<InlineKeyboardButton> listOfButtons2 = List.of(notification);

    private final static List<InlineKeyboardButton> listOfButtons3 = List.of(exit);

    private final static List<InlineKeyboardButton> listOfButtons4 = List.of(back);

    private final static List<InlineKeyboardButton> listOfButtons5 = List.of(deleteAll);

    private final static List<InlineKeyboardButton> listOfButtons6 = List.of(deleteAllYes, deleteAllNo);

    private final static List<InlineKeyboardButton> listOfButtons7 = List.of(enableOrDisable);

    private final static List<InlineKeyboardButton> listOfButtons8 = List.of(setEmail);

//    private final static List<InlineKeyboardButton> listOfButtons9 = List.of(lastUpdate);

    private final static InlineKeyboardMarkup menuMarkup = new InlineKeyboardMarkup(List.of(listOfButtons, listOfButtons1, listOfButtons2, listOfButtons3));

    private final static InlineKeyboardMarkup backMarkup = new InlineKeyboardMarkup(List.of(listOfButtons4));

    private final static InlineKeyboardMarkup yesOrNoMarkup = new InlineKeyboardMarkup(List.of(listOfButtons6));

    private final static InlineKeyboardMarkup notificationMarkup = new InlineKeyboardMarkup(List.of(listOfButtons7, listOfButtons8, listOfButtons4));


    public static SendMessage getNotification(boolean isNotifyByEmailActive, String email) {
        email = email == null ? "Not Specified" : email;
        if (isNotifyByEmailActive) {
            enableOrDisable.setText("Disable email notifications");
            enableOrDisable.setCallbackData("-disableEmail");
        } else {
            enableOrDisable.setText("Enable email notifications");
            enableOrDisable.setCallbackData("-enableEmail");
        }

        if (email.equals("Not Specified")) {
            setEmail.setText("Set email");
            setEmail.setCallbackData("-setEmail");
        } else {
            setEmail.setText("Change email");
            setEmail.setCallbackData("-setEmail");
        }

        String message = """
                *Notification* 📧
                            
                I am able to send price notification via email
                                
                *Status:* %s    
                *Your email:* %s"""
                .formatted(
                        isNotifyByEmailActive ? "Enabled" : "Disabled",
                        MarkdownV2.apply(email).get());

        return SendMessage.builder()
                .text(message)
                .chatId(ActiveUser.get().getChatId())
                .replyMarkup(notificationMarkup)
                .parseMode(ParseMode.MarkdownV2)
                .build();
    }

    public static SendMessage getMenu() {
        ActiveUser user = ActiveUser.get();

        String menuMessage = """
                *Hello %s* 👋
                                
                %s""".formatted(
                MarkdownV2.apply(user.getNick()).get(),
                HI_MESSAGES.get(ThreadLocalRandom.current().nextInt(HI_MESSAGES.size())));

        return SendMessage.builder()
                .chatId(user.getUserId())
                .text(menuMessage)
                .replyMarkup(menuMarkup)
                .parseMode(ParseMode.MarkdownV2)
                .build();
    }

    public static InlineKeyboardMarkup getBack() {
        return backMarkup;
    }

    public static SendMessage getDeleteYesOrNo() {
        ActiveUser user = ActiveUser.get();

        String yesOrNoMessage = "Are you sure you want to delete all your products?";

        return SendMessage.builder()
                .chatId(user.getChatId())
                .text(yesOrNoMessage)
                .replyMarkup(yesOrNoMarkup)
                .build();
    }

    public static InlineKeyboardMarkup getProductChoice(List<ProductDTO> productDTOList, String callbackDataType) {
        List<List<InlineKeyboardButton>> listOfListsOfButtons = new ArrayList<>();
        List<InlineKeyboardButton> listOfButtons = new ArrayList<>();


        for (int i = 0; i < productDTOList.size(); i++) {

            listOfButtons.add(new InlineKeyboardButton(String.valueOf(i + 1), callbackDataType + " " + productDTOList.get(i).getProductId()));
            if (i == productDTOList.size() - 1) {
                listOfListsOfButtons.add(listOfButtons);
            }

            if (listOfButtons.size() == 6) {
                List<InlineKeyboardButton> threeButtons = new ArrayList<>();
                for (int k = 0; k < 6; k++)
                    threeButtons.add(listOfButtons.get(k));

                listOfListsOfButtons.add(threeButtons);
                listOfButtons.clear();
            }
        }

        if (callbackDataType.equals("-delete"))
            listOfListsOfButtons.add(listOfButtons5);

        listOfListsOfButtons.add(listOfButtons4);

        return new InlineKeyboardMarkup(listOfListsOfButtons);
    }
}










