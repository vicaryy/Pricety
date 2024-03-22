package com.vicary.zalandoscraper.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtService {

    public String getUserEmail(String jwt) {
        return getClaims(jwt).getSubject();
    }

    public boolean isJwtValid(String jwt, UserDetails userDetails) {
        return userDetails.getUsername().equals(getUserEmail(jwt));
    }

    public String generateJwt(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60)))
                .signWith(getKey())
                .compact();
    }

    private Claims getClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(com.vicary.zalandoscraper.configuration.SecretKey.get().getBytes(StandardCharsets.UTF_8));
    }
}


















