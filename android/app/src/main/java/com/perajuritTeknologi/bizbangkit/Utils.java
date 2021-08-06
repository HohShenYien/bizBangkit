package com.perajuritTeknologi.bizbangkit;

import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class Utils {
    public static void hideTitleBar(AppCompatActivity activity) {
        activity.getSupportActionBar().hide(); // hide the title bar
    }

    public static void fullScreenMode(AppCompatActivity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        hideTitleBar(activity);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
