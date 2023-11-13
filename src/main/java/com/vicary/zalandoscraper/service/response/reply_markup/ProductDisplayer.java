package com.vicary.zalandoscraper.service.response.reply_markup;

import com.vicary.zalandoscraper.api_telegram.api_object.ParseMode;
import com.vicary.zalandoscraper.api_telegram.api_object.keyboard.ReplyMarkup;
import com.vicary.zalandoscraper.api_telegram.api_request.send.SendMessage;
import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.format.MarkdownV2;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.service.response.InlineBlock;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

class ProductDisplayer {

    private final String chatId;
    private final Type type;
    private final List<ProductDTO> productDTOList;

    public ProductDisplayer(@NonNull List<ProductDTO> productDTOList, @NonNull Type type, @NonNull String chatId) {
        this.productDTOList = productDTOList;
        this.type = type;
        this.chatId = chatId;
    }

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
            QuickSender.message(message);
            return;
        }


        for (StringBuilder s : stringBuilders)
            QuickSender.message(chatId, s.toString(), true);

        sb.setLength(0);

        sb.append(getSummaryMessage());
        message.setReplyMarkup(getReplyMarkup());

        message.setText(sb.toString());
        QuickSender.message(message);
    }


    private void setTitle(StringBuilder sb) {
        if (type == Type.ALL)
            sb.append("*That's your products* 📝\n\n\n");
        else if (type == Type.EDIT)
            sb.append("*Products to edit* ⚙️\n\n\n");
        else if (type == Type.DELETE)
            sb.append("Products to delete 🗑️\n\n\n");
    }

    private String getFullProductDescription(ProductDTO dto, int iterator) {
        String price = getFormattedPrice(dto.getPrice());
        String priceAlert = getFormattedPriceAlert(dto.getPriceAlert());
        String variant = getFormattedVariant(dto.getVariant());
        return """     
                *Product nr %d*
                                    
                *Name:* %s
                *Description:* %s
                *Link:* %s
                *Variant:* %s
                *Price:* %s
                *Price alert:* %s""".
                formatted(iterator + 1,
                        MarkdownV2.apply(dto.getName()).get(),
                        MarkdownV2.apply(dto.getDescription()).get(),
                        MarkdownV2.apply(dto.getLink()).toZalandoURL().get(),
                        MarkdownV2.apply(variant).get(),
                        MarkdownV2.apply(price).get(),
                        MarkdownV2.apply(priceAlert).get());
    }

    private String getFormattedPrice(double p) {
        return p == 0 ? "Sold Out" : String.format("%.2f zł", p).replaceFirst(",", ".");
    }

    private String getFormattedPriceAlert(String p) {
        return (!p.equals("OFF") && !p.equals("AUTO")) ? p + " zł" : p;
    }

    private String getFormattedVariant(String v) {
        if (v.startsWith("-oneVariant"))
            v = v.substring(12);
        return v;
    }

    private String getSummaryMessage() {
        if (type == Type.ALL)
            return "\u200E \n\n\n*Did you know?*\nThe Eiffel Tower can be 15 cm taller during the summer due to the expansion of iron in the heat 💀";
        else if (type == Type.EDIT)
            return "\u200E \n\n*Please select the item you want to edit*\\.";
        else if (type == Type.DELETE)
            return "\u200E \n\n*Please select the item you want to delete*\\.";
        return null;
    }

    private ReplyMarkup getReplyMarkup() {
        if (type == Type.ALL)
            return InlineBlock.getBack();
        else if (type == Type.EDIT)
            return InlineBlock.getProductChoice(productDTOList, "-edit");
        else if (type == Type.DELETE)
            return InlineBlock.getProductChoice(productDTOList, "-delete");
        return null;
    }
}



















