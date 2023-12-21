package com.vicary.zalandoscraper.service.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProductHistoryDTO {
    private double price;
    private LocalDateTime updateTime;
}
