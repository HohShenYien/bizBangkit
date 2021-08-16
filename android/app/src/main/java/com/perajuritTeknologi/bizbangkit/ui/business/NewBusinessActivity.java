package com.perajuritTeknologi.bizbangkit.ui.business;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.R;

public class NewBusinessActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private static LinearProgressIndicator progress1, progress2, progress3, progress4;
    public static DataStructure.BusinessProfileDetails businessProfileDetails = new DataStructure.BusinessProfileDetails();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_business);

        setUpComponents();
        setUpFragmentManager();
        setUpFragment();
    }

    private void setUpComponents() {
        progress1 = findViewById(R.id.businessRegisterProgress1);
        progress2 = findViewById(R.id.businessRegisterProgress2);
        progress3 = findViewById(R.id.businessRegisterProgress3);
        progress4 = findViewById(R.id.businessRegisterProgress4);

        progress1.setProgress(100, true);
    }

    private void setUpFragmentManager() {
        fragmentManager = getSupportFragmentManager();
    }

    private void setUpFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new BusinessNewBusinessTypeFragment();
        fragmentTransaction.replace(R.id.businessNewBusinessFragmentContainer, fragment)
                .commit();

    }

    // determining progress indicators' animation movements
    public static void progressIndicatorChanges(int initialFragment, int finalFragment) {
        if (initialFragment < finalFragment) {
            if (initialFragment == 1) {
                progress1.setIndicatorDirection(LinearProgressIndicator.INDICATOR_DIRECTION_RIGHT_TO_LEFT);
                progress1.setProgress(0, true);
                progress2.setProgress(100, true);
            }
            else if (initialFragment == 2) {
                progress2.setIndicatorDirection(LinearProgressIndicator.INDICATOR_DIRECTION_RIGHT_TO_LEFT);
                progress2.setProgress(0, true);
                progress3.setProgress(100, true);
            }
            else {
                progress3.setIndicatorDirection(LinearProgressIndicator.INDICATOR_DIRECTION_RIGHT_TO_LEFT);
                progress3.setProgress(0, true);
                progress4.setProgress(100, true);
            }
        }
        else {
            if (initialFragment == 4) {
                progress4.setIndicatorDirection(LinearProgressIndicator.INDICATOR_DIRECTION_LEFT_TO_RIGHT);
                progress4.setProgress(0, true);
                progress3.setIndicatorDirection(LinearProgressIndicator.INDICATOR_DIRECTION_RIGHT_TO_LEFT);
                progress3.setProgress(100, true);
            }
            else if (initialFragment == 3) {
                progress3.setIndicatorDirection(LinearProgressIndicator.INDICATOR_DIRECTION_LEFT_TO_RIGHT);
                progress3.setProgress(0, true);
                progress2.setIndicatorDirection(LinearProgressIndicator.INDICATOR_DIRECTION_RIGHT_TO_LEFT);
                progress2.setProgress(100, true);
            }
            else {
                progress2.setIndicatorDirection(LinearProgressIndicator.INDICATOR_DIRECTION_LEFT_TO_RIGHT);
                progress2.setProgress(0, true);
                progress1.setIndicatorDirection(LinearProgressIndicator.INDICATOR_DIRECTION_RIGHT_TO_LEFT);
                progress1.setProgress(100, true);
            }
        }
    }

}