package com.uap.domain.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @SerializedName("_id")
    private String id;
    private String email;
    private String role;
    private String status;
    private boolean isFirstLogin;
    private String name;
    private String avatar;
}
