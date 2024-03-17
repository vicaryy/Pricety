package com.vicary.zalandoscraper.service.repository_services;

import com.vicary.zalandoscraper.entity.WebUserEntity;
import com.vicary.zalandoscraper.model.RegisterModel;
import com.vicary.zalandoscraper.repository.WebUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebUserService {

    private final WebUserRepository repository;


    public void registerUser(WebUserEntity entity) {
        repository.save(entity);
    }

    public boolean isProvidedDataValid(RegisterModel model) {
        return true;
    }
}
