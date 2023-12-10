package com.vicary.zalandoscraper.service.response.inline_markup;

import com.vicary.zalandoscraper.api_telegram.api_object.ParseMode;
import com.vicary.zalandoscraper.api_telegram.api_object.keyboard.ReplyMarkup;
import com.vicary.zalandoscraper.api_telegram.api_request.send.SendMessage;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.format.MarkdownV2;
import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.response.InlineKeyboardMarkupFactory;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

class DeleteProductDisplay implements ProductDisplayer {
    private String chatId;
    private List<Product> products;
    private final QuickSender quickSender;

    public DeleteProductDisplay(@NonNull List<Product> products, @NonNull String chatId) {
        this.products = products;
        this.chatId = chatId;
        this.quickSender = new QuickSender();
    }

    public DeleteProductDisplay(@NonNull List<Product> products, @NonNull String chatId, QuickSender quickSender) {
        this.products = products;
        this.chatId = chatId;
        this.quickSender = quickSender;
    }

    public DeleteProductDisplay() {
        this.quickSender = new QuickSender();
    }


    @Override
    public void display() {
        List<StringBuilder> stringBuilders = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);

            if (i == 0)
                setTitle(sb);

            sb.append(getFullProductDescription(product, i));

            if (i != products.size() - 1)
                sb.append("\n\n\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\\-\n\n");

            int maxProductsAmountInOneMessage = 10;
            if (i % maxProductsAmountInOneMessage == 0 && i != 0) {
                stringBuilders.add(new StringBuilder(sb.toString()));
                sb.setLength(0);
            }
        }

        stringBuilders.add(new StringBuilder(sb));

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .disableWebPagePreview(true)
                .parseMode(ParseMode.MarkdownV2)
                .text("")
                .build();

        if (stringBuilders.size() == 1) {
            sb.append(getSummaryMessage());
            message.setReplyMarkup(getReplyMarkup());

            message.setText(sb.toString());
            quickSender.message(message);
            return;
        }


        for (StringBuilder s : stringBuilders) {
            if (!s.toString().isBlank())
                quickSender.message(chatId, s.toString(), true);
        }

        sb.setLength(0);

        sb.append(getSummaryMessage());
        message.setReplyMarkup(getReplyMarkup());

        message.setText(sb.toString());
        quickSender.message(message);
    }

    @Override
    public void setProductDTOList(List<Product> products) {
        this.products = products;
    }

    @Override
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }


    private void setTitle(StringBuilder sb) {
        sb.append("*").append(Messages.deleteProduct("yourProducts")).append("*\n\n");
    }

    private String getFullProductDescription(Product product, int iterator) {
        String price = getFormattedPrice(product.getPrice(), product.getCurrency());
        String priceAlert = getFormattedPriceAlert(product.getPriceAlert(), product.getCurrency());
        String variant = getFormattedVariant(product.getVariant());
        return """     
                *%s nr %d*
                                    
                *%s:* %s
                *%s:* %s
                *%s:* %s
                *%s:* %s
                *%s:* %s
                *%s:* %s""".
                formatted(
                        Messages.allProducts("product"),
                        iterator + 1,
                        Messages.allProducts("name"),
                        MarkdownV2.apply(product.getName()).get(),
                        Messages.allProducts("description"),
                        MarkdownV2.apply(product.getDescription()).get(),
                        Messages.allProducts("link"),
                        MarkdownV2.apply(product.getLink()).toURL(product.getServiceName()).get(),
                        Messages.allProducts("variant"),
                        MarkdownV2.apply(variant).get(),
                        Messages.allProducts("price"),
                        MarkdownV2.apply(price).get(),
                        Messages.allProducts("priceAlert"),
                        MarkdownV2.apply(priceAlert).get());
    }

    private String getFormattedPrice(double price, String currency) {
        return price == 0 ? Messages.allProducts("soldOut") : String.format("%.2f %s", price, currency).replaceFirst(",", ".");
    }

    private String getFormattedPriceAlert(String priceAlert, String currency) {
        return (!priceAlert.equals("OFF") && !priceAlert.equals("AUTO")) ? priceAlert + " " + currency : priceAlert;
    }

    private String getFormattedVariant(String v) {
        if (v.startsWith("-oneVariant")) {
            String oneVariant = v.substring(11).trim();

            if (oneVariant.equals("Unknown"))
                return Messages.allProducts("unknown");

            return oneVariant;
        }
        return v;
    }

    private String getSummaryMessage() {
        return "\u200E \n\n\n*" + Messages.deleteProduct("select") + "*\\.";
    }

    private ReplyMarkup getReplyMarkup() {
        return InlineKeyboardMarkupFactory.getProductChoice(products, "-delete");
    }
}
