package com.vicary.zalandoscraper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Controller
public class HomeController {

    enum asd {
        ADMIN
    }
    @GetMapping("/")
    public String home() {
        System.out.println(Objects.equals(asd.ADMIN.toString(), "ADMIN"));
        return "index";
    }
}
