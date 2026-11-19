package com.example.gruz.models;

public class LoginRequest {
    private String identity;
    private String password;
    public LoginRequest(String identity, String password) {
        this.identity = identity;
        this.password = password;
    }
}
