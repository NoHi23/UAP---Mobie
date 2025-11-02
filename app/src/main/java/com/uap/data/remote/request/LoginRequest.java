package com.uap.data.remote.request;



public class LoginRequest {
    public String email;
    public String password;

    public LoginRequest(String u, String p) {
        this.email = u;
        this.password = p;
    }
}
