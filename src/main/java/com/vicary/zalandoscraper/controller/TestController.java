package com.vicary.zalandoscraper.controller;

import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.entity.WebUserEntity;
import com.vicary.zalandoscraper.model.*;
import com.vicary.zalandoscraper.model.Error;
import com.vicary.zalandoscraper.repository.WebUserRepository;
import com.vicary.zalandoscraper.security.JwtService;
import com.vicary.zalandoscraper.service.repository_services.ProductService;
import com.vicary.zalandoscraper.service.repository_services.UserService;
import com.vicary.zalandoscraper.service.repository_services.WebUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static Logger logger = LoggerFactory.getLogger(TestController.class);

    private final ProductService productService;

    private final WebUserService webUserService;
    private final JwtService jwtService = new JwtService();

    @GetMapping("/")
    private String home() {
        return "index";
    }

    @GetMapping("/join")
    private String join(@RequestParam(value = "l", required = false) boolean logIn, Model model) {
        if (logIn)
            model.addAttribute("logIn", true);
        else
            model.addAttribute("signUp", true);
        setModelAttributes(model);
        return "join";
    }

    @PostMapping("/join/log-in")
    private String join(@ModelAttribute LogInModel logInModel, Model model, HttpServletResponse response) {

        try {
            webUserService.checkLogInModelValidation(logInModel);
        } catch (IllegalArgumentException ex) {
            logger.warn(ex.getMessage());
            model.addAttribute("logIn", true);
            setModelAttributes(model, new Error("LogIn", ex.getMessage()));
            return "join";
        }
        Cookie cookie = new Cookie("access_key", jwtService.generateJwt(webUserService.getByEmail(logInModel.getEmail()).get()));
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/join";
    }

    @PostMapping("/join/register")
    private String join(@ModelAttribute RegisterModel registerModel, Model model, HttpServletResponse response) {
        try {
            webUserService.checkRegisterModelValidation(registerModel);
        } catch (IllegalArgumentException ex) {
            logger.warn(ex.getMessage());
            model.addAttribute("signUp", true);
            setModelAttributes(model, new Error("SignUp", ex.getMessage()));
            return "join";
        }

        webUserService.registerUser(registerModel);
        return "redirect:/";
    }

    // 6488358449
    @GetMapping("/account")
    private String account(Model model) {
        String userId = "6488358449";
        List<ProductTemplate> products = productService.getAllTemplates();
        model.addAttribute("user", products.get(2).getUser());
        model.addAttribute("products", products);
        return "account";
    }



    private void setModelAttributes(Model model) {
        model.addAttribute("logInModel", new LogInModel());
        model.addAttribute("registerModel", new RegisterModel());
        model.addAttribute("forgotPasswordModel", new ForgotPasswordModel());
    }

    private void setModelAttributes(Model model, Error error) {
        model.addAttribute("logInModel", new LogInModel());
        model.addAttribute("registerModel", new RegisterModel());
        model.addAttribute("forgotPasswordModel", new ForgotPasswordModel());
        model.addAttribute("error" + error.getType(), true);
        model.addAttribute("errorInfo" + error.getType(), error.getMessage());
    }
}
















