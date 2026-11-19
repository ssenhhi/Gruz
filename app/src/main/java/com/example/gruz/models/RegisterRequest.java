package com.example.gruz.models;

public class RegisterRequest {
    private String email;
    private String password;
    private String passwordConfirm;
    private String login;
    private String full_name;
    private String phone;

    public RegisterRequest(String login, String fullName, String phone, String email, String password) {
        this.login = login;
        this.full_name = fullName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.passwordConfirm = password;
    }
}
