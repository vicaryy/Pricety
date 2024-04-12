package com.vicary.pricety.controller;

import com.vicary.pricety.entity.UserEntity;
import com.vicary.pricety.exception.IllegalInputException;
import com.vicary.pricety.model.ContactModel;
import com.vicary.pricety.model.Email;
import com.vicary.pricety.model.Product;
import com.vicary.pricety.pattern.Pattern;
import com.vicary.pricety.sender.EmailSenderService;
import com.vicary.pricety.service.ScraperService;
import com.vicary.pricety.service.repository_services.ProductService;
import com.vicary.pricety.service.repository_services.UserService;
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

    private final ProductService productService;

    private final UserService userService;

    private final EmailSenderService emailSenderService;

    @GetMapping({"/", "/#"})
    public String home(Authentication authentication, Model model) {
        if (authentication != null)
            model.addAttribute("logged", true);
        else
            model.addAttribute("logged", false);

        model.addAttribute("ContactModel", new ContactModel());
        return "index";
    }

    @PostMapping("/add")
    public String addItem(@RequestParam(name = "url") String url, Model model, Authentication authentication) {
        log.info("Auth: {}", authentication);
        if (authentication == null)
            return unauthorizedTemplate("Only for logged users!", model);

        List<String> variants;
        try {
            variants = scrapVariants(url);

            if (variants.size() == 1) {
                scrapAndSaveProduct(url, variants.get(0), authentication);
                return successTemplate("Product added successfully!", model);
            }
        } catch (IllegalInputException ex) {
            log.warn(ex.getLoggerMessage());
            return errorTemplate(ex.getMessage(), model);
        } catch (Exception ex) {
            return errorTemplate(ex.getMessage(), model);
        }

        return variantsTemplate(url, variants, model);
    }

    @PostMapping("/addByVariant")
    public String addItemByVariant(
            @RequestParam(name = "u") String url,
            @RequestParam(name = "v") String variant,
            Model model,
            Authentication authentication) throws InterruptedException {

        try {
            scrapAndSaveProduct(url, variant, authentication);
        } catch (IllegalInputException ex) {
            log.warn(ex.getLoggerMessage());
            return errorTemplate(ex.getMessage(), model);
        } catch (Exception ex) {
            return errorTemplate(ex.getMessage(), model);
        }
        return successTemplate("Product added successfully!", model);
    }

    @PostMapping("/contact")
    public String sendContactForm(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "message") String message,
            Model model) {
        if (name.isBlank() || email.isBlank() || message.isBlank()) {
            model.addAttribute("error", true);
            model.addAttribute("errorInfo", "Fields cannot be empty.");
            model.addAttribute("name", name);
            model.addAttribute("email", email);
            model.addAttribute("message", message);
            return "fragments/home/contact";
        }

        if (!Pattern.isEmail(email)) {
            model.addAttribute("error", true);
            model.addAttribute("errorInfo", "Invalid email address.");
            model.addAttribute("name", name);
            model.addAttribute("email", email);
            model.addAttribute("message", message);
            return "fragments/home/contact";
        }

        try {
            emailSenderService.send(
                    new Email(
                            "vicarycholewa@gmail.com",
                            "Wiadomość formularzowa",
                            """
                                    Wiadomość od użytkownika %s - %s:
                                    
                                    %s""".formatted(name, email, message), false));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        model.addAttribute("success", true);
        return "fragments/home/contact";
    }

    private void scrapAndSaveProduct(String url, String variant, Authentication authentication) {
        Product product = scraperService.scrapProduct(url, variant);
        checkUserAndProductValidations(product, authentication);
        saveProduct(product, authentication);
    }

    private List<String> scrapVariants(String url) {
        return scraperService.scrapVariants(url);
    }

    private void saveProduct(Product product, Authentication authentication) {
        productService.saveProduct(product, authentication.getPrincipal().toString());
    }

    private void checkUserAndProductValidations(Product product, Authentication authentication) {
        UserEntity user = userService.findWebUserByEmail(authentication.getPrincipal().toString());
        if (productService.existsByUserIdAndLinkAndVariant(user.getUserId(), product.getLink(), product.getVariant()))
            throw new IllegalInputException("You already have this product!", "User %s already have product.".formatted(user.getEmail()));

        if (user.isPremium())
            return;

        if (productService.countByUserId(user.getUserId()) > 15)
            throw new IllegalInputException("You are above limit of 15 products! Go premium.", "User %s above limit of 15 products.".formatted(user.getEmail()));
    }

    private String errorTemplate(String errorInfo, Model model) {
        model.addAttribute("error", true);
        model.addAttribute("errorInfo", errorInfo);
        return "fragments/home/add-item";
    }

    private String unauthorizedTemplate(String errorInfo, Model model) {
        model.addAttribute("unauthorized", true);
        model.addAttribute("errorInfo", errorInfo);
        return "fragments/home/add-item";
    }

    private String successTemplate(String successInfo, Model model) {
        model.addAttribute("success", true);
        model.addAttribute("successInfo", successInfo);
        return "fragments/home/add-item";
    }

    private String variantsTemplate(String url, List<String> variants, Model model) {
        model.addAttribute("size", true);
        model.addAttribute("variants", variants);
        model.addAttribute("url", url);
        return "fragments/home/add-item";
    }
}
