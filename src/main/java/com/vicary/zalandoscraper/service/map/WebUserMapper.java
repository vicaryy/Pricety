package com.vicary.zalandoscraper.service.map;

import com.vicary.zalandoscraper.entity.WebUserEntity;
import com.vicary.zalandoscraper.model.RegisterModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebUserMapper {

    private final PasswordEncoder passwordEncoder;

    public WebUserEntity map(RegisterModel model) {
        return WebUserEntity.builder()
                .email(model.getEmail())
                .password(passwordEncoder.encode(model.getPassword()))
                .role("USER")
                .activated(false)
                .build();
    }
}
