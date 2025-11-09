package com.uap.data.remote;

public class ApiService {
    private static final String BASE_URL = "http://192.168.1.6:9999/api/";
    public static SOService getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(SOService.class);
    }
}
