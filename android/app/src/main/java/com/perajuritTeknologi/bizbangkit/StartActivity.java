package com.perajuritTeknologi.bizbangkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.fullScreenMode(this);
        setContentView(R.layout.activity_launch);
        LocalStorage.setUpSharedPreferences(this);
        // let the user see the front page for a while before moving on XD
        delayRun();
    }

    private void redirect() {
        Intent intent;
        if (LocalStorage.isLoggedIn()) {
            intent = new Intent(getApplicationContext(), MainActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        }
        startActivity(intent);
    }

    private void delayRun() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                redirect();
            }
        }, 1500);
    }

}