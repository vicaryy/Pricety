package com.vicary.pricety.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtService {

    private final static long JWT_EXPIRATION = 1000L * 60L * 60L * 24L * 31L; // one month

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
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
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
        return Keys.hmacShaKeyFor(com.vicary.pricety.configuration.SecretKey.get().getBytes(StandardCharsets.UTF_8));
    }
}