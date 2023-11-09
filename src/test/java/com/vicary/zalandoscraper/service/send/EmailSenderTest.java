package com.vicary.zalandoscraper.service.send;

import com.vicary.zalandoscraper.updater.sender.EmailSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailSenderTest {

    @Autowired
    private EmailSender sender;


    @Test
    void test() {
    }
}