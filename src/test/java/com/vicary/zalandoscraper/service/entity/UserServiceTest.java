package com.vicary.zalandoscraper.service.entity;

import com.vicary.zalandoscraper.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService service;


    @Test
    void findByUserId_expectWhatever_Test() {
    }
}