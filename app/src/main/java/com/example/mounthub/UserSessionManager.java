package com.example.mounthub;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionManager {
    private final SharedPreferences pref;

    // sharedpref file name
    private static final String PREF_NAME = "user_session";

    // All Shared Preferences Keys
    private static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String KEY_ID = "id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
//    public static final String KEY_PASSWORD = "password";
    public static final String KEY_INFO = "info";

    public UserSessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, 0);
    }

    public void saveUser(User user) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_LOGGED_IN, true);

        // Storing user details
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
//        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putString(KEY_INFO, user.getInfo());
        editor.apply();
    }

    public User getCurrentUser() {
        boolean isLoggedIn = pref.getBoolean(IS_LOGGED_IN, false);

        if (isLoggedIn) {
            return new User(
                    pref.getInt(KEY_ID, -1),
                    pref.getString(KEY_USERNAME, null),
                    pref.getString(KEY_EMAIL, null),
                    null,
                    pref.getString(KEY_INFO, null)
            );
        }

        return null;
    }

    public void clearUser() {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
