package com.example.restapi.config;

import com.example.restapi.dto.ErrorDto;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setExceptionName(authException.getClass().getSimpleName());
        String errorMessage = authException.getMessage();
        errorDto.setMessages(Collections.singletonList(errorMessage));
        String employeeJsonString = new Gson().toJson(errorDto);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter out = response.getWriter();
        out.print(employeeJsonString);
    }
}

