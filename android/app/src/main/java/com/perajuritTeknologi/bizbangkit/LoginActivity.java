package com.perajuritTeknologi.bizbangkit;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ShenYien_test", "entered Login");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}