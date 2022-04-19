package com.example.demo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String fullname;
    private String username;
    private String password;
    private String role;
}
