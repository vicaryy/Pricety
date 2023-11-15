package com.vicary.zalandoscraper.service;

import com.vicary.zalandoscraper.api_telegram.api_object.Update;
import com.vicary.zalandoscraper.api_telegram.api_object.User;
import com.vicary.zalandoscraper.api_telegram.api_object.chat.Chat;
import com.vicary.zalandoscraper.api_telegram.api_object.message.Message;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest
class UpdateReceiverServiceTest {
    @Autowired
    private UpdateReceiverService service;


//    @Test
//    void test() {
//        List<Update> updates = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Update update1 = getUpdate();
//            update1.getMessage().getFrom().setId(123L);
//            update1.getMessage().getChat().setId("123");
//            Update update = getUpdate();
//
//            updates.add(update1);
//            updates.add(update);
//        }
//
//        ExecutorService executorService = Executors.newCachedThreadPool();
//
//        for (Update u : updates) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            executorService.submit(() -> service.receive(u));
//        }
//    }
//
//
//    private Update getUpdate() {
//        Update update = new Update();
//        User user = new User();
//        Chat chat = new Chat();
//        chat.setId("1935527130");
//        user.setId(1935527130L);
//        user.setLanguageCode("pl");
//        Message message = Message.builder()
//                .messageId(123)
//                .from(user)
//                .chat(chat)
//                .text("/menu")
//                .build();
//        return Update.builder()
//                .message(message)
//                .build();
//    }
}