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

    private final String SECRET_KEY = "b12447d19515d5b0c05d4382689019235aa539664c85686ca8e5d26e1b62263e";

    public String getUserEmail(String jwt) {
        return getClaims(jwt).getSubject();
    }

    public boolean isJwtValid(String jwt, UserDetails userDetails) {
        Date expiration = getClaims(jwt).getExpiration();
        String email = getUserEmail(jwt);

        if (expiration.before(new Date(System.currentTimeMillis())))
            return true;

        if (userDetails.getUsername().equals(email))
            return true;

        return false;

//        return expiration.before(new Date(System.currentTimeMillis())) && userDetails.getUsername().equals(email);
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
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }
}


















