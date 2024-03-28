package com.vicary.zalandoscraper.controller;

import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.ScraperService;
import com.vicary.zalandoscraper.service.repository_services.ProductService;
import com.vicary.zalandoscraper.service.repository_services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final ScraperService scraperService;

    private final UserService userService;

    private final ProductService productService;


    @GetMapping({"/", "/#"})
    public String home(Authentication authentication, Model model) {
        if (authentication != null)
            model.addAttribute("logged", true);
        else
            model.addAttribute("logged", false);

        return "index";
    }

    @PostMapping("/add")
    public String addItem(@RequestParam(name = "url") String url, Model model, Authentication authentication) {
        if (authentication == null) {
            model.addAttribute("unauthorized", true);
            model.addAttribute("errorInfo", "You have to log in first!");
            return "/fragments/home/add-item";
        }
        List<String> variants;
//        System.out.println(authentication.getPrincipal());
        try {
            variants = scraperService.scrapVariants(url);
        } catch (Exception ex) {
            model.addAttribute("error", true);
            model.addAttribute("errorInfo", ex.getMessage());
            return "/fragments/home/add-item";
        }

        if (variants.size() == 1) {
            Product product = scraperService.scrapProduct(url, variants.get(0));
            model.addAttribute("success", true);
            return "/fragments/home/add-item";
        }

        model.addAttribute("size", true);
        model.addAttribute("variants", variants);
        model.addAttribute("url", url);
        return "/fragments/home/add-item";
    }

    @PostMapping("/addByVariant")
    public String addItemByVariant(
            @RequestParam(name = "u") String url,
            @RequestParam(name = "v") String variant,
            Model model, HttpServletRequest request) throws InterruptedException {

        Product product;
        try {
            product = scraperService.scrapProduct(url, variant);
        } catch (Exception ex) {
            model.addAttribute("error", true);
            model.addAttribute("errorInfo", ex.getMessage());
            return "/fragments/home/add-item";
        }

        model.addAttribute("success", true);
        log.info("Dodano nowy produkt: " + product);
        return "/fragments/home/add-item";
    }
}
