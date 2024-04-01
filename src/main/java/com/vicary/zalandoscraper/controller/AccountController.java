package com.vicary.zalandoscraper.controller;

import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.model.ProductTemplate;
import com.vicary.zalandoscraper.repository.ProductHistoryRepository;
import com.vicary.zalandoscraper.service.repository_services.ProductService;
import com.vicary.zalandoscraper.service.repository_services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final ProductService productService;
    private final UserService userService;
    private final ProductHistoryRepository productHistoryRepository;

    @GetMapping("/account")
    public String account(Model model, Authentication authentication, @RequestParam(name = "settings", required = false) boolean settings) {
        String userEmail = authentication.getPrincipal().toString();
        UserEntity user = userService.findWebUserByEmail(userEmail);
        List<ProductTemplate> products = productService.getTemplatesByUserEmail(userEmail);
        if (settings)
            model.addAttribute("settings", true);
        model.addAttribute("user", user);
        model.addAttribute("products", products);
        return "account";
    }

    @PatchMapping("/account/productNotify")
    public String notifyWhenAvailableProduct(@RequestParam(name = "productId") long productId, Authentication authentication) {
        if (doesUserHaveProduct(productId, authentication))
            productService.updateProductNotifyWhenAvailable(productId, true);

        return "empty";
    }

    @PatchMapping("/account/product")
    public String editProduct(
            @RequestParam(name = "item-id") long productId,
            @RequestParam(name = "item-title") String itemTitle,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "alert") String alert,
            Authentication authentication) {
        if (doesUserHaveProduct(productId, authentication))
            productService.updateProduct(productId, itemTitle, description, alert);
        return "empty";
    }

    @DeleteMapping("/account/product")
    public String deleteProduct(@RequestParam(name = "item-id") long productId, Authentication authentication) {
        if (doesUserHaveProduct(productId, authentication))
            productService.deleteProductById(productId);
        return "empty";
    }

    @PatchMapping("/account/telegramGet")
    public String getDataFromTelegram(Authentication authentication, Model model) {
        String email = authentication.getPrincipal().toString();
        if (userService.existsByVerifiedEmailAndTelegram(email, true)) {
            userService.updateWebUserByTelegram(email);
            return "redirect:/account?settings=true";
        }
        return "empty";
    }

    @PatchMapping("/account/telegramSend")
    public String sendDataToTelegram() {
        return "empty";
    }

    private boolean doesUserHaveProduct(long productId, Authentication authentication) {
        return userService.findWebUserByEmail(authentication.getPrincipal().toString())
                .getProducts()
                .stream()
                .anyMatch(e -> e.getId() == productId);
    }
}
