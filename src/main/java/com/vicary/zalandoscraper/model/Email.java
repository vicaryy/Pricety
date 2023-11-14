package com.vicary.zalandoscraper.model;

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
        title = "[Price Alert] Zalando product became cheaper!";
        String newPrice = String.format("%.2f", p.getNewPrice());
        String oldPrice = String.format("%.2f", p.getPrice());
        String cheaper = String.format("%.2f", p.getPrice() - p.getNewPrice());
        String variant = p.getVariant();
        if (variant.startsWith("-oneVariant "))
            variant = variant.substring(12);
        message = """
                <html>
                <body>
                <font size=4><b>Price Alert 🔔</b></font>
                <br><br>
                <font size=3>The product you have in your watchlist became cheaper by %s zł!</font>
                <br><br>         
                <font size=2><b>Name:</b> %s<br></font>
                <font size=2><b>Description:</b> %s<br></font>
                <font size=2><b>Variant:</b> %s<br></font>
                <font size=2><b>Link:</b> %s<br></font>
                <br>
                <font size=2><b>Old Price</b>: %s zł<br></font>
                <font size=2><b>New Price:</b> %s zł<br></font>
                <br>       
                <font size=2><i>Have a nice shopping!</i></font>
                </body>
                </html>
                """.formatted(
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
        title = "[Price Alert] Zalando product became cheaper!";
        String newPrice = String.format("%.2f", p.getNewPrice());
        String variant = p.getVariant();
        if (variant.startsWith("-oneVariant "))
            variant = variant.substring(12);
        message = """
                <html>
                <body>
                <font size=4><b>Price Alert 🔔</b></font>
                <br><br>
                <font size=3>The product you have in your watchlist became cheaper!</font>
                <br><br>         
                <font size=2><b>Name:</b> %s<br></font>
                <font size=2><b>Description:</b> %s<br></font>
                <font size=2><b>Variant:</b> %s<br></font>
                <font size=2><b>Link:</b> %s<br></font>
                <br>
                <font size=2><b>New Price</b>: %s zł<br></font>
                <font size=2><b>Price Alert:</b> %s zł<br></font>
                <br>       
                <font size=2><i>Have a nice shopping!</i></font>
                </body>
                </html>
                """.formatted(
                p.getName(),
                p.getDescription(),
                variant,
                p.getLink(),
                newPrice,
                p.getPriceAlert()
        );
    }

    public void setVerificationMessageAndTitle(String token) {
        mime = true;
        title = "[Verification] Email verification code";
        message = """
                <html>
                <body>
                <font size=3>Here is your verification code.</font><br><br>
                <font size=3>Paste it into the chat with the bot:</font><br>
                <font size=4><b>v-%s</b></font><br><br>
                <font size=1><i>If you don't recognize this message, please ignore it.</i></font>"""
                .formatted(token);
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
