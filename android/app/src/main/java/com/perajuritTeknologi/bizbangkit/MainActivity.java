package com.perajuritTeknologi.bizbangkit;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ShenYien_test", "entered Main");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }
}
