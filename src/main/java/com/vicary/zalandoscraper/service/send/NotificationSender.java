package com.vicary.zalandoscraper.service.send;

import com.vicary.zalandoscraper.entity.NotificationEntity;
import com.vicary.zalandoscraper.format.MarkdownV2;
import com.vicary.zalandoscraper.model.Email;
import com.vicary.zalandoscraper.service.quick_sender.QuickSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationSender {

    private final QuickSender quickSender;

    private final EmailSender emailSender;

    public void send(List<NotificationEntity> notifications) {
        for (NotificationEntity n : notifications) {
            sendOnTelegram(n);
            if (n.isNotifyByEmail())
                sendOnEmail(n);
        }
    }

    private void sendOnTelegram(NotificationEntity n) {
        quickSender.message(n.getUserId(), getTelegramMessage(n), true);
    }

    private void sendOnEmail(NotificationEntity n) {
        String emailTo = n.getEmail();
        String title = "[Price Alert] Zalando product became cheaper!";
        String message = getEmailMessage(n);
        emailSender.sendAsMime(new Email(emailTo, title, message));
    }

    private String getEmailMessage(NotificationEntity n) {
        String newPrice = String.format("%.2f", n.getNewPrice());
        String oldPrice = String.format("%.2f", n.getOldPrice());
        String cheaper = String.format("%.2f", n.getOldPrice() - n.getNewPrice());
        String variant = n.getVariant();
        if (variant.startsWith("-oneVariant "))
            variant = variant.substring(12);
        return """
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
                n.getProductName(),
                n.getDescription(),
                variant,
                n.getLink(),
                oldPrice,
                newPrice
        );
    }

    private String getTelegramMessage(NotificationEntity n) {
        String newPrice = String.format("%.2f", n.getNewPrice());
        String oldPrice = String.format("%.2f", n.getOldPrice());
        String cheaper = String.format("%.2f", n.getOldPrice() - n.getNewPrice());
        String variant = n.getVariant();
        if (variant.startsWith("-oneVariant "))
            variant = variant.substring(12);
        return """
                *Price Alert* 🔔
                                
                The product you have in your watchlist became cheaper by %s zł\\!
                                
                *Name*: %s
                *Description*: %s
                *Variant*: %s
                *Link*: %s
                                
                *Old Price*: %s zł
                *New Price*: %s zł
                                
                _Have a nice shopping\\!_
                """.formatted(
                MarkdownV2.apply(cheaper).get(),
                MarkdownV2.apply(n.getProductName()).get(),
                MarkdownV2.apply(n.getDescription()).get(),
                MarkdownV2.apply(variant).get(),
                MarkdownV2.apply(n.getLink()).toZalandoURL().get(),
                MarkdownV2.apply(oldPrice).get(),
                MarkdownV2.apply(newPrice).get()
        );
    }
}
