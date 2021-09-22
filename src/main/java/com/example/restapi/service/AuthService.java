package com.example.restapi.service;

import com.example.restapi.domain.CustomUser;

import java.util.HashMap;

public interface AuthService {
    HashMap<String, String> loginUser(CustomUser user);

    CustomUser registrationUser(CustomUser user);
}
