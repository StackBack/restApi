package com.example.restapi.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {
    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
