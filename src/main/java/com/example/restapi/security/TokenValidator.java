package com.example.restapi.security;

public interface TokenValidator {
    boolean validate(String token);

    boolean isTokenForResetPassword(String token);

}
