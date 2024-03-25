package com.vicary.zalandoscraper.controller;

import com.vicary.zalandoscraper.entity.EmailVerificationEntity;
import com.vicary.zalandoscraper.model.*;
import com.vicary.zalandoscraper.model.Error;
import com.vicary.zalandoscraper.security.JwtService;
import com.vicary.zalandoscraper.sender.EmailSenderService;
import com.vicary.zalandoscraper.service.repository_services.EmailVerificationService;
import com.vicary.zalandoscraper.service.repository_services.UserService;
import com.vicary.zalandoscraper.utils.Cookies;
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

import java.util.Arrays;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class JoinController {
    private final static Logger logger = LoggerFactory.getLogger(JoinController.class);

    private final EmailSenderService emailSenderService;

    private final EmailVerificationService emailVerificationService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService = new JwtService();


    @GetMapping("/join")
    public String join(
            @RequestParam(value = "l", required = false) boolean logIn,
            @RequestParam(value = "activation", required = false) String activation,
            Model model,
            HttpServletResponse response) {

        if (activation != null)
            return verifyEmail(activation, response);

        if (logIn)
            model.addAttribute("logIn", true);
        else
            model.addAttribute("signUp", true);
        setModelAttributes(model);
        return "join";
    }

    @PostMapping("/join/log-in")
    public String logIn(@ModelAttribute LogInModel logInModel, Model model, HttpServletResponse response) {

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
    public String register(@ModelAttribute RegisterModel registerModel, Model model) {
        try {
            userService.checkRegisterModelValidation(registerModel);
        } catch (IllegalArgumentException ex) {
            logger.warn(ex.getMessage());
            model.addAttribute("signUp", true);
            setModelAttributes(model, new Error("SignUp", ex.getMessage()));
            return "join";
        }

        long userId = userService.registerUser(registerModel, passwordEncoder).getUserId();
        String token = emailVerificationService.createVerification(userId).getToken();
        try {
            emailSenderService.sendLinkActivation(registerModel.getEmail(), "http://localhost:7070/join?activation=" + token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("email", registerModel.getEmail());
        return "join-verify";
    }

    @PostMapping("/join/log-out")
    public String logOut(HttpServletResponse response, HttpServletRequest request) {
        System.out.println(Arrays.toString(request.getCookies()));
        logger.info("Dostałem post");
        response.addCookie(Cookies.getEmptyJwtCookie());
        response.addCookie(new Cookie("test", "testoweCookies"));
        return "redirect:/join";
    }

    public String verifyEmail(@RequestParam String activation, HttpServletResponse response) {
        Optional<EmailVerificationEntity> entity = emailVerificationService.findByTokenAndDelete(activation);
        if (entity.isEmpty())
            return "redirect:/";


        userService.setVerifiedEmail(entity.get().getUserId(), true);
        Cookie cookie = Cookies.getJwtCookie(jwtService.generateJwt(userService.findByUserId(entity.get().getUserId())));
        response.addCookie(cookie);
        return "join-verified";
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
