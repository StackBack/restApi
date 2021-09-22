package com.example.restapi.service;

import com.example.restapi.domain.CustomUser;

public interface UserService {
    CustomUser findUserById(Long id);

    CustomUser findUserByEmail(String email);
}
