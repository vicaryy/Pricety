package com.vicary.zalandoscraper.service.repository_services;

import com.vicary.zalandoscraper.repository.LinkRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LinkRequestServiceTest {

    @Autowired
    private LinkRequestService service;

    @MockBean
    private LinkRequestRepository repository;

}