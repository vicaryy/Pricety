package com.vicary.pricety.controller.database_import;

import com.vicary.pricety.entity.*;
import com.vicary.pricety.model.UserDTO;
import com.vicary.pricety.repository.*;
import com.vicary.pricety.service.dto.MessageDTO;
import com.vicary.pricety.service.dto.ProductDTO;
import com.vicary.pricety.service.dto.WaitingUserDTO;
import com.vicary.pricety.service.map.MessageMapper;
import com.vicary.pricety.service.map.ProductMapper;
import com.vicary.pricety.service.map.UserMapper;
import com.vicary.pricety.service.map.WaitingUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/set/")
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


    @PostMapping("users")
    public ResponseEntity<String> setUsers(@RequestBody List<UserDTO> users) {
        this.users = userMapper.mapToEntityWithEmptyProducts(users);
        return save(this.users, userRepository, "users");
    }

    @PostMapping("products")
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

    @PostMapping("activeRequests")
    public ResponseEntity<String> setActiveRequests(@RequestBody List<ActiveRequestEntity> activeRequestEntities) {
        return save(activeRequestEntities, activeRequestRepository, "activeRequests");
    }

    @PostMapping("awaitedMessages")
    public ResponseEntity<String> setAwaitedMessages(@RequestBody List<AwaitedMessageEntity> awaitedMessageEntities) {
        return save(awaitedMessageEntities, awaitedMessageRepository, "awaitedMessages");
    }

    @PostMapping("dataImports")
    public ResponseEntity<String> setDataImports(@RequestBody List<DataImportEntity> dataImportEntities) {
        return save(dataImportEntities, dataImportRepository, "dataImports");
    }

    @PostMapping("emailVerifications")
    public ResponseEntity<String> setEmailVerifications(@RequestBody List<EmailVerificationEntity> emailVerificationEntities) {
        return save(emailVerificationEntities, emailVerificationRepository, "emailVerifications");
    }

    @PostMapping("linkRequests")
    public ResponseEntity<String> setLinkRequests(@RequestBody List<LinkRequestEntity> linkRequestEntities) {
        return save(linkRequestEntities, linkRequestRepository, "linkRequests");
    }


    @PostMapping("messages")
    public ResponseEntity<String> setMessages(@RequestBody List<MessageDTO> messageDTOS) {
        List<MessageEntity> messageEntities = new ArrayList<>();
        messageDTOS.forEach(e -> messageEntities.add(messageMapper.mapFromDTO(
                e, this.users.stream()
                        .filter(u -> u.getUserId() == e.getUserId())
                        .findAny()
                        .orElseThrow())));
        return save(messageEntities, messageRepository, "messages");
    }


    @PostMapping("notificationChats")
    public ResponseEntity<String> setNotificationChats(@RequestBody List<NotificationChatEntity> notificationChatEntities) {
        return save(notificationChatEntities, notificationChatRepository, "notificationChats");
    }

    @PostMapping("notificationEmails")
    public ResponseEntity<String> setNotificationEmails(@RequestBody List<NotificationEmailEntity> notificationEmailEntities) {
        return save(notificationEmailEntities, notificationEmailRepository, "notificationEmails");
    }

    @PostMapping("productHistories")
    public ResponseEntity<String> setProductHistories(@RequestBody List<ProductHistoryEntity> productHistoryEntities) {
        return save(productHistoryEntities, productHistoryRepository, "productHistories");
    }


    @PostMapping("waitingUsers")
    public ResponseEntity<String> setWaitingUsers(@RequestBody List<WaitingUserDTO> waitingUserDTOS) {
        List<WaitingUserEntity> waitingUserEntities = new ArrayList<>();
        waitingUserDTOS.forEach(e -> waitingUserEntities.add(waitingUserMapper.mapFromDTO(
                e, this.users.stream()
                        .filter(u -> u.getUserId() == e.getUserId())
                        .findAny().orElseThrow())));
        return save(waitingUserEntities, waitingUserRepository, "waitingUsers");
    }


    private synchronized <S> ResponseEntity<String> save(List<S> objects, JpaRepository<S, ?> repository, String objectName) {
        if (set == 1)
            log.info("Starting setting objects in repository...");

        if (done)
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("Data is set already.");
        log.info("({}/{}) Setting {} {} to repository...", set++, AMOUNT, objects.size(), objectName);

        if (set > AMOUNT)
            done = true;
        try {
            long time = System.currentTimeMillis();
            if (objects.size() > 200) {
                List<List<S>> lists = partitionList(objects, 200);
                lists.forEach(e -> saveAll(e, repository));
            } else {
                saveAll(objects, repository);
            }
            log.info("It takes: {}ms", (System.currentTimeMillis() - time));
        } catch (Exception ex) {
            log.error("Error in setting {} to repository.", objectName);
            ex.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + ex.getMessage());
        }
        return ResponseEntity.ok("Success");
    }


    @Transactional
    private <S> void saveAll(List<S> objects, JpaRepository<S, ?> repository) {
        repository.saveAll(objects);
    }

    private <T> List<List<T>> partitionList(List<T> list, int partitionSize) {
        return IntStream.range(0, (list.size() + partitionSize - 1) / partitionSize)
                .mapToObj(i -> list.subList(i * partitionSize, Math.min((i + 1) * partitionSize, list.size())))
                .collect(Collectors.toList());
    }
}
