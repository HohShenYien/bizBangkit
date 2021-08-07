package com.perajuritTeknologi.bizbangkit;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class LocalStorage {
    private static final String TOKEN_NAME = "token";
    private static final String LOGIN_TOKEN = "login_token";
    private static final String ID_TOKEN = "id_token";

    public static boolean isLoggedIn(AppCompatActivity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(TOKEN_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.contains(LOGIN_TOKEN) &&
                sharedPreferences.contains(ID_TOKEN);
    }

    public static String getToken(AppCompatActivity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(TOKEN_NAME,
                                                        Context.MODE_PRIVATE);

        return sharedPreferences.getString(LOGIN_TOKEN, null);
    }

    public static void setToken(AppCompatActivity activity, String token) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(TOKEN_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN_TOKEN, token);
        editor.commit();
    }

    public static String getID(AppCompatActivity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(TOKEN_NAME,
                Context.MODE_PRIVATE);

        return sharedPreferences.getString(ID_TOKEN, null);
    }

    public static void setID(AppCompatActivity activity, String userID) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(TOKEN_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ID_TOKEN, userID);
        editor.commit();
    }

    // --------- Error classes
    private static class TokenNotFoundException extends Exception{
        public TokenNotFoundException(String errorMsg) {
            super(errorMsg);
        }
    }
}
