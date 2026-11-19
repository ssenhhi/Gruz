package com.example.gruz.models;

public class UserUpdateRequest {
    private String full_name;
    private String email;
    private String phone;

    public UserUpdateRequest(String full_name, String email, String phone) {
        this.full_name = full_name;
        this.email = email;
        this.phone = phone;
    }
}
