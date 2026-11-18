package com.example.gruz;

public class RegisterRequest {
    private String email;
    private String password;
    private String passwordConfirm;
    private String login;       // БЫЛО username
    private String full_name;   // НОВОЕ
    private String phone;       // НОВОЕ

    public RegisterRequest(String login, String fullName, String phone, String email, String password) {
        this.login = login;
        this.full_name = fullName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.passwordConfirm = password;
    }
}
