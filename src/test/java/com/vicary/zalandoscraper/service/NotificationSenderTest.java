package com.vicary.zalandoscraper.service;

import com.vicary.zalandoscraper.entity.NotificationEntity;
import com.vicary.zalandoscraper.service.send.NotificationSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class NotificationSenderTest {

    @Autowired
    private NotificationSender sender;

    @Test
    void test() {
        var notification = NotificationEntity.builder()
                .userId("1935527130")
                .email("vicarycholewa@gmail.com")
                .productName("Nazwa Produktu")
                .description("Opis produktu")
                .newPrice(2299.90)
                .oldPrice(2500.90)
                .link("https://www.zalando.pl/")
                .notifyByEmail(true)
                .variant("L")
                .build();

        var notification1 = NotificationEntity.builder()
                .userId("1935527130")
                .email("vicarycholewa@gmail.com")
                .productName("Nazwa Produktu")
                .description("Opis produktu")
                .newPrice(300.90)
                .oldPrice(900.90)
                .link("https://www.zalando.pl/")
                .notifyByEmail(true)
                .variant("L")
                .build();

        var notification2 = NotificationEntity.builder()
                .userId("1935527130")
                .email("vicarycholewa@gmail.com")
                .productName("Nazwa Produktu")
                .description("Opis produktu")
                .newPrice(350.90)
                .oldPrice(360.90)
                .link("https://www.zalando.pl/")
                .notifyByEmail(true)
                .variant("L")
                .build();

        var notification3 = NotificationEntity.builder()
                .userId("1935527130")
                .email("vicarycholewa@gmail.com")
                .productName("Nazwa Produktu")
                .description("Opis produktu")
                .newPrice(100.90)
                .oldPrice(360.90)
                .link("https://www.zalando.pl/")
                .notifyByEmail(true)
                .variant("L")
                .build();

        var notification4 = NotificationEntity.builder()
                .userId("1935527130")
                .email("vicarycholewa@gmail.com")
                .productName("Nazwa Produktu")
                .description("Opis produktu")
                .newPrice(200.90)
                .oldPrice(399.90)
                .link("https://www.zalando.pl/")
                .notifyByEmail(true)
                .variant("L")
                .build();

        sender.send(List.of(notification));
    }

}