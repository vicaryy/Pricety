package com.vicary.zalandoscraper.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private long userId;
    private String telegramId;
    private String email;
    private String nick;
    private String language;
    private boolean premium;
    private boolean admin;
    private boolean notifyByEmail;
    private boolean website;
    private boolean telegram;
}
