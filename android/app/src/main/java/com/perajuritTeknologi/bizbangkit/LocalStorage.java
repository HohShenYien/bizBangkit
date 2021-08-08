package com.perajuritTeknologi.bizbangkit;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class LocalStorage {
    private static final String TOKEN_NAME = "token";
    private static final String LOGIN_TOKEN = "login_token";
    private static final String ID_TOKEN = "id_token";
    private static SharedPreferences sharedPreferences;

    public static void setUpSharedPreferences(AppCompatActivity activity) {
        sharedPreferences = activity.getSharedPreferences(TOKEN_NAME,
                Context.MODE_PRIVATE);
    }

    public static boolean isLoggedIn() {
        return sharedPreferences.contains(LOGIN_TOKEN) &&
                sharedPreferences.contains(ID_TOKEN);
    }

    public static String getToken() {
        return sharedPreferences.getString(LOGIN_TOKEN, null);
    }

    public static void setToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN_TOKEN, token);
        editor.commit();
    }

    public static String getID() {
        return sharedPreferences.getString(ID_TOKEN, null);
    }

    public static void setID(String userID) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ID_TOKEN, userID);
        editor.commit();
    }

    public static void clearCredentials() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ID_TOKEN);
        editor.remove(LOGIN_TOKEN);
        editor.commit();
    }

    // --------- Error classes
    private static class TokenNotFoundException extends Exception{
        public TokenNotFoundException(String errorMsg) {
            super(errorMsg);
        }
    }
}
