package com.pucmm.user;

public class AuthUser {

    public String email;
    public String password;

    public AuthUser(){}

    public AuthUser(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
