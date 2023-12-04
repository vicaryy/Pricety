package com.vicary.zalandoscraper.service;

import com.vicary.zalandoscraper.thread_local.ActiveUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateReceiverServiceTest {

    @Test
    void test() {
        ActiveUser activeUser = new ActiveUser();
        activeUser.setText("asd");

        String someText = activeUser.getText();
        someText = "dsa";

        System.out.println(activeUser.getText());
    }

}