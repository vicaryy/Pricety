package com.vicary.zalandoscraper.model;

import com.vicary.zalandoscraper.security.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private long userId;
    private String telegramId;
    private String email;
    private String password;
    private String nick;
    private String nationality;
    private boolean premium;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean admin;
    private boolean emailNotifications;
    private boolean verifiedEmail;
    private boolean website;
    private boolean telegram;
}
