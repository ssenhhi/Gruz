package com.example.gruz;

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

    public void clearTokens() {
        prefs.edit().clear().apply();
    }
    public void saveUserEmail(String email) {
        prefs.edit().putString("user_email", email).apply();
    }
    public String getUserEmail() {
        return prefs.getString("user_email", null);
    }
}
