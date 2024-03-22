package com.vicary.zalandoscraper.controller;

import com.vicary.zalandoscraper.model.ProductTemplate;
import com.vicary.zalandoscraper.service.repository_services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final ProductService productService;

    // 6488358449
    @GetMapping("/account")
    public String account(Model model, Authentication authentication) {
        String userEmail = authentication.getPrincipal().toString();
//        String userId = "6488358449";
        List<ProductTemplate> products = productService.getAllTemplates();
        model.addAttribute("user", products.get(2).getUser());
        model.addAttribute("products", products);
        return "account";
    }
}
