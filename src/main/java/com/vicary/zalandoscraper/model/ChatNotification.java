package com.vicary.zalandoscraper.model;

import com.vicary.zalandoscraper.format.MarkdownV2;
import com.vicary.zalandoscraper.messages.Messages;
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

    public void setWaitingUserMessage(String language) {
        message = Messages.chat("waitingUser", language);
    }

    public void setDefaultPriceAlertMessage(Product p) {
        if (p.isNotifyWhenAvailable() && p.getNewPrice() != 0) {
            setPriceAlertWhenNotifyWhenAvailable(p);
            return;
        }

        if (p.getPrice() == 0) {
            setMessageWhenOldPriceIsZero(p);
            return;
        }
        String newPrice = String.format("%.2f", p.getNewPrice());
        String oldPrice = String.format("%.2f", p.getPrice());
        String cheaper = String.format("%.2f", p.getPrice() - p.getNewPrice());
        String variant = getFormattedVariant(p);

        message = Messages.chat("notificationMessage", p.getUserDTO().getNationality()).formatted(
                MarkdownV2.apply(cheaper).get(),
                MarkdownV2.apply(p.getCurrency()).get(),
                MarkdownV2.apply(p.getName()).get(),
                MarkdownV2.apply(p.getDescription()).get(),
                MarkdownV2.apply(variant).get(),
                MarkdownV2.apply(p.getLink()).toURL(p.getServiceName()).get(),
                MarkdownV2.apply(oldPrice).get(),
                MarkdownV2.apply(p.getCurrency()).get(),
                MarkdownV2.apply(newPrice).get(),
                MarkdownV2.apply(p.getCurrency()).get()
        );
    }

    private void setMessageWhenOldPriceIsZero(Product p) {
        String newPrice = String.format("%.2f", p.getNewPrice());
        String variant = getFormattedVariant(p);
        message = Messages.chat("notificationMessageWhenPriceZero", p.getUserDTO().getNationality()).formatted(
                MarkdownV2.apply(p.getName()).get(),
                MarkdownV2.apply(p.getDescription()).get(),
                MarkdownV2.apply(variant).get(),
                MarkdownV2.apply(p.getLink()).toURL(p.getServiceName()).get(),
                MarkdownV2.apply(newPrice).get(),
                MarkdownV2.apply(p.getCurrency()).get(),
                MarkdownV2.apply(p.getPriceAlert()).get(),
                MarkdownV2.apply(p.getCurrency()).get()
        );
    }

    private void setPriceAlertWhenNotifyWhenAvailable(Product p) {
        String newPrice = String.format("%.2f", p.getNewPrice());
        String variant = getFormattedVariant(p);
        message = Messages.chat("notificationMessageWhenAvailable", p.getUserDTO().getNationality()).formatted(
                MarkdownV2.apply(p.getName()).get(),
                MarkdownV2.apply(p.getDescription()).get(),
                MarkdownV2.apply(variant).get(),
                MarkdownV2.apply(p.getLink()).toURL(p.getServiceName()).get(),
                MarkdownV2.apply(newPrice).get(),
                MarkdownV2.apply(p.getCurrency()).get()
        );
    }

    private String getFormattedVariant(Product p) {
        String variant = p.getVariant();
        if (variant.startsWith("-oneVariant ")) {
            variant = variant.substring(12).trim();
            if (variant.equals("One Variant"))
                variant = Messages.allProducts("oneVariant", p.getUserDTO().getNationality());
            else if (variant.equals("Unknown"))
                variant = Messages.allProducts("unknown", p.getUserDTO().getNationality());
        }
        return variant;
    }
}
