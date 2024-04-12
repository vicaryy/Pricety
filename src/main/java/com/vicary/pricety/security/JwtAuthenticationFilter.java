package com.vicary.pricety.security;

import com.vicary.pricety.configuration.SecretKey;
import com.vicary.pricety.service.repository_services.UserService;
import com.vicary.pricety.thread_local.ActiveLanguage;
import com.vicary.pricety.utils.Cookies;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final static Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;

    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        setLanguage();

        if (hasSecretKey(request)) {
            setAsAdmin();
            filterChain.doFilter(request, response);
            return;
        }

        if (dontNeedsToBeFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = Cookies.getJwtFromCookies(request.getCookies());
        if (jwt.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String userEmail;
        try {
            userEmail = jwtService.getUserEmail(jwt);
        } catch (Exception ex) {
            logger.warn("Unauthenticated jwt: %s\nFrom IP: %s".formatted(jwt, request.getRemoteAddr()));
            response.addCookie(Cookies.getEmptyJwtCookie());
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails user = userService.findWebUserByEmail(userEmail);

        if (!jwtService.isJwtValid(jwt, user)) {
            response.addCookie(Cookies.getEmptyJwtCookie());
            filterChain.doFilter(request, response);
            return;
        }

        var token = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);

        filterChain.doFilter(request, response);
    }

    private void setAsAdmin() {
        UsernamePasswordAuthenticationToken u = new UsernamePasswordAuthenticationToken("admin", null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(u);
    }

    private boolean hasSecretKey(HttpServletRequest request) {
        String secretKey = request.getHeader("secretKey");
        if (secretKey == null || secretKey.isBlank())
            return false;

        return secretKey.equals(SecretKey.get());
    }

    private boolean dontNeedsToBeFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/assets") || request.getCookies() == null;
    }

    private void setLanguage() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", Locale.of("en"));
        ActiveLanguage.get().setResourceBundle(resourceBundle);
    }
}
