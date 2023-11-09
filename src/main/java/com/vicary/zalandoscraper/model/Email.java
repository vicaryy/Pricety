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


    public void setPriceAlertTitle() {
        title = "[Price Alert] Zalando product became cheaper!";
    }

    public void setPriceAlertMessage(ProductDTO p) {
        mime = true;
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
}
