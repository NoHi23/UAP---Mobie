package com.uap.data.remote;

import android.content.Intent;

import com.uap.App;
import com.uap.data.local.AuthManager;
import com.uap.ui.LoginActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
public class UnauthorizedInterceptor implements Interceptor {
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        if (response.code() == 401) {
            // Xóa token
            AuthManager.logout(App.getContext());

            // Mở màn hình login
            Intent intent = new Intent(App.getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            App.getContext().startActivity(intent);
        }

        return response;
    }
}
