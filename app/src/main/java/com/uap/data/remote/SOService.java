package com.uap.data.remote;

import com.uap.data.remote.request.LoginRequest;
import com.uap.data.remote.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SOService {
    @POST("account/login")
    Call<LoginResponse> login(@Body LoginRequest body);
}
