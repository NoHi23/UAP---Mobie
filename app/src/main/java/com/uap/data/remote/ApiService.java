package com.uap.data.remote;

import com.uap.App;

public class ApiService {
    private static final String BASE_URL = "http://172.19.240.1:9999/api/";
    public static SOService getSOService() {
        return RetrofitClient.getClient(App.getContext(), BASE_URL)
                .create(SOService.class);
    }
}
