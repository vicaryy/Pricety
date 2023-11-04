package com.vicary.zalandoscraper.service.entity;

import com.vicary.zalandoscraper.entity.LinkRequestEntity;
import com.vicary.zalandoscraper.repository.LinkRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
