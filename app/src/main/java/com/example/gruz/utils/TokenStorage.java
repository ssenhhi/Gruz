package com.example.gruz.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenStorage {
    private static final String PREFS_NAME = "auth_prefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private final SharedPreferences prefs;
    public TokenStorage(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    public void saveTokens(String accessToken, String refreshToken) {
        prefs.edit().putString(KEY_ACCESS_TOKEN, accessToken).putString(KEY_REFRESH_TOKEN, refreshToken).apply();
    }
    public String getAccessToken() {
        return prefs.getString(KEY_ACCESS_TOKEN, null);
    }
    public void saveUserEmail(String email) {
        prefs.edit().putString("user_email", email).apply();
    }
    public void saveUserId(String id) {
        prefs.edit().putString("user_id", id).apply();
    }
    public String getUserId() {
        return prefs.getString("user_id", null);
    }
    public void saveUserName(String name) {
        prefs.edit().putString("user_name", name).apply();
    }
    public String getUserName() {
        return prefs.getString("user_name", "Пользователь");
    }

    public void saveEmail(String email) {
        prefs.edit().putString("user_email", email).apply();
    }
    public String getEmail() {
        return prefs.getString("user_email", null);
    }
}
