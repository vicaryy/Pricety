package com.vicary.zalandoscraper.controller;

import com.vicary.zalandoscraper.entity.DataImportEntity;
import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.model.ProductTemplate;
import com.vicary.zalandoscraper.service.repository_services.DataImportService;
import com.vicary.zalandoscraper.service.repository_services.ProductService;
import com.vicary.zalandoscraper.service.repository_services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final ProductService productService;
    private final UserService userService;
    private final DataImportService dataImportService;

    @GetMapping("/account")
    public String account(Model model, Authentication authentication, @RequestParam(name = "settings", required = false) boolean settings) {
        String userEmail = authentication.getPrincipal().toString();
        UserEntity user = userService.findWebUserByEmail(userEmail);
        List<ProductTemplate> products = productService.getTemplatesByUserEmail(userEmail);
        if (settings)
            model.addAttribute("settings", true);
        model.addAttribute("user", user);
        model.addAttribute("products", products);
        model.addAttribute("telegram", user.isTelegram());
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
    public String getDataFromTelegram(Authentication authentication, Model model) throws InterruptedException {
        Thread.sleep(1000);
        String email = authentication.getPrincipal().toString();
        if (userService.findWebUserByEmail(email).isTelegram())
            return "empty";

        if (userService.existsByVerifiedEmailAndTelegram(email, true)) {
            userService.updateWebUserByTelegram(email);
            model.addAttribute("telegram", false);
            model.addAttribute("done", true);
            return "fragments/account/telegram-info :: get-telegram";
        }

        Optional<DataImportEntity> importEntity = dataImportService.findByEmailAndMethod(email, "get");
        if (importEntity.isEmpty())
            importEntity = Optional.of(dataImportService.generateAndSave(email, "get"));
        model.addAttribute("telegram", false);
        model.addAttribute("getCode", importEntity.get().getRequest());
        return "fragments/account/telegram-info :: get-telegram";
    }

    @PatchMapping("/account/telegramSend")
    public String sendDataToTelegram(Authentication authentication, Model model) throws InterruptedException {
        Thread.sleep(1000);
        String email = authentication.getPrincipal().toString();
        if (userService.findWebUserByEmail(email).isTelegram())
            return "empty";

        if (userService.existsByVerifiedEmailAndTelegram(email, true)) {
            userService.updateTelegramByWebUser(email);
            model.addAttribute("telegram", false);
            model.addAttribute("done", true);
            return "fragments/account/telegram-info :: get-telegram";
        }
        Optional<DataImportEntity> importEntity = dataImportService.findByEmailAndMethod(email, "send");
        if (importEntity.isEmpty())
            importEntity = Optional.of(dataImportService.generateAndSave(email, "send"));
        model.addAttribute("telegram", false);
        model.addAttribute("sendCode", importEntity.get().getRequest());
        return "fragments/account/telegram-info :: send-telegram";
    }

    @PatchMapping("/account/emailNotifications")
    public String setEmailNotifications(@RequestParam(name = "n") boolean notifications, Authentication authentication) {
        userService.updateNotifyByEmailByEmail(authentication.getPrincipal().toString(), notifications);
        return "empty";
    }


    private boolean doesUserHaveProduct(long productId, Authentication authentication) {
        return userService.findWebUserByEmail(authentication.getPrincipal().toString())
                .getProducts()
                .stream()
                .anyMatch(e -> e.getId() == productId);
    }
}
