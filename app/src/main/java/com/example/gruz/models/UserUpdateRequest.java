package com.example.gruz.models;

public class UserUpdateRequest {
    private String full_name;
    private String email;
    private String phone;
    private String login;
    private String index;
    private String city;
    private String street;
    private String homeNumber;

    public UserUpdateRequest(String full_name, String email, String phone, String login,
                             String index, String city, String street, String homeNumber) {
        this.full_name = full_name;
        this.email = email;
        this.phone = phone;
        this.login = login;
        this.index = index;
        this.city = city;
        this.street = street;
        this.homeNumber = homeNumber;
    }
}
