package com.vicary.zalandoscraper.service.response.inline_markup;

import com.vicary.zalandoscraper.api_telegram.api_object.ParseMode;
import com.vicary.zalandoscraper.api_telegram.api_object.keyboard.ReplyMarkup;
import com.vicary.zalandoscraper.api_telegram.api_request.send.SendMessage;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.format.MarkdownV2;
import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.service.response.InlineKeyboardMarkupFactory;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

class AllProductDisplay implements ProductDisplayer {
    private String chatId;
    private List<ProductDTO> productDTOList;
    private final QuickSender quickSender;
    private final ThreadRandom threadRandom;

    public AllProductDisplay(@NonNull List<ProductDTO> productDTOList, @NonNull String chatId) {
        this.productDTOList = productDTOList;
        this.chatId = chatId;
        this.quickSender = new QuickSender();
        this.threadRandom = new ThreadRandom();
    }

    public AllProductDisplay(@NonNull List<ProductDTO> productDTOList, @NonNull String chatId, QuickSender quickSender, ThreadRandom threadRandom) {
        this.productDTOList = productDTOList;
        this.chatId = chatId;
        this.quickSender = quickSender;
        this.threadRandom = threadRandom;
    }

    public AllProductDisplay() {
        this.quickSender = new QuickSender();
        this.threadRandom = new ThreadRandom();
    }

    @Override
    public void display() {
        List<StringBuilder> stringBuilders = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < productDTOList.size(); i++) {
            ProductDTO dto = productDTOList.get(i);

            if (i == 0)
                setTitle(sb);

            sb.append(getFullProductDescription(dto, i));

            if (i != productDTOList.size() - 1)
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


        for (StringBuilder s : stringBuilders)
            quickSender.message(chatId, s.toString(), true);

        sb.setLength(0);

        sb.append(getSummaryMessage());
        message.setReplyMarkup(getReplyMarkup());

        message.setText(sb.toString());
        quickSender.message(message);
    }

    @Override
    public void setProductDTOList(List<ProductDTO> DTOs) {
        this.productDTOList = DTOs;
    }

    @Override
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }


    private void setTitle(StringBuilder sb) {
        sb.append("*").append(Messages.allProducts("yourProducts")).append("*\n\n");
    }

    private String getFullProductDescription(ProductDTO dto, int iterator) {
        String price = getFormattedPrice(dto.getPrice());
        String priceAlert = getFormattedPriceAlert(dto.getPriceAlert());
        String variant = getFormattedVariant(dto.getVariant());
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
                        MarkdownV2.apply(dto.getName()).get(),
                        Messages.allProducts("description"),
                        MarkdownV2.apply(dto.getDescription()).get(),
                        Messages.allProducts("link"),
                        MarkdownV2.apply(dto.getLink()).toURL().get(),
                        Messages.allProducts("variant"),
                        MarkdownV2.apply(variant).get(),
                        Messages.allProducts("price"),
                        MarkdownV2.apply(price).get(),
                        Messages.allProducts("priceAlert"),
                        MarkdownV2.apply(priceAlert).get());
    }

    private String getFormattedPrice(double p) {
        return p == 0 ? Messages.allProducts("soldOut") : String.format("%.2f zł", p).replaceFirst(",", ".");
    }

    private String getFormattedPriceAlert(String p) {
        return (!p.equals("OFF") && !p.equals("AUTO")) ? p + " zł" : p;
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
        return """
                                
                                
                                
                *%s*
                %s""".formatted(
                Messages.allProducts("didYouKnow"),
                MarkdownV2.apply(Messages.allProducts("funFact" + threadRandom.generate(1, 25))).get());
    }

    private ReplyMarkup getReplyMarkup() {
        return InlineKeyboardMarkupFactory.getBack();
    }
}
