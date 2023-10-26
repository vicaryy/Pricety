package com.vicary.zalandoscraper.service.entity;

import com.vicary.zalandoscraper.entity.UpdateHistoryEntity;
import com.vicary.zalandoscraper.pattern.Pattern;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UpdatesHistoryServiceTest {

    @Autowired
    private UpdatesHistoryService service;

    @SneakyThrows
    @Test
    void test() {
//        UpdateHistoryEntity entity = UpdateHistoryEntity.builder()
//                .productId(3)
//                .price(100)
//                .lastUpdate(LocalDateTime.now())
//                .build();
//
//        Thread.sleep(2000);
//
//        UpdateHistoryEntity entity1 = UpdateHistoryEntity.builder()
//                .productId(15)
//                .price(2000)
//                .lastUpdate(LocalDateTime.now())
//                .build();
//
//        Thread.sleep(3337);
//
//
//        UpdateHistoryEntity entity2 = UpdateHistoryEntity.builder()
//                .productId(100)
//                .price(3)
//                .lastUpdate(LocalDateTime.now())
//                .build();
//
//        Thread.sleep(6030);
//
//
//        UpdateHistoryEntity entity3 = UpdateHistoryEntity.builder()
//                .productId(19)
//                .price(2143.12)
//                .lastUpdate(LocalDateTime.now())
//                .build();
//
//        Thread.sleep(1423);
//
//
//        UpdateHistoryEntity entity4 = UpdateHistoryEntity.builder()
//                .productId(9999)
//                .price(2.5)
//                .lastUpdate(LocalDateTime.now())
//                .build();
//
//        Thread.sleep(9132);
//
//
//        service.saveUpdate(entity);
//        service.saveUpdate(entity1);
//        service.saveUpdate(entity2);
//        service.saveUpdate(entity3);
//        service.saveUpdate(entity4);
    }

    @Test
    void test1() {
        System.out.println(service.getLastUpdateTime().format(DateTimeFormatter.ofPattern(Pattern.getDatePattern())));
    }
}








