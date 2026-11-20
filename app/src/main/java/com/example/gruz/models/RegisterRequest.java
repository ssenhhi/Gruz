package com.example.gruz.models;

public class RegisterRequest {
    private String email;
    private String password;
    private String passwordConfirm;
    private String login;
    private String full_name;
    private String phone;
    private String index;
    private String city;
    private String street;
    private String homeNumber;

    public RegisterRequest(String login, String fullName, String phone, String email, String password,
                           String index, String city, String street, String homeNumber) {
        this.login = login;
        this.full_name = fullName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.passwordConfirm = password;
        this.index = index;
        this.city = city;
        this.street = street;
        this.homeNumber = homeNumber;
    }
}
