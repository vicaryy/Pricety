package com.vicary.pricety.service.map;

import com.vicary.pricety.entity.MessageEntity;
import com.vicary.pricety.entity.UserEntity;
import com.vicary.pricety.service.dto.MessageDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MessageMapper {

    public MessageEntity mapFromDTO(MessageDTO m, UserEntity user) {
        return MessageEntity.builder()
                .id(m.getId())
                .message(m.getMessage())
                .sentDate(m.getSentDate())
                .user(user)
                .build();
    }

    public MessageEntity map(UserEntity user, String text) {
        return MessageEntity.builder()
                .user(user)
                .message(text)
                .sentDate(LocalDateTime.now())
                .build();
    }

    public MessageDTO mapToDTO(MessageEntity m) {
        return MessageDTO.builder()
                .id(m.getId())
                .userId(m.getUser().getUserId())
                .message(m.getMessage())
                .sentDate(m.getSentDate())
                .build();
    }

    public List<MessageDTO> mapToDTO(List<MessageEntity> m) {
        return m.stream()
                .map(this::mapToDTO)
                .toList();
    }
}
