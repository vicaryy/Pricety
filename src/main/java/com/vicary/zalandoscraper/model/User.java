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
    private String userId;
    private String email;
    private String nick;
    private String language;
    private boolean premium;
    private boolean admin;
    private boolean notifyByEmail;
}
