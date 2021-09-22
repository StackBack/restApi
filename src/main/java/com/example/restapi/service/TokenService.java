package com.example.restapi.service;

import org.springframework.security.core.Authentication;

import java.util.HashMap;

public interface TokenService {
    HashMap<String, String> updateTokens(String tokenFromHeader);

    HashMap<String, String> createTokens(Authentication authentication);

    String getUserEmailFromToken(String token);

    void createRefreshTokenEntityAndSave(HashMap<String, String> tokens, Authentication authentication);
}
