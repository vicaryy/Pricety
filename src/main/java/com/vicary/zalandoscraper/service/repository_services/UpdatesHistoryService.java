package com.vicary.zalandoscraper.service.repository_services;

import com.vicary.zalandoscraper.entity.UpdateHistoryEntity;
import com.vicary.zalandoscraper.repository.UpdateHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdatesHistoryService {

    private final static Logger logger = LoggerFactory.getLogger(UpdatesHistoryService.class);

    private final UpdateHistoryRepository repository;


    public void saveUpdate(UpdateHistoryEntity updateHistoryEntity) {
        repository.save(updateHistoryEntity);
    }

    public void saveUpdates(List<UpdateHistoryEntity> updateHistoryEntityList) {
        for (UpdateHistoryEntity u : updateHistoryEntityList)
            saveUpdate(u);

        logger.info("[Product Updater] Added {} updates history to database.", updateHistoryEntityList.size());
    }

    public LocalDateTime getLastUpdateTime() {
        return repository.getLatestUpdate().getLastUpdate();
    }
}
