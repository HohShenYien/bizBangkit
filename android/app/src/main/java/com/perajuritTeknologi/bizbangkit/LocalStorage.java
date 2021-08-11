package com.perajuritTeknologi.bizbangkit;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

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

    public static void saveCurrentTextChange(AppCompatActivity activity, EditText editText, String mainKey, String sideKey) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(mainKey, Context.MODE_PRIVATE);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(sideKey, editText.getText().toString());
                editor.apply();
            }
        });
    }

    public static void setChangedText(AppCompatActivity activity, EditText editText, String mainKey, String sideKey) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(mainKey, Context.MODE_PRIVATE);
        String n = sharedPreferences.getString(sideKey, null);
        if (n != null) {
            editText.setText(n);
        }
    }

    // --------- Error classes
    private static class TokenNotFoundException extends Exception{
        public TokenNotFoundException(String errorMsg) {
            super(errorMsg);
        }
    }
}
