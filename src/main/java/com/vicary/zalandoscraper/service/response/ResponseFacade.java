package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.entity.WaitingUserEntity;
import com.vicary.zalandoscraper.format.MarkdownV2;
import com.vicary.zalandoscraper.service.dto.ProductHistoryDTO;
import com.vicary.zalandoscraper.utils.PrettyTime;
import com.vicary.zalandoscraper.entity.AwaitedMessageEntity;
import com.vicary.zalandoscraper.entity.LinkRequestEntity;
import com.vicary.zalandoscraper.model.Product;
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

    private final ProductHistoryService productHistoryService;

    private final ActiveRequestService activeRequestService;

    private final WaitingUserService waitingUserService;


    public void saveProduct(Product product, String userId) {
        productService.saveProduct(product, userId);
    }

    public List<Product> getAllProductsByUserId(String userId) {
        return productService.getAllProductsByTelegramId(userId);
    }

    public void createAndSaveAwaitedMessage(String userId, String request) {
        awaitedMessageService.saveAwaitedMessage(AwaitedMessageEntity.builder()
                .userId(userId)
                .request(request)
                .build());
    }

    public void deleteProduct(long productId) {
        productService.deleteProductById(productId);
    }

    public void deleteAllProductsByTelegramId(String telegramId) {
        productService.deleteAllProductsByTelegramId(telegramId);
    }

    public void updateNotifyByEmailByTelegramId(String telegramId, boolean notifyByEmail) {
        userService.updateNotifyByEmailByTelegramId(telegramId, notifyByEmail);
    }

    public boolean productExistsByTelegramIdAndLinkAndVariant(String telegramId, String link, String variant) {
        return productService.existsByTelegramIdAndLinkAndVariant(telegramId, link, variant);
    }

    public int countProductsByTelegramIdId(String telegramId) {
        return productService.countByTelegramId(telegramId);
    }

    public LinkRequestEntity getLinkRequestByIdAndDelete(String requestId) {
        return linkRequestService.getAndDeleteByRequestId(requestId);
    }

    public boolean updateUserToPremiumByTelegramId(String userId) {
        return userService.updateUserToPremiumByTelegramId(userId);
    }

    public boolean updateUserToStandardByTelegramId(String userId) {
        return userService.updateUserToStandardByTelegramId(userId);
    }

    public boolean updateUserToAdminByTelegramId(String userId) {
        return userService.updateUserToAdminByTelegramId(userId);
    }

    public boolean updateUserToNonAdminByTelegramId(String userId) {
        return userService.updateUserToNonAdminByTelegramId(userId);
    }

    public boolean emailVerExistsByTelegramIdAndToken(String userId, String token) {
        return emailVerificationService.existsByUserIdAndToken(userId, token);
    }

    public void setUserVerifiedEmail(String userId, boolean b) {
        userService.setVerifiedEmail(userId, b);
    }

    public void deleteEmailVerByToken(String token) {
        emailVerificationService.deleteByToken(token);
    }

    public String getAwaitedMessageRequestAndDelete(String userId) {
        return awaitedMessageService.getRequestAndDeleteMessage(userId);
    }

    public void deleteAwaitedMessage(String userId) {
        awaitedMessageService.deleteAwaitedMessage(userId);
    }

    public String getAwaitedMessageRequest(String userId) {
        return awaitedMessageService.getRequest(userId);
    }

    public Product getProductById(Long productId) {
        return productService.getProduct(productId);
    }

    public void updateProductPriceAlert(Long productId, String priceAlert) {
        productService.updateProductPriceAlert(productId, priceAlert);
    }

    public void deleteEmailById(String userId) {
        userService.deleteEmailByTelegramId(userId);
    }

    public void updateEmailAndSendToken(String userId, String email) {
        userService.updateEmailByTelegramId(userId, email);

        if (emailVerificationService.existsByUserId(userId))
            emailVerificationService.deleteAllByUserId(userId);

        var verification = emailVerificationService.createVerification(userId);

        emailVerificationService.sendTokenToUser(verification, email);
    }

    public String getLastUpdateTime() {
        return MarkdownV2.apply(PrettyTime.getAgo(productHistoryService.getLastUpdateTime())).toItalic().get();
    }

    public String generateAndSaveRequest(String link) {
        return linkRequestService.generateAndSaveRequest(link);
    }

    public void deleteActiveRequestById(String userId) {
        activeRequestService.deleteByUserId(userId);
    }

    public void deleteAllActiveRequests() {
        activeRequestService.deleteAllActiveUsers();
    }

    public UserEntity getUser(String userId) {
        return userService.findByTelegramId(userId);
    }
    public boolean isUserExists(String userId) {
        return userService.existsByTelegramId(userId);
    }

    public void deleteUser(String userId) {
        userService.deleteUser(userId);
    }

    public List<UserEntity> getAllUsers() {
        return userService.findAllUsers();
    }

    public void updateUserNick(String userId, String nick) {
        userService.updateUserNick(userId, nick);
    }

    public void updateUserLanguage(String userId, String language) {
        userService.updateLanguage(userId, language);
    }

    public void checkAndSaveWaitingUser(String userId) {
        if (!waitingUserService.existsByUserId(getUser(userId)))
            waitingUserService.saveWaitingUser(new WaitingUserEntity(getUser(userId)));
    }

    public List<Product> getAllProducts() {
        return productService.getAllProductsSortById();
    }

    public boolean isProductExists(String productId) {
        long id;
        try {
            id = Long.parseLong(productId);
        } catch (Exception ex) {
            return false;
        }
        return productService.existsById(id);
    }

    public List<ProductHistoryDTO> getAllReducedProductHistory(long productId) {
        return productHistoryService.getReducedProductHistory(productId);
    }
}
