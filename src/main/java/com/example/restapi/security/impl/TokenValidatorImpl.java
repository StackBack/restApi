package com.example.restapi.security.impl;

import com.example.restapi.security.TokenValidator;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class TokenValidatorImpl implements TokenValidator {
    private static final Logger logger = LoggerFactory.getLogger(TokenValidatorImpl.class);
    private static final String RESET_PASSWORD_SCOPE = "resetPassword";

    @Value("${token.secret}")
    private String tokenSecret;


    @Override
    public boolean validate(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(tokenSecret)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;

    }

    @Override
    public boolean isTokenForResetPassword(String token) {
        return false;
    }
}
