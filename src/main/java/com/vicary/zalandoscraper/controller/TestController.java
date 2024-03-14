package com.vicary.zalandoscraper.controller;

import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.model.ProductTemplate;
import com.vicary.zalandoscraper.service.repository_services.ProductService;
import com.vicary.zalandoscraper.service.repository_services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TestController {

    private final ProductService productService;

    private final UserService userService;

    @GetMapping("/test")
    private void test() {
//        productService.updatePhotoURL();
    }

    @GetMapping("/")
    private String home() {
        return "index";
    }

    @GetMapping("/join")
    private String join() {
        return "join";
    }

//    @GetMapping("/account")
//    private String account() {
//        return "account";
//    }


    // 6488358449
    @GetMapping("/account")
    private String account(Model model) {
        String userId = "6488358449";
        List<ProductTemplate> products = productService.getAllTemplates();
        model.addAttribute("user", products.get(2).getUser());
        model.addAttribute("products", products);
        return "account";
    }
}

















