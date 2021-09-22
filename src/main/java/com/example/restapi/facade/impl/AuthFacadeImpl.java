package com.example.restapi.facade.impl;

import com.example.restapi.domain.CustomUser;
import com.example.restapi.dto.AuthenticationResponseDTO;
import com.example.restapi.dto.UserDto;
import com.example.restapi.dto.UserLoginDto;
import com.example.restapi.dto.UserRegistrationDto;
import com.example.restapi.facade.AuthFacade;
import com.example.restapi.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthFacadeImpl implements AuthFacade {
    private final AuthService authService;
    private final ModelMapper modelMapper;

    public AuthFacadeImpl(AuthService authService, ModelMapper modelMapper) {
        this.authService = authService;
        this.modelMapper = modelMapper;
    }

    @Override
    public AuthenticationResponseDTO loginUser(UserLoginDto dto) {
        CustomUser user = modelMapper.map(dto, CustomUser.class);
        HashMap<String, String> tokens = authService.loginUser(user);
        AuthenticationResponseDTO authenticationResponseDTO = new AuthenticationResponseDTO(tokens.get("AccessToken"));
        authenticationResponseDTO.setRefreshToken(tokens.get("RefreshToken"));
        return authenticationResponseDTO;

    }

    @Override
    public AuthenticationResponseDTO registrationUser(UserRegistrationDto dto) {
        CustomUser user = modelMapper.map(dto, CustomUser.class);
        authService.registrationUser(user);
        HashMap<String, String> tokens = authService.loginUser(modelMapper.map(dto, CustomUser.class));
        AuthenticationResponseDTO authenticationResponseDTO = new AuthenticationResponseDTO(tokens.get("AccessToken"));
        authenticationResponseDTO.setRefreshToken(tokens.get("RefreshToken"));
        return authenticationResponseDTO;
    }
}
