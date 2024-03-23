package com.vicary.zalandoscraper.controller;

import com.vicary.zalandoscraper.entity.ProductEntity;
import com.vicary.zalandoscraper.repository.ProductRepository;
import com.vicary.zalandoscraper.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TestController {

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    @GetMapping("/test")
    public void test() {

    }
}





























