package com.example.restapi.dto;

import com.example.restapi.util.annotation.Password;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class UserRegistrationDto {
    @Email
    private String email;
    @Size(min = 2, max = 16)
    private String name;
    @Password
    private String password;
}
