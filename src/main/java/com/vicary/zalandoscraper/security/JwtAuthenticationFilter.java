package com.vicary.zalandoscraper.security;

import com.vicary.zalandoscraper.service.repository_services.WebUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final static Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService = new JwtService();

    private final WebUserService webUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        if (dontNeedsToBeFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        logger.info("Wszedłem tutaj");

        String jwt = getJwtFromCookies(request.getCookies());
        if (jwt.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String userEmail;
        try {
            userEmail = jwtService.getUserEmail(jwt);
        } catch (Exception ex) {
            logger.warn("Unauthenticated jwt: %s\nFrom IP: %s".formatted(jwt, request.getRemoteAddr()));
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails user = webUserService.getByEmail(userEmail).orElseThrow();

        if (!jwtService.isJwtValid(jwt, user)) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);

        logger.info("User authenticated!");
        filterChain.doFilter(request, response);
    }

    private String getJwtFromCookies(Cookie[] cookies) {
        for (Cookie c : cookies)
            if (c.getName().equals("access_key"))
                return c.getValue();
        return "";
    }

    private boolean dontNeedsToBeFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/join") || uri.startsWith("/assets") || request.getCookies() == null;
    }
}
