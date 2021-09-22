package com.example.restapi.service.impl;

import com.example.restapi.domain.RefreshToken;
import com.example.restapi.exception.InvalidTokenException;
import com.example.restapi.repository.RefreshTokenRepository;
import com.example.restapi.repository.UserRepository;
import com.example.restapi.security.TokenProvider;
import com.example.restapi.security.TokenValidator;
import com.example.restapi.security.UserPrincipal;
import com.example.restapi.service.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class TokenServiceImpl implements TokenService {
    private final TokenValidator tokenValidator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public TokenServiceImpl(TokenValidator tokenValidator, RefreshTokenRepository refreshTokenRepository, TokenProvider tokenProvider, UserRepository userRepository) {
        this.tokenValidator = tokenValidator;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public HashMap<String, String> updateTokens(String tokenFromHeader) {
        String refreshToken = tokenFromHeader.split(" ",2)[1];
        if (!tokenValidator.validate(refreshToken)) {
            throw new InvalidTokenException("invalid token");
        }
        RefreshToken token = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new InvalidTokenException("token not found"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        HashMap<String, String> tokens = createTokens(authentication);
        token.setRefreshToken(tokens.get("RefreshToken"));
        token.setCreationDate(LocalDateTime.parse(tokens.get("CreatedTime")));
        token = refreshTokenRepository.save(token);
        return tokens;

    }

    @Override
    public HashMap<String, String> createTokens(Authentication authentication) {
        HashMap<String, String> tokens = new HashMap<>();
        tokens.put("AccessToken", tokenProvider.createAccessToken(authentication));
        tokens.put("RefreshToken", tokenProvider.createRefreshToken(authentication));
        tokens.put("CreatedTime", LocalDateTime.now().toString());
        return tokens;
    }

    @Override
    public String getUserEmailFromToken(String token) {
        if (!tokenValidator.validate(token) || !tokenValidator.isTokenForResetPassword(token)){
            throw new InvalidTokenException("invalid.token");
        }
        return getEmailFromToken(token);
    }


    @Override
    public void createRefreshTokenEntityAndSave(HashMap<String, String> tokens, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        RefreshToken refreshToken = refreshTokenRepository.findByUser(userRepository.getById(userPrincipal.getId()))
                .orElseGet(RefreshToken::new);
        refreshToken.setRefreshToken(tokens.get("RefreshToken"));
        refreshToken.setCreationDate(LocalDateTime.now());
        refreshToken.setUser(userRepository.findById(userPrincipal.getId()).get());
        refreshTokenRepository.save(refreshToken);
    }

    private String getEmailFromToken(String token){
        return tokenProvider.getUserEmailFromConfirmationToken(token);
    }


}
