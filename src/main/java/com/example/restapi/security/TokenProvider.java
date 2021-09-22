package com.example.restapi.security;

import org.springframework.security.core.Authentication;

public interface TokenProvider {
    String createAccessToken(Authentication authentication);

    String createRefreshToken(Authentication authentication);

    String createConfirmationToken(String email);

    Long getUserIdFromToken(String token);

    String getUserEmailFromConfirmationToken(String token);

}
