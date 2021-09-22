package com.example.restapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class ErrorDto {
    List<String> messages;
    String exceptionName;
}
