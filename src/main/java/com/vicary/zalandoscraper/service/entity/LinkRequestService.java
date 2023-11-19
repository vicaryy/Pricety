package com.vicary.zalandoscraper.service.entity;

import com.vicary.zalandoscraper.entity.LinkRequestEntity;
import com.vicary.zalandoscraper.repository.LinkRequestRepository;
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

    public LinkRequestEntity findByRequestId(String requestId) {
        return repository.findByRequestId(requestId).orElse(null);
    }

    @Transactional
    public LinkRequestEntity findByRequestIdAndDelete(String requestId) {
        LinkRequestEntity linkRequest = findByRequestId(requestId);
        deleteByRequestId(linkRequest.getRequestId());
        return linkRequest;
    }

    public void deleteByRequestId(String requestId) {
        repository.deleteByRequestId(requestId);
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
