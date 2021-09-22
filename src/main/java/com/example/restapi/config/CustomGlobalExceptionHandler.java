package com.example.restapi.config;

import com.example.restapi.dto.ErrorDto;
import com.example.restapi.exception.InvalidTokenException;
import com.example.restapi.exception.UserAlreadyExistsException;
import com.example.restapi.exception.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({
            UserAlreadyExistsException.class,
            UserNotFoundException.class,
            InvalidTokenException.class})
    public ErrorDto handleException(RuntimeException exception) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setExceptionName(exception.getClass().getSimpleName());
        errorDto.setMessages(Collections.singletonList(exception.getMessage()));
        return errorDto;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setExceptionName(ex.getClass().getSimpleName());
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());
        errorDto.setMessages(errors);
        return new ResponseEntity<>(errorDto, headers, status);
    }
}

