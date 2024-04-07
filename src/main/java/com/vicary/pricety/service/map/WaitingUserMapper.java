package com.vicary.pricety.service.map;

import com.vicary.pricety.entity.UserEntity;
import com.vicary.pricety.entity.WaitingUserEntity;
import com.vicary.pricety.service.dto.WaitingUserDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WaitingUserMapper {

    public WaitingUserDTO map(WaitingUserEntity w) {
        return WaitingUserDTO.builder()
                .id(w.getId())
                .userId(w.getUser().getUserId())
                .build();
    }

    public List<WaitingUserDTO> map(List<WaitingUserEntity> w) {
        return w.stream()
                .map(this::map)
                .toList();
    }

    public WaitingUserEntity mapFromDTO(WaitingUserDTO w, UserEntity user) {
        return WaitingUserEntity.builder()
                .id(w.getId())
                .user(user)
                .build();
    }
}
