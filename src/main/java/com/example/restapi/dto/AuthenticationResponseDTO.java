package com.example.restapi.dto;

import lombok.Data;

@Data
public class AuthenticationResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    public AuthenticationResponseDTO(String accessToken){
        this.accessToken = accessToken;
    }
}
