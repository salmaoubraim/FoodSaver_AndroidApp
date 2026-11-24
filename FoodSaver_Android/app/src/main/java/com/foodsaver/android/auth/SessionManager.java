package com.foodsaver.android.auth;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREFS_NAME = "FoodSaverSession";
    private static final String KEY_USER_ID   = "userId";   // Firebase UID (String)
    private static final String KEY_EMAIL     = "email";
    private static final String KEY_ROLE      = "role";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_LOGGED_IN = "loggedIn";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // ✅ Nouveau login avec Firebase UID (String)
    public void login(String firebaseUid, String email, String role, String name) {
        prefs.edit()
                .putString(KEY_USER_ID,   firebaseUid)
                .putString(KEY_EMAIL,     email)
                .putString(KEY_ROLE,      role)
                .putString(KEY_USER_NAME, name != null ? name : "")
                .putBoolean(KEY_LOGGED_IN, true)
                .apply();
    }

    public void logout() {
        prefs.edit().clear().apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_LOGGED_IN, false);
    }

    // ✅ Retourne String au lieu de long
    public String getUserId() {
        return prefs.getString(KEY_USER_ID, null);
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, "");
    }

    public String getRole() {
        return prefs.getString(KEY_ROLE, "");
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "");
    }
}