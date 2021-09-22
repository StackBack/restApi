package com.example.restapi.security.impl;

import com.example.restapi.security.TokenProvider;
import com.example.restapi.security.UserPrincipal;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenProviderImpl implements TokenProvider {
    @Value("${token.secret}")
    private String tokenSecret;
    @Value("${access.token.expiration.time.ms}")
    private long expirationTimeAccessToken;
    @Value("${refresh.token.expiration.time.ms}")
    private long expirationTimeRefreshToken;
    @Value("${confirmation.token.expiration.time.ms}")
    private long expirationTimeConfirmationToken;

    @Override
    public String createAccessToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date expiryDate = new Date(System.currentTimeMillis() + expirationTimeAccessToken);
        return createToken(userPrincipal,expiryDate);
    }

    @Override
    public String createRefreshToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date expiryDate = new Date(System.currentTimeMillis() + expirationTimeRefreshToken);
        return createToken(userPrincipal,expiryDate);
    }

    @Override
    public String createConfirmationToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeConfirmationToken))
                .claim("scope", "resetPassword")
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    @Override
    public Long getUserIdFromToken(String token) {
        return Long.parseLong(getTokenClaimsSubject(token));
    }

    @Override
    public String getUserEmailFromConfirmationToken(String token) {
        return getTokenClaimsSubject(token);
    }

    private String createToken(UserPrincipal userPrincipal, Date expiryDate){
        return Jwts.builder()
                .setSubject(String.valueOf(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    private String getTokenClaimsSubject(String token){
        return Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

