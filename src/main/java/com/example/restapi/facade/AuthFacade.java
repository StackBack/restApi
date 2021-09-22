package com.example.restapi.facade;

import com.example.restapi.dto.AuthenticationResponseDTO;
import com.example.restapi.dto.UserLoginDto;
import com.example.restapi.dto.UserRegistrationDto;

public interface AuthFacade {
    AuthenticationResponseDTO loginUser(UserLoginDto dto);

    AuthenticationResponseDTO registrationUser(UserRegistrationDto dto);
}
