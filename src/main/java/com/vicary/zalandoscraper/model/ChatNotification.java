package com.vicary.zalandoscraper.model;

import com.vicary.zalandoscraper.format.MarkdownV2;
import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatNotification {

    private String chatId;

    private String message;

    private boolean markdownV2;

    public void setDefaultMessageNotification(ProductDTO p) {
        if (p.getPrice() == 0) {
            setMessageWhenOldPriceIsZero(p);
            return;
        }
        String newPrice = String.format("%.2f", p.getNewPrice());
        String oldPrice = String.format("%.2f", p.getPrice());
        String cheaper = String.format("%.2f", p.getPrice() - p.getNewPrice());
        String variant = p.getVariant();
        if (variant.startsWith("-oneVariant "))
            variant = variant.substring(12);
        message = Messages.chat("notificationMessage", p.getLanguage()).formatted(
                MarkdownV2.apply(cheaper).get(),
                MarkdownV2.apply(p.getName()).get(),
                MarkdownV2.apply(p.getDescription()).get(),
                MarkdownV2.apply(variant).get(),
                MarkdownV2.apply(p.getLink()).toZalandoURL().get(),
                MarkdownV2.apply(oldPrice).get(),
                MarkdownV2.apply(newPrice).get()
        );
    }

    private void setMessageWhenOldPriceIsZero(ProductDTO p) {
        String newPrice = String.format("%.2f", p.getNewPrice());
        String variant = p.getVariant();
        if (variant.startsWith("-oneVariant "))
            variant = variant.substring(12);
        message = Messages.chat("notificationMessageWhenPriceZero", p.getLanguage()).formatted(
                MarkdownV2.apply(p.getName()).get(),
                MarkdownV2.apply(p.getDescription()).get(),
                MarkdownV2.apply(variant).get(),
                MarkdownV2.apply(p.getLink()).toZalandoURL().get(),
                MarkdownV2.apply(newPrice).get(),
                MarkdownV2.apply(p.getPriceAlert()).get()
        );
    }
}
