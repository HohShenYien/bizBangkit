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
    private static SharedPreferences sharedPreferences;

    public static void setUpSharedPreferences(AppCompatActivity activity) {
        sharedPreferences = activity.getSharedPreferences(TOKEN_NAME,
                Context.MODE_PRIVATE);
    }

    public static boolean firstTime() {
        if ( sharedPreferences.contains("First Time") ) {
            return false;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("First Time", "No");
        editor.commit();
        return true;
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
