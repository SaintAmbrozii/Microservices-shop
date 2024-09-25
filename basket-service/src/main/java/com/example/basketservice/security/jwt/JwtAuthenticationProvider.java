package com.example.basketservice.security.jwt;

import com.example.basketservice.security.AuthException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationProvider.class);

    private final JwtUtil jwtUtil;

    @SuppressWarnings("unused")
    public JwtAuthenticationProvider(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            String token = (String) authentication.getCredentials();
            String username = jwtUtil.getUsernameFromToken(token);
            Long id = jwtUtil.getUserIdFromToken(token);

            return new JwtAuthentificationProfile(username, id);

        } catch (JwtException ex) {
            log.error(String.format("Invalid JWT Token: %s", ex.getMessage()));
            throw new AuthException("Failed to verify token");
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
