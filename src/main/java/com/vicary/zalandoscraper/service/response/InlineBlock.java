package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.thread_local.ActiveUser;
import com.vicary.zalandoscraper.api_telegram.api_object.ParseMode;
import com.vicary.zalandoscraper.api_telegram.api_object.keyboard.InlineKeyboardButton;
import com.vicary.zalandoscraper.api_telegram.api_object.keyboard.InlineKeyboardMarkup;
import com.vicary.zalandoscraper.api_telegram.api_request.send.SendMessage;
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
    private final static InlineKeyboardButton deleteAll = new InlineKeyboardButton("Delete All", "-deleteAll");

    private final static InlineKeyboardButton back = new InlineKeyboardButton("Back To Menu", "-back");

    private final static InlineKeyboardButton deleteAllYes = new InlineKeyboardButton("Yes", "-deleteAllYes");

    private final static InlineKeyboardButton deleteAllNo = new InlineKeyboardButton("No", "-deleteAllNo");

    private final static InlineKeyboardButton enableOrDisable = new InlineKeyboardButton("", "");

    private final static InlineKeyboardButton setEmail = new InlineKeyboardButton("", "");

    private final static List<InlineKeyboardButton> listOfButtons4 = List.of(back);

    private final static List<InlineKeyboardButton> listOfButtons5 = List.of(deleteAll);

    private final static List<InlineKeyboardButton> listOfButtons6 = List.of(deleteAllYes, deleteAllNo);

    private final static List<InlineKeyboardButton> listOfButtons7 = List.of(enableOrDisable);

    private final static List<InlineKeyboardButton> listOfButtons8 = List.of(setEmail);

    private final static InlineKeyboardMarkup backMarkup = new InlineKeyboardMarkup(List.of(listOfButtons4));

    private final static InlineKeyboardMarkup yesOrNoMarkup = new InlineKeyboardMarkup(List.of(listOfButtons6));

    private final static InlineKeyboardMarkup notificationMarkup = new InlineKeyboardMarkup(List.of(listOfButtons7, listOfButtons8, listOfButtons4));


    public static SendMessage getMenu() {
        ActiveUser user = ActiveUser.get();
        String nick = user.getNick() != null ? user.getNick() : "";
        String language = Messages.menu("language");

        final InlineKeyboardButton allProducts = new InlineKeyboardButton(Messages.menu("allProducts"), "-allProducts");
        final InlineKeyboardButton addProduct = new InlineKeyboardButton(Messages.menu("addProduct"), "-addProduct");
        final InlineKeyboardButton editPriceProduct = new InlineKeyboardButton(Messages.menu("editPriceAlert"), "-editPriceAlert");
        final InlineKeyboardButton deleteProduct = new InlineKeyboardButton(Messages.menu("deleteProduct"), "-deleteProduct");
        final InlineKeyboardButton notification = new InlineKeyboardButton(Messages.menu("notification"), "-notification");
        final InlineKeyboardButton exit = new InlineKeyboardButton(Messages.menu("exit"), "-exit");
        final InlineKeyboardButton changeLanguage = new InlineKeyboardButton(language, "-lang " + (language.equals("\uD83C\uDDFA\uD83C\uDDF8") ? "en" : "pl"));

        final InlineKeyboardMarkup menu = new InlineKeyboardMarkup(
                List.of(
                        List.of(allProducts, addProduct),
                        List.of(editPriceProduct, deleteProduct),
                        List.of(notification),
                        List.of(exit),
                        List.of(changeLanguage)));

        String menuMessage = """
                *%s %s* 👋
                                
                %s""".formatted(
                Messages.menu("hi"),
                MarkdownV2.apply(nick).get(),
                Messages.menu("how" + ThreadLocalRandom.current().nextInt(1, 10)));

        return SendMessage.builder()
                .chatId(user.getUserId())
                .text(menuMessage)
                .replyMarkup(menu)
                .parseMode(ParseMode.MarkdownV2)
                .build();
    }

    public static SendMessage getNotification(boolean isNotifyByEmail, boolean isVerifiedEmail, String email) {
        String verified = "";

        if (email == null)
            email = "Not Specified";

        else if (isVerifiedEmail)
            verified = "\n\n✅ Email Verified";

        else
            verified = "\n\n⚠️ Email Not Verified";


        if (isNotifyByEmail && isVerifiedEmail) {
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
                *Your email:* %s%s"""
                .formatted(
                        isNotifyByEmail ? "Enabled" : "Disabled",
                        MarkdownV2.apply(email).get(),
                        verified);

        return SendMessage.builder()
                .text(message)
                .chatId(ActiveUser.get().getChatId())
                .replyMarkup(notificationMarkup)
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
        final InlineKeyboardButton back = new InlineKeyboardButton(Messages.editPriceAlert("back"), "-back");

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

        listOfListsOfButtons.add(List.of(back));

        return new InlineKeyboardMarkup(listOfListsOfButtons);
    }
}










