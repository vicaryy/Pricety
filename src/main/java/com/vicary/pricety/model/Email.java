package com.vicary.pricety.model;

import com.vicary.pricety.messages.Messages;
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


    public void setPriceAlertMessageAndTitle(Product p) {
        if (p.isNotifyWhenAvailable() && p.getNewPrice() != 0) {
            setPriceAlertWhenNotifyWhenAvailable(p);
            return;
        }

        if (p.getPrice() == 0) {
            setPriceAlertWhenOldPriceIsZero(p);
            return;
        }

        mime = true;
        title = Messages.email("notificationTitle", p.getUserDTO().getNationality());
        String newPrice = String.format("%.2f", p.getNewPrice());
        String oldPrice = String.format("%.2f", p.getPrice());
        String cheaper = String.format("%.2f", p.getPrice() - p.getNewPrice());
        String variant = getFormattedVariant(p);
        message = Messages.email("notificationMessage", p.getUserDTO().getNationality()).formatted(
                cheaper,
                p.getCurrency(),
                p.getName(),
                p.getDescription(),
                variant,
                p.getLink(),
                oldPrice,
                p.getCurrency(),
                newPrice,
                p.getCurrency()
        );
    }

    private void setPriceAlertWhenOldPriceIsZero(Product p) {
        mime = true;
        title = Messages.email("notificationTitle", p.getUserDTO().getNationality());
        String newPrice = String.format("%.2f", p.getNewPrice());
        String variant = getFormattedVariant(p);
        if (variant.startsWith("-oneVariant "))
            variant = variant.substring(12);
        message = Messages.email("notificationMessageWhenPriceZero", p.getUserDTO().getNationality()).formatted(
                p.getName(),
                p.getDescription(),
                variant,
                p.getLink(),
                newPrice,
                p.getCurrency(),
                p.getPriceAlert(),
                p.getCurrency()
        );
    }

    private void setPriceAlertWhenNotifyWhenAvailable(Product p) {
        mime = true;
        title = Messages.email("notificationTitleWhenAvailable", p.getUserDTO().getNationality());
        String newPrice = String.format("%.2f", p.getNewPrice());
        String variant = getFormattedVariant(p);
        if (variant.startsWith("-oneVariant "))
            variant = variant.substring(12);
        message = Messages.email("notificationMessageWhenAvailable", p.getUserDTO().getNationality()).formatted(
                p.getName(),
                p.getDescription(),
                variant,
                p.getLink(),
                newPrice,
                p.getCurrency()
        );
    }

    public void setVerificationMessageAndTitle(String token) {
        mime = true;
        title = Messages.email("verificationTitle");
        message = Messages.email("verificationMessage")
                .formatted(token);
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

    public void checkValidation() {
        if (to == null || to.isBlank())
            throw new IllegalArgumentException("Value 'to' in Email cannot be null and empty.");

        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Value 'title' in Email cannot be null and empty.");

        if (message == null || message.isBlank())
            throw new IllegalArgumentException("Value 'message' in Email cannot be null and empty.");
    }
}
