package com.uap.auth;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthManager {
    private static final String PREF = "auth_pref";
    private static final String KEY_TOKEN = "token";

    public static void saveToken(Context ctx, String token) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_TOKEN, token).apply();
    }

    public static String getToken(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(KEY_TOKEN, null);
    }

    public static boolean isLoggedIn(Context ctx) {
        return getToken(ctx) != null;
    }

    public static void logout(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }
}