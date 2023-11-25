package com.vicary.zalandoscraper.model;

import com.vicary.zalandoscraper.messages.Messages;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email {
    private String to;

    private String title;

    private String message;

    private boolean mime;

    public Email(String to) {
        this.to = to;
    }


    public void setPriceAlertMessageAndTitle(ProductDTO p) {
        if (p.getPrice() == 0) {
            setPriceAlertWhenOldPriceIsZero(p);
            return;
        }

        mime = true;
        title = Messages.email("notificationTitle", p.getLanguage());
        String newPrice = String.format("%.2f", p.getNewPrice());
        String oldPrice = String.format("%.2f", p.getPrice());
        String cheaper = String.format("%.2f", p.getPrice() - p.getNewPrice());
        String variant = getFormattedVariant(p);
        message = Messages.email("notificationMessage", p.getLanguage()).formatted(
                cheaper,
                p.getName(),
                p.getDescription(),
                variant,
                p.getLink(),
                oldPrice,
                newPrice
        );
    }

    private void setPriceAlertWhenOldPriceIsZero(ProductDTO p) {
        mime = true;
        title = Messages.email("notificationTitle", p.getLanguage());
        String newPrice = String.format("%.2f", p.getNewPrice());
        String variant = getFormattedVariant(p);
        if (variant.startsWith("-oneVariant "))
            variant = variant.substring(12);
        message = Messages.email("notificationMessageWhenPriceZero", p.getLanguage()).formatted(
                p.getName(),
                p.getDescription(),
                variant,
                p.getLink(),
                newPrice,
                p.getPriceAlert());
    }

    public void setVerificationMessageAndTitle(String token) {
        mime = true;
        title = Messages.email("verificationTitle");
        message = Messages.email("verificationMessage")
                .formatted(token);
    }

    private String getFormattedVariant(ProductDTO p) {
        String variant = p.getVariant();
        if (variant.startsWith("-oneVariant ")) {
            variant = variant.substring(12).trim();
            if (variant.equals("One Variant"))
                variant = Messages.allProducts("oneVariant", p.getLanguage());
            else if (variant.equals("Unknown"))
                variant = Messages.allProducts("unknown", p.getLanguage());
        }
        return variant;
    }

    public void checkValidation() {
        if (to == null || to.isBlank())
            throw new IllegalArgumentException("Value 'to' in Email cannot be null and empty.");

        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Value 'title' in Email cannot be null and empty.");

        if (message == null || message.isBlank())
            throw new IllegalArgumentException("Value 'message' in Email cannot be null and empty.");
    }
}
