package com.vicary.zalandoscraper.controller;

import com.google.gson.JsonObject;
import com.vicary.zalandoscraper.entity.ProductEntity;
import com.vicary.zalandoscraper.entity.ProductHistoryEntity;
import com.vicary.zalandoscraper.repository.ProductHistoryRepository;
import com.vicary.zalandoscraper.repository.ProductRepository;
import com.vicary.zalandoscraper.repository.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TestController {

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final ProductHistoryRepository productHistoryRepository;

    @GetMapping("/test")
    public void test() {

    }
}





























