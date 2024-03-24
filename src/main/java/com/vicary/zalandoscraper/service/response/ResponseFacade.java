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


    public void saveProduct(Product product, long userId) {
        productService.saveProduct(product, userId);
    }

    public List<Product> getAllProductsByUserId(long userId) {
        return productService.getAllProductsByUserId(userId);
    }

    public void createAndSaveAwaitedMessage(String userId, String request) { //todo
        awaitedMessageService.saveAwaitedMessage(AwaitedMessageEntity.builder()
                .userId(userId)
                .request(request)
                .build());
    }

    public void deleteProduct(long productId) {
        productService.deleteProductById(productId);
    }

    public void deleteAllProductsByUserId(long userId) {
        productService.deleteAllProductsByUserId(userId);
    }

    public void updateNotifyByEmailByUserId(long userId, boolean notifyByEmail) {
        userService.updateNotifyByEmailByUserId(userId, notifyByEmail);
    }

    public boolean productExistsByUserIdAndLinkAndVariant(long userId, String link, String variant) {
        return productService.existsByUserIdAndLinkAndVariant(userId, link, variant);
    }

    public int countProductsByUserId(long userId) {
        return productService.countByUserId(userId);
    }

    public LinkRequestEntity getLinkRequestByIdAndDelete(String requestId) {
        return linkRequestService.getAndDeleteByRequestId(requestId);
    }

    public boolean updateUserToPremiumByUserId(long userId) {
        return userService.updateUserToPremiumByUserId(userId);
    }

    public boolean updateUserToStandardByUserId(long userId) {
        return userService.updateUserToStandardByUserId(userId);
    }

    public boolean updateUserToAdminByUserId(long userId) {
        return userService.updateUserToAdminByUserId(userId);
    }

    public boolean updateUserToNonAdminByUserId(long userId) {
        return userService.updateUserToNonAdminByUserId(userId);
    }

    public boolean emailVerExistsByUserIdAndToken(long userId, String token) {
        return emailVerificationService.existsByUserIdAndToken(userId, token);
    }

    public void setUserVerifiedEmail(long userId, boolean b) {
        userService.setVerifiedEmail(userId, b);
    }

    public void deleteEmailVerByToken(String token) {
        emailVerificationService.deleteByToken(token);
    }

    public String getAwaitedMessageRequestAndDelete(String userId) { //todo
        return awaitedMessageService.getRequestAndDeleteMessage(userId);
    }

    public void deleteAwaitedMessage(String userId) { //todo
        awaitedMessageService.deleteAwaitedMessage(userId);
    }

    public String getAwaitedMessageRequest(String userId) { //todo
        return awaitedMessageService.getRequest(userId);
    }

    public Product getProductById(Long productId) {
        return productService.getProduct(productId);
    }

    public void updateProductPriceAlert(Long productId, String priceAlert) {
        productService.updateProductPriceAlert(productId, priceAlert);
    }

    public void deleteEmailById(long userId) {
        userService.deleteEmailByUserId(userId);
    }

    public void updateEmailAndSendToken(long userId, String email) {
        userService.updateEmailByUserId(userId, email);

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

    public UserEntity getUser(long userId) {
        return userService.findByUserId(userId);
    }

    public boolean isUserExists(long userId) {
        return userService.existsByUserId(userId);
    }

    public boolean isUserExists(String telegramId) {
        return userService.existsByTelegramId(telegramId);
    }

    public void deleteUser(long userId) {
        userService.deleteUser(userId);
    }

    public List<UserEntity> getAllUsers() {
        return userService.findAllUsers();
    }

    public void updateUserNick(long userId, String nick) {
        userService.updateUserNick(userId, nick);
    }

    public void updateUserLanguage(long userId, String language) {
        userService.updateLanguage(userId, language);
    }

    public void checkAndSaveWaitingUser(long userId) {
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
