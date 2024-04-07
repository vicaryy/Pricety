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
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/get/")
public class DatabaseSenderController {

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


    @GetMapping("users")
    public ResponseEntity<List<UserDTO>> sendUsers() {
        return ResponseEntity.ok(userMapper.mapToDTO(userRepository.findAll(Sort.by("userId"))));
    }

    @GetMapping("products")
    public ResponseEntity<List<ProductDTO>> sendProducts() {
        return ResponseEntity.ok(productMapper.mapToDTO(productRepository.findAll(Sort.by("id"))));
    }

    @GetMapping("activeRequests")
    public ResponseEntity<List<ActiveRequestEntity>> sendActiveRequests() {
        return ResponseEntity.ok(activeRequestRepository.findAll(Sort.by("id")));
    }

    @GetMapping("awaitedMessages")
    public ResponseEntity<List<AwaitedMessageEntity>> sendAwaitedMessages() {
        return ResponseEntity.ok(awaitedMessageRepository.findAll(Sort.by("id")));
    }

    @GetMapping("dataImports")
    public ResponseEntity<List<DataImportEntity>> sendDataImports() {
        return ResponseEntity.ok(dataImportRepository.findAll(Sort.by("id")));
    }

    @GetMapping("emailVerifications")
    public ResponseEntity<List<EmailVerificationEntity>> sendEmailVerifications() {
        return ResponseEntity.ok(emailVerificationRepository.findAll(Sort.by("id")));
    }

    @GetMapping("linkRequests")
    public ResponseEntity<List<LinkRequestEntity>> sendLinkRequests() {
        return ResponseEntity.ok(linkRequestRepository.findAll(Sort.by("id")));
    }

    @GetMapping("messages")
    public ResponseEntity<List<MessageDTO>> sendMessages() {
        return ResponseEntity.ok(messageMapper.mapToDTO(messageRepository.findAll(Sort.by("id"))));
    }

    @GetMapping("notificationChats")
    public ResponseEntity<List<NotificationChatEntity>> sendNotificationChats() {
        return ResponseEntity.ok(notificationChatRepository.findAll(Sort.by("id")));
    }

    @GetMapping("notificationEmails")
    public ResponseEntity<List<NotificationEmailEntity>> sendNotificationEmails() {
        return ResponseEntity.ok(notificationEmailRepository.findAll(Sort.by("id")));
    }

    @GetMapping("productHistories")
    public ResponseEntity<List<ProductHistoryEntity>> sendProductHistories() {
        return ResponseEntity.ok(productHistoryRepository.findAll(Sort.by("id")));
    }

    @GetMapping("waitingUsers")
    public ResponseEntity<List<WaitingUserDTO>> sendWaitingUsers() {
        return ResponseEntity.ok(waitingUserMapper.map(waitingUserRepository.findAll(Sort.by("id"))));
    }
}
