package com.vicary.zalandoscraper.service.entity;

import com.vicary.zalandoscraper.entity.LinkRequestEntity;
import com.vicary.zalandoscraper.exception.ZalandoScraperBotException;
import com.vicary.zalandoscraper.repository.LinkRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
