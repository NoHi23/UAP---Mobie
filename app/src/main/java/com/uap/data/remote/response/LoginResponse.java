package com.uap.data.remote.response;

import androidx.annotation.NonNull;

import com.uap.domain.model.User;

public class LoginResponse {
    private String message;
    private String token;
    private User user;
    private boolean passwordChangeRequired;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isPasswordChangeRequired() {
        return passwordChangeRequired;
    }

    public void setPasswordChangeRequired(boolean passwordChangeRequired) {
        this.passwordChangeRequired = passwordChangeRequired;
    }

    @NonNull
    @Override
    public String toString() {
        return "LoginResponse{" +
                "message='" + message + '\'' +
                ", token='" + token + '\'' +
                ", user=" + user +
                ", passwordChangeRequired=" + passwordChangeRequired +
                '}';
    }
}