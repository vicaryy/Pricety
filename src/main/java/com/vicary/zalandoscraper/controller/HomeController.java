package com.vicary.zalandoscraper.controller;

import ch.qos.logback.core.testUtil.RandomUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

@Controller
@Slf4j
public class HomeController {

    @GetMapping({"/", "/#"})
    public String home(Authentication authentication, Model model) {
        if (authentication != null)
            model.addAttribute("logged", true);
        else
            model.addAttribute("logged", false);

        return "index";
    }

    @PostMapping("/add")
    public String addItem(@RequestParam(name = "url") String url, Model model) throws InterruptedException {
        Thread.sleep(1500);
        int x = ThreadLocalRandom.current().nextInt(10);
        if (x % 2 == 0)
            model.addAttribute("success", true);
        else {
            model.addAttribute("error", true);
            model.addAttribute("errorInfo", "Service amazon.pl not found.");
        }

        return "/fragments/home/add-item";
    }
}
