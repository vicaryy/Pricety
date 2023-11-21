package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.format.MarkdownV2;
import com.vicary.zalandoscraper.utils.PrettyTime;
import com.vicary.zalandoscraper.entity.AwaitedMessageEntity;
import com.vicary.zalandoscraper.entity.LinkRequestEntity;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.service.repository_services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponseFacade {

    private final LinkRequestService linkRequestService;

    private final ProductService productService;

    private final AwaitedMessageService awaitedMessageService;

    private final UserService userService;

    private final EmailVerificationService emailVerificationService;

    private final UpdatesHistoryService updatesHistoryService;

    private final ActiveRequestService activeRequestService;


    public void saveProduct(Product product) {
        productService.saveProduct(product);
    }

    public List<ProductDTO> getAllProductsByUserId(String userId) {
        return productService.getAllProductsDtoByUserId(userId);
    }

    public void saveAwaitedMessage(AwaitedMessageEntity awaitedMessageEntity) {
        awaitedMessageService.saveAwaitedMessage(awaitedMessageEntity);
    }

    public void deleteProductById(long productId) {
        productService.deleteProductById(productId);
    }

    public void deleteAllProductsByUserId(String userId) {
        productService.deleteAllProductsByUserId(userId);
    }

    public void updateLanguageByUserId(String userId, String language) {
        userService.updateLanguage(userId, language);
    }

    public void updateNotifyByEmailById(String userId, boolean notifyByEmail) {
        userService.updateNotifyByEmailById(userId, notifyByEmail);
    }

    public boolean productExistsByUserIdAndLinkAndVariant(String chatId, String link, String variant) {
        return productService.existsByUserIdAndLinkAndVariant(chatId, link, variant);
    }

    public int countProductsByUserId(String userId) {
        return productService.countByUserId(userId);
    }

    public LinkRequestEntity getLinkRequestByIdAndDelete(String requestId) {
        return linkRequestService.findByRequestIdAndDelete(requestId);
    }

    public boolean updateUserToPremiumByNick(String userNick) {
        return userService.updateUserToPremiumByNick(userNick);
    }

    public boolean updateUserToStandardByNick(String userNick) {
        return userService.updateUserToStandardByNick(userNick);
    }

    public boolean updateUserToAdminByNick(String userNick) {
        return userService.updateUserToAdminByNick(userNick);
    }

    public boolean updateUserToNonAdminByNick(String userNick) {
        return userService.updateUserToNonAdminByNick(userNick);
    }

    public boolean emailVerExistsByUserIdAndToken(String userId, String token) {
        return emailVerificationService.existsByUserIdAndToken(userId, token);
    }

    public void setUserVerifiedEmail(String userId, boolean b) {
        userService.setVerifiedEmail(userId, b);
    }

    public void deleteEmailVerByToken(String token) {
        emailVerificationService.deleteByToken(token);
    }

    public String getAwaitedMessageRequestByUserIdAndDelete(String userId) {
        return awaitedMessageService.getRequestAndDeleteMessage(userId);
    }

    public ProductDTO getProductDTOById(Long productId) {
        return productService.getProductDTOById(productId);
    }

    public void updateProductPriceAlert(Long productId, String priceAlert) {
        productService.updateProductPriceAlert(productId, priceAlert);
    }

    public void deleteEmailById(String userId) {
        userService.deleteEmailById(userId);
    }

    public void updateEmailAndSendToken(String userId, String email) {
        userService.updateEmailById(userId, email);

        if (emailVerificationService.existsByUserId(userId))
            emailVerificationService.deleteAllByUserId(userId);

        var verification = emailVerificationService.createVerification(userId);

        emailVerificationService.sendTokenToUser(verification, email);
    }

    public String getLastUpdateTime() {
        return MarkdownV2.apply(PrettyTime.get(updatesHistoryService.getLastUpdateTime())).toItalic().get();
    }

    public String generateAndSaveRequest(String link) {
        return linkRequestService.generateAndSaveRequest(link);
    }

    public void deleteActiveRequestById(String userId) {
        activeRequestService.deleteByUserId(userId);
    }
}
