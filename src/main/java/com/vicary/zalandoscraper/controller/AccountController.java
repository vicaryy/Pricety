package com.vicary.zalandoscraper.controller;

import com.vicary.zalandoscraper.entity.ProductHistoryEntity;
import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.model.ProductTemplate;
import com.vicary.zalandoscraper.repository.ProductHistoryRepository;
import com.vicary.zalandoscraper.service.repository_services.ProductHistoryService;
import com.vicary.zalandoscraper.service.repository_services.ProductService;
import com.vicary.zalandoscraper.service.repository_services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final ProductService productService;
    private final UserService userService;
    private final ProductHistoryRepository productHistoryRepository;

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

    private boolean doesUserHaveProduct(long productId, Authentication authentication) {
        return userService.findByEmail(authentication.getPrincipal().toString())
                .getProducts()
                .stream()
                .anyMatch(e -> e.getId() == productId);
    }
}
