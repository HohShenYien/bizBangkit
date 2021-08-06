package com.perajuritTeknologi.bizbangkit;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class LocalStorage {
    private static final String TOKEN_NAME = "token";
    private static final String LOGIN_TOKEN = "login_token";
    private static final String ID_TOKEN = "id_token";

    public static boolean isLoggedIn(AppCompatActivity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(TOKEN_NAME,
                Context.MODE_PRIVATE);
        if (sharedPreferences.contains(LOGIN_TOKEN) &&
                sharedPreferences.contains(ID_TOKEN)) {
            return true;
        }
        return false;
    }

    public static String getToken(AppCompatActivity activity) throws TokenNotFoundException{
        SharedPreferences sharedPreferences = activity.getSharedPreferences(TOKEN_NAME,
                                                        Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(LOGIN_TOKEN, null);

        if (token == null) {
            throw new TokenNotFoundException("Login token not found");
        }

        return token;
    }

    public static void setToken(StartActivity activity, String token) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(TOKEN_NAME,
                Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(LOGIN_TOKEN, token);
    }

    public static String getID(StartActivity activity) throws TokenNotFoundException{
        SharedPreferences sharedPreferences = activity.getSharedPreferences(TOKEN_NAME,
                Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString(ID_TOKEN, null);

        if (userId == null) {
            throw new TokenNotFoundException("User ID token not found");
        }

        return userId;
    }

    public static void setID(StartActivity activity, String userID) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(TOKEN_NAME,
                Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(LOGIN_TOKEN, userID);
    }

    // --------- Error classes
    private static class TokenNotFoundException extends Exception{
        public TokenNotFoundException(String errorMsg) {
            super(errorMsg);
        }
    }
}
