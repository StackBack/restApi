package com.example.restapi.controller;

import com.example.restapi.dto.AuthenticationResponseDTO;
import com.example.restapi.dto.UserDto;
import com.example.restapi.dto.UserLoginDto;
import com.example.restapi.dto.UserRegistrationDto;
import com.example.restapi.facade.AuthFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthCntroller {
    private final AuthFacade authFacade;

    public AuthCntroller(AuthFacade authFacade) {
        this.authFacade = authFacade;
    }

    @PostMapping("/signIn")
    public ResponseEntity<AuthenticationResponseDTO> loginUser(@Valid @RequestBody UserLoginDto dto){
        return ResponseEntity.ok(authFacade.loginUser(dto));
    }

    @PostMapping("/signUp")
    public ResponseEntity<AuthenticationResponseDTO> registrationUser(@Valid @RequestBody UserRegistrationDto dto){
        return ResponseEntity.status(HttpStatus.OK).body(authFacade.registrationUser(dto));
    }
}
