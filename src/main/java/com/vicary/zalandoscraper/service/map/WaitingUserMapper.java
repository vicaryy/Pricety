package com.vicary.zalandoscraper.service.map;

import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.entity.WaitingUserEntity;
import com.vicary.zalandoscraper.service.dto.WaitingUserDTO;
import org.springframework.stereotype.Component;

@Component
public class WaitingUserMapper {

    public WaitingUserEntity mapFromDTO(WaitingUserDTO w, UserEntity user) {
        return WaitingUserEntity.builder()
                .id(w.getId())
                .user(user)
                .build();
    }
}
