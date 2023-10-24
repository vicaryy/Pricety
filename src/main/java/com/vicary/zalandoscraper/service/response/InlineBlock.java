package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.api_object.keyboard.InlineKeyboardButton;
import com.vicary.zalandoscraper.api_object.keyboard.InlineKeyboardMarkup;
import com.vicary.zalandoscraper.api_request.send.SendMessage;
import com.vicary.zalandoscraper.service.dto.ProductDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
    private final static InlineKeyboardButton back = new InlineKeyboardButton("Back To Menu", "-back");

    private final static InlineKeyboardButton exit = new InlineKeyboardButton("Exit", "-exit");

    private final static List<InlineKeyboardButton> listOfButtons = List.of(allProducts, addProduct);

    private final static List<InlineKeyboardButton> listOfButtons1 = List.of(editPriceProduct, deleteProduct);

    private final static List<InlineKeyboardButton> listOfButtons2 = List.of(notification);

    private final static List<InlineKeyboardButton> listOfButtons3 = List.of(exit);

    private final static List<InlineKeyboardButton> listOfButtons4 = List.of(back);

    private final static List<List<InlineKeyboardButton>> backButtons = List.of(listOfButtons4);

    private final static List<List<InlineKeyboardButton>> menuButtons = List.of(listOfButtons, listOfButtons1, listOfButtons2, listOfButtons3);

    private final static InlineKeyboardMarkup menuMarkup = new InlineKeyboardMarkup(menuButtons);

    private final static InlineKeyboardMarkup backMarkup = new InlineKeyboardMarkup(backButtons);

    public static SendMessage getMenu() {
        ActiveUser user = ActiveUser.get();

        String menuMessage = """
                Hi %s,
                %s""".formatted(user.getNick(), HI_MESSAGES.get(ThreadLocalRandom.current().nextInt(HI_MESSAGES.size())));

        return SendMessage.builder()
                .chatId(user.getUserId())
                .text(menuMessage)
                .replyMarkup(menuMarkup)
                .build();
    }

    public static InlineKeyboardMarkup getBack() {
        return backMarkup;
    }

    public static InlineKeyboardMarkup getProductChoice(List<ProductDTO> productDTOList) {
        List<List<InlineKeyboardButton>> listOfListsOfButtons = new ArrayList<>();
        List<InlineKeyboardButton> listOfButtons = new ArrayList<>();

        for (int i = 0; i < productDTOList.size(); i++) {

            listOfButtons.add(new InlineKeyboardButton(String.valueOf(i + 1), "-edit " + productDTOList.get(i).getProductId()));
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

        listOfListsOfButtons.add(listOfButtons4);

        return new InlineKeyboardMarkup(listOfListsOfButtons);
    }
}










