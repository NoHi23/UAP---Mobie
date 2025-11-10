package com.uap.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.uap.domain.model.User;

public class AuthManager {
    private static final String PREF = "auth_pref";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_ROLE = "role";
    private static final String KEY_REQUIRE_CHANGE = "require_change";

    private static final String KEY_USER = "user";

    public static void saveLogin(Context ctx, String token, String role, boolean requireChange) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit()
                .putString(KEY_TOKEN, token)
                .putString(KEY_ROLE, role)
                .putBoolean(KEY_REQUIRE_CHANGE, requireChange)
                .apply();
    }

    public static boolean isLoggedIn(Context ctx) {
        return getToken(ctx) != null;
    }

    public static String getToken(Context ctx) {
        return ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .getString(KEY_TOKEN, null);
    }

    public static String getRole(Context ctx) {
        return ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .getString(KEY_ROLE, null);
    }

    public static void saveUser(Context context, User userData) {
        if (context == null || userData == null) return;

        SharedPreferences prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String userJson = gson.toJson(userData);

        editor.putString(KEY_USER, userJson);
        editor.apply();
    }

    public static User getUser(Context context) {
        if (context == null) return null;
        SharedPreferences prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String userJson = prefs.getString(KEY_USER, null);
        if (userJson == null) return null;

        return new Gson().fromJson(userJson, User.class);
    }

    public static boolean isPasswordChangeRequired(Context ctx) {
        return ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .getBoolean(KEY_REQUIRE_CHANGE, false);
    }

    public static void setPasswordChangeRequired(Context ctx, boolean required) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit().putBoolean(KEY_REQUIRE_CHANGE, required).apply();
    }

    public static void logout(Context ctx) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit().clear().apply();
    }

}