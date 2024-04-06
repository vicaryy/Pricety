package com.vicary.zalandoscraper.controller;

import com.vicary.zalandoscraper.entity.*;
import com.vicary.zalandoscraper.model.UserDTO;
import com.vicary.zalandoscraper.repository.*;
import com.vicary.zalandoscraper.service.dto.MessageDTO;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.service.dto.WaitingUserDTO;
import com.vicary.zalandoscraper.service.map.MessageMapper;
import com.vicary.zalandoscraper.service.map.ProductMapper;
import com.vicary.zalandoscraper.service.map.UserMapper;
import com.vicary.zalandoscraper.service.map.WaitingUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@RestController
@Slf4j
public class DatabaseSetterController {
    private final static int AMOUNT = 12;
    private int set = 1;
    public static boolean done;

    private List<UserEntity> users;

    // UPEWNIJ SIĘ, ŻE JESTEŚ PODŁĄCZONY POD WŁAŚCIWĄ BAZĘ DANYCH

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ActiveRequestRepository activeRequestRepository;
    private final AwaitedMessageRepository awaitedMessageRepository;
    private final DataImportRepository dataImportRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final LinkRequestRepository linkRequestRepository;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final NotificationChatRepository notificationChatRepository;
    private final NotificationEmailRepository notificationEmailRepository;
    private final ProductHistoryRepository productHistoryRepository;
    private final WaitingUserRepository waitingUserRepository;
    private final WaitingUserMapper waitingUserMapper;


    @PostMapping(value = "/api/set/users")
    public ResponseEntity<String> setUsers(@RequestBody List<UserDTO> users) {
        this.users = userMapper.mapToEntityWithEmptyProducts(users);
        return save(this.users, userRepository, "users");
    }

    @PostMapping(value = "/api/set/products")
    public ResponseEntity<String> setProducts(@RequestBody List<ProductDTO> products) {
        List<ProductEntity> productEntities = new ArrayList<>();
        products.forEach(e -> productEntities.add(productMapper.mapFromDTO(
                e,
                this.users.stream()
                        .filter(u -> u.getUserId() == e.getUserId())
                        .findAny()
                        .orElseThrow())));
        return save(productEntities, productRepository, "products");
    }

    @PostMapping(value = "/api/set/activeRequests")
    public ResponseEntity<String> setActiveRequests(@RequestBody List<ActiveRequestEntity> activeRequestEntities) {
        return save(activeRequestEntities, activeRequestRepository, "activeRequests");
    }

    @PostMapping(value = "/api/set/awaitedMessages")
    public ResponseEntity<String> setAwaitedMessages(@RequestBody List<AwaitedMessageEntity> awaitedMessageEntities) {
        return save(awaitedMessageEntities, awaitedMessageRepository, "awaitedMessages");
    }

    @PostMapping(value = "/api/set/dataImports")
    public ResponseEntity<String> setDataImports(@RequestBody List<DataImportEntity> dataImportEntities) {
        return save(dataImportEntities, dataImportRepository, "dataImports");
    }

    @PostMapping(value = "/api/set/emailVerifications")
    public ResponseEntity<String> setEmailVerifications(@RequestBody List<EmailVerificationEntity> emailVerificationEntities) {
        return save(emailVerificationEntities, emailVerificationRepository, "emailVerifications");
    }

    @PostMapping(value = "/api/set/linkRequests")
    public ResponseEntity<String> setLinkRequests(@RequestBody List<LinkRequestEntity> linkRequestEntities) {
        return save(linkRequestEntities, linkRequestRepository, "linkRequests");
    }


    @PostMapping(value = "/api/set/messages")
    public ResponseEntity<String> setMessages(@RequestBody List<MessageDTO> messageDTOS) {
        List<MessageEntity> messageEntities = new ArrayList<>();
        messageDTOS.forEach(e -> messageEntities.add(messageMapper.mapFromDTO(
                e, this.users.stream()
                        .filter(u -> u.getUserId() == e.getUserId())
                        .findAny()
                        .orElseThrow())));
        return save(messageEntities, messageRepository, "messages");
    }


    @PostMapping(value = "/api/set/notificationChats")
    public ResponseEntity<String> setNotificationChats(@RequestBody List<NotificationChatEntity> notificationChatEntities) {
        return save(notificationChatEntities, notificationChatRepository, "notificationChats");
    }

    @PostMapping(value = "/api/set/notificationEmails")
    public ResponseEntity<String> setNotificationEmails(@RequestBody List<NotificationEmailEntity> notificationEmailEntities) {
        return save(notificationEmailEntities, notificationEmailRepository, "notificationEmails");
    }

    @PostMapping(value = "/api/set/productHistories")
    public ResponseEntity<String> setProductHistories(@RequestBody List<ProductHistoryEntity> productHistoryEntities) {
        return save(productHistoryEntities, productHistoryRepository, "productHistories");
    }


    @PostMapping(value = "/api/set/waitingUsers")
    public ResponseEntity<String> setWaitingUsers(@RequestBody List<WaitingUserDTO> waitingUserDTOS) {
        List<WaitingUserEntity> waitingUserEntities = new ArrayList<>();
        waitingUserDTOS.forEach(e -> waitingUserEntities.add(waitingUserMapper.mapFromDTO(
                e, this.users.stream()
                        .filter(u -> u.getUserId() == e.getUserId())
                        .findAny().orElseThrow())));
        return save(waitingUserEntities, waitingUserRepository, "waitingUsers");
    }


    private <S> ResponseEntity<String> save(List<S> objects, JpaRepository<S, ?> repository, String objectName) {
        if (set == 1)
            log.info("Starting setting objects in repository...");

        if (done)
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("Data is set already.");
        log.info("({},{}) Setting '{}' {} to repository...", set++, AMOUNT, objects.size(), objectName);

        if (set >= AMOUNT)
            done = true;

        try {
            objects.forEach(repository::save);
        } catch (Exception ex) {
            log.error("Error in setting {} to repository.", objectName);
            ex.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + ex.getMessage());
        }
        return ResponseEntity.ok("Success");
    }
}
