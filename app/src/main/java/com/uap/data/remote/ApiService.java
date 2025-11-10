package com.uap.data.remote;

import com.uap.App;

public class ApiService {
    private static final String BASE_URL = "http://192.168.1.16:9999/api/";
    public static SOService getSOService() {
        return RetrofitClient.getClient(App.getContext(), BASE_URL)
                .create(SOService.class);
    }
}
