package com.vicary.zalandoscraper.controller;

import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.entity.WebUserEntity;
import com.vicary.zalandoscraper.model.*;
import com.vicary.zalandoscraper.repository.WebUserRepository;
import com.vicary.zalandoscraper.security.JwtService;
import com.vicary.zalandoscraper.service.repository_services.ProductService;
import com.vicary.zalandoscraper.service.repository_services.UserService;
import com.vicary.zalandoscraper.service.repository_services.WebUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TestController {

    private final ProductService productService;

    private final UserService userService;

    private final WebUserService webUserService;

    private final JwtService jwtService = new JwtService();
    private final WebUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/test")
    private void test() {
//        productService.updatePhotoURL();
    }

    @GetMapping("/")
    private String home() {
        return "index";
    }

    @GetMapping("/join")
    private String join(Model model) {
        model.addAttribute("logInModel", new LogInModel());
        model.addAttribute("registerModel", new RegisterModel());
        model.addAttribute("forgotPasswordModel", new ForgotPasswordModel());
        return "join";
    }

    @PostMapping("/join/log-in")
    private String join1(@ModelAttribute LogInModel logInModel, HttpServletResponse response) {
        if (userService.isLoggedUserValidateTrue(logInModel)) {
            WebUserEntity webUserEntity = new WebUserEntity();
            Cookie cookie = new Cookie("access_key", jwtService.generateJwt());
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:/join";
        }
        if (userService.isLoggedUserValidateFalse(logInModel))
            return "redirect:/join";
        return "/join";
    }

    @PostMapping("/join/register")
    private String join2(@ModelAttribute RegisterModel registerModel, HttpServletResponse response) {
        // sprawdz walidacje danych

        WebUserEntity webUserEntity = WebUserEntity.builder()
                .email(registerModel.getEmail())
                .password(passwordEncoder.encode(registerModel.getPassword()))
                .role("USER")
                .activated(false)
                .build();

        webUserService.registerUser(webUserEntity);

        return "redirect:/join";
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

















