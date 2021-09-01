package com.perajuritTeknologi.bizbangkit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.perajuritTeknologi.bizbangkit.ui.HelpPage;

public class FirstTimeActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    LinearLayout skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_time_activity);
        Utils.hideTitleBar(this);
        frameLayout = findViewById(R.id.help_fragment);
        skip = findViewById(R.id.skip);
        skip.setOnClickListener(v-> {
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(intent);
        });
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment newFragment = new HelpPage();
        transaction.replace(R.id.help_fragment,
                newFragment).commit();
    }
}
