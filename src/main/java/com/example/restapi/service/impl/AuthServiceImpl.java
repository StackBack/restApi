package com.example.restapi.service.impl;

import com.example.restapi.domain.CustomUser;
import com.example.restapi.exception.UserAlreadyExistsException;
import com.example.restapi.repository.UserRepository;
import com.example.restapi.service.AuthService;
import com.example.restapi.service.TokenService;
import com.example.restapi.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final TokenService tokenService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    public HashMap<String, String> loginUser(CustomUser user) {
        if(isUserExistByEmail(user.getEmail()) &&
                passwordEncoder.matches(user.getPassword(), userService.findUserByEmail(user.getEmail()).getPassword()));
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        return authorizeUserAndCreateToken(authentication);
    }

    @Override
    public CustomUser registrationUser(CustomUser user) {
        if (isUserExistByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User already exist");
        }
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        return userRepository.save(user);
    }

    private HashMap<String, String> authorizeUserAndCreateToken(Authentication authentication){
        SecurityContextHolder.getContext().setAuthentication(authentication);
        HashMap<String, String> tokens = tokenService.createTokens(authentication);
        tokenService.createRefreshTokenEntityAndSave(tokens, authentication);
        return tokens;
    }

    private boolean isUserExistByEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }
}
