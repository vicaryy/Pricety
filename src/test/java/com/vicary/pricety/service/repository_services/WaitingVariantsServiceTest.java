package com.vicary.pricety.service.repository_services;

import com.vicary.pricety.entity.WaitingVariantsEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WaitingVariantsServiceTest {

    @Autowired
    private WaitingVariantsService service;

    @Test
    void getAll() {
    }
}