package com.vicary.zalandoscraper.controller;

import com.vicary.zalandoscraper.model.Error;
import com.vicary.zalandoscraper.model.ForgotPasswordModel;
import com.vicary.zalandoscraper.model.LogInModel;
import com.vicary.zalandoscraper.model.RegisterModel;
import com.vicary.zalandoscraper.security.JwtService;
import com.vicary.zalandoscraper.service.repository_services.UserService;
import com.vicary.zalandoscraper.service.repository_services.WebUserService;
import com.vicary.zalandoscraper.utils.Cookies;
import jakarta.servlet.http.Cookie;
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

@Controller
@RequiredArgsConstructor
public class JoinController {
    private final static Logger logger = LoggerFactory.getLogger(JoinController.class);

    private final WebUserService webUserService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService = new JwtService();


    @GetMapping("/join")
    public String join(@RequestParam(value = "l", required = false) boolean logIn, Model model) {
        if (logIn)
            model.addAttribute("logIn", true);
        else
            model.addAttribute("signUp", true);
        setModelAttributes(model);
        return "join";
    }

    @PostMapping("/join/log-in")
    public String join(@ModelAttribute LogInModel logInModel, Model model, HttpServletResponse response) {

        try {
            userService.checkLogInModelValidation(logInModel, passwordEncoder);
        } catch (IllegalArgumentException ex) {
            logger.warn(ex.getMessage());
            model.addAttribute("logIn", true);
            setModelAttributes(model, new Error("LogIn", ex.getMessage()));
            return "join";
        }
        Cookie cookie = Cookies.getJwtCookie(jwtService.generateJwt(userService.findByEmail(logInModel.getEmail())));
        response.addCookie(cookie);
        return "redirect:/";
    }

    @PostMapping("/join/register")
    public String join(@ModelAttribute RegisterModel registerModel, Model model, HttpServletResponse response) {
        try {
            userService.checkRegisterModelValidation(registerModel);
        } catch (IllegalArgumentException ex) {
            logger.warn(ex.getMessage());
            model.addAttribute("signUp", true);
            setModelAttributes(model, new Error("SignUp", ex.getMessage()));
            return "join";
        }

        userService.registerUser(registerModel, passwordEncoder);
        return "redirect:/";
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
