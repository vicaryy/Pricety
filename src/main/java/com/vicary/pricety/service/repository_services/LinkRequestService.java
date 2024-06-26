package com.vicary.pricety.service.repository_services;

import com.vicary.pricety.entity.LinkRequestEntity;
import com.vicary.pricety.exception.InvalidLinkException;
import com.vicary.pricety.messages.Messages;
import com.vicary.pricety.repository.LinkRequestRepository;
import com.vicary.pricety.thread_local.ActiveUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class LinkRequestService {

    private final LinkRequestRepository repository;

    public void saveRequest(LinkRequestEntity entity) {
        repository.save(entity);
    }

    @Transactional
    public LinkRequestEntity getAndDeleteByRequestId(String requestId) {
        LinkRequestEntity linkRequest = repository.findByRequestId(requestId)
                .orElseThrow(() -> new InvalidLinkException(Messages.other("sessionExpired"), "User '%s' session expired".formatted(ActiveUser.get().getTelegramId())));
        repository.deleteByRequestId(requestId);
        checkExpiration(linkRequest.getExpiration());
        return linkRequest;
    }

    private void checkExpiration(long expiration) {
        if (System.currentTimeMillis() > expiration)
            throw new InvalidLinkException(Messages.other("sessionExpired"), "User '%s' session expired".formatted(ActiveUser.get().getTelegramId()));
    }

    public String generateAndSaveRequest(String link) {
        String requestId = generateRequestId();
        long fiveMinutes = 1000 * 60 * 5;
        LinkRequestEntity entity = LinkRequestEntity.builder()
                .requestId(requestId)
                .link(link)
                .expiration(System.currentTimeMillis() + fiveMinutes)
                .build();
        saveRequest(entity);
        return requestId;
    }

    private String generateRequestId() {
        StringBuilder sb = new StringBuilder();
        IntStream intStream = ThreadLocalRandom.current().ints(10, 0, 10);
        intStream.forEach(sb::append);
        return sb.toString();
    }
}
