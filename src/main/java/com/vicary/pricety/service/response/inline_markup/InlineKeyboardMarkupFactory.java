package com.vicary.pricety.service.response.inline_markup;

import com.vicary.pricety.messages.Messages;
import com.vicary.pricety.model.Product;
import com.vicary.pricety.thread_local.ActiveUser;
import com.vicary.pricety.api_telegram.api_object.keyboard.InlineKeyboardButton;
import com.vicary.pricety.api_telegram.api_object.keyboard.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardMarkupFactory {

    public static InlineKeyboardMarkup getMenu() {
        String language = Messages.menu("language");
        final InlineKeyboardButton allProducts = new InlineKeyboardButton(Messages.menu("allProducts"), "-allProducts");
        final InlineKeyboardButton addProduct = new InlineKeyboardButton(Messages.menu("addProduct"), "-addProduct");
        final InlineKeyboardButton editPriceProduct = new InlineKeyboardButton(Messages.menu("editPriceAlert"), "-editPriceAlert");
        final InlineKeyboardButton deleteProduct = new InlineKeyboardButton(Messages.menu("deleteProduct"), "-deleteProduct");
        final InlineKeyboardButton notification = new InlineKeyboardButton(Messages.menu("notification"), "-notification");
        final InlineKeyboardButton chart = new InlineKeyboardButton(Messages.menu("generateChart"), "-generateProduct");
        final InlineKeyboardButton exit = new InlineKeyboardButton(Messages.menu("exit"), "-exit");
        final InlineKeyboardButton changeLanguage = new InlineKeyboardButton(language, "-lang " + (language.equals("\uD83C\uDDFA\uD83C\uDDF8") ? "en" : "pl"));


        return new InlineKeyboardMarkup(
                List.of(
                        List.of(allProducts, addProduct),
                        List.of(editPriceProduct, deleteProduct),
                        List.of(chart),
                        List.of(notification),
                        List.of(exit),
                        List.of(changeLanguage)
                ));
    }

    public static InlineKeyboardMarkup getNotification(ActiveUser user) {
        final InlineKeyboardButton enableOrDisable = new InlineKeyboardButton();
        final InlineKeyboardButton setEmail = new InlineKeyboardButton();
        final InlineKeyboardButton back = new InlineKeyboardButton(Messages.notifications("back"), "-back");
        final List<InlineKeyboardButton> listOfButtons1 = List.of(enableOrDisable);
        final List<InlineKeyboardButton> listOfButtons2 = List.of(setEmail);
        final List<InlineKeyboardButton> listOfButtons3 = List.of(back);

        if (user.isNotifyByEmail() && user.isVerifiedEmail()) {
            enableOrDisable.setText(Messages.notifications("disable"));
            enableOrDisable.setCallbackData("-disableEmail");
        } else {
            enableOrDisable.setText(Messages.notifications("enable"));
            enableOrDisable.setCallbackData("-enableEmail");
        }

        if (user.getEmail() == null) {
            setEmail.setText(Messages.notifications("setEmail"));
            setEmail.setCallbackData("-setEmail");
        } else {
            setEmail.setText(Messages.notifications("changeEmail"));
            setEmail.setCallbackData("-setEmail");
        }

        return new InlineKeyboardMarkup(List.of(listOfButtons1, listOfButtons2, listOfButtons3));
    }

    public static InlineKeyboardMarkup getBack() {
        final InlineKeyboardButton back = new InlineKeyboardButton(Messages.notifications("back"), "-back");
        return new InlineKeyboardMarkup(List.of(List.of(back)));
    }

    public static InlineKeyboardMarkup getDeleteYesOrNo() {
        final InlineKeyboardButton deleteAllYes = new InlineKeyboardButton(Messages.deleteProduct("yes"), "-deleteAllYes");
        final InlineKeyboardButton deleteAllNo = new InlineKeyboardButton(Messages.deleteProduct("no"), "-deleteAllNo");
        return new InlineKeyboardMarkup(List.of(List.of(deleteAllYes, deleteAllNo)));
    }

    public static InlineKeyboardMarkup getProductChoice(List<Product> products, String callbackDataType) {
        List<List<InlineKeyboardButton>> listOfListsOfButtons = new ArrayList<>();
        List<InlineKeyboardButton> listOfButtons = new ArrayList<>();
        final InlineKeyboardButton back = new InlineKeyboardButton(Messages.editPriceAlert("back"), "-back");

        for (int i = 0; i < products.size(); i++) {

            listOfButtons.add(new InlineKeyboardButton(String.valueOf(i + 1), callbackDataType + " " + products.get(i).getProductId()));
            if (i == products.size() - 1) {
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
            listOfListsOfButtons.add(List.of(new InlineKeyboardButton(Messages.deleteProduct("deleteAll"), "-deleteAll")));

        listOfListsOfButtons.add(List.of(back));

        return new InlineKeyboardMarkup(listOfListsOfButtons);
    }

    public static InlineKeyboardMarkup getVariantChoice(List<String> variants, String requestId) {
        String command = "-l " + requestId + " ";
        List<List<InlineKeyboardButton>> listOfListsOfButtons = new ArrayList<>();
        List<InlineKeyboardButton> listOfButtons = new ArrayList<>();
        for (int i = 0; i < variants.size(); i++) {

            listOfButtons.add(InlineKeyboardButton.builder()
                    .text(variants.get(i))
                    .callbackData(command + variants.get(i))
                    .build());

            if (i == variants.size() - 1) {
                listOfListsOfButtons.add(listOfButtons);
            }

            if (listOfButtons.size() == 3) {
                List<InlineKeyboardButton> threeButtons = new ArrayList<>();
                for (int k = 0; k < 3; k++)
                    threeButtons.add(listOfButtons.get(k));

                listOfListsOfButtons.add(threeButtons);
                listOfButtons.clear();
            }
        }

        return new InlineKeyboardMarkup(listOfListsOfButtons);
    }
}