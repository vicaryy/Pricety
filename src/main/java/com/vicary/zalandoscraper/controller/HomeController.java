package com.vicary.zalandoscraper.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

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
}
