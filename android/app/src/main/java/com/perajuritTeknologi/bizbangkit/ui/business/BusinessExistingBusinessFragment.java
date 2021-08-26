package com.perajuritTeknologi.bizbangkit.ui.business;

import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.perajuritTeknologi.bizbangkit.MainActivity;
import com.perajuritTeknologi.bizbangkit.R;

public class BusinessExistingBusinessFragment extends Fragment {
    private View root;
    private int progressFinalValue;
    private TextView progressPercent, progressValues;
    private CircularProgressIndicator progressIndicator;
    private MaterialButton financialDetailsButton;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_business_existing_business, container, false);

        setUpComponents();
        setUpFragmentManager();
        setUpProgress();
        onFinancialDetailsClicked();
        return root;
    }

    private void setUpComponents() {
        progressPercent = root.findViewById(R.id.businessExistingBusinessProgressNumber);
        progressIndicator = root.findViewById(R.id.businessExistingBusinessProgressIndicator);
        financialDetailsButton = root.findViewById(R.id.businessExistingBusinessFinancialDetails);

    }

    private void setUpFragmentManager() {
        fragmentManager = getParentFragmentManager();
    }

    // will set depending on data from server (sample for now)
    private void setUpProgress() {

        // need to assign value with data from server
        progressFinalValue = 60;

        ValueAnimator animator = ValueAnimator.ofInt(0, progressFinalValue);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                progressPercent.setText((valueAnimator.getAnimatedValue().toString() + "%"));
            }
        });
        animator.start();
        progressIndicator.setProgress(progressFinalValue, true);
    }

    private void changeFragment(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_to_bottom)
                .replace(R.id.businessFragmentContainer, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
        MainActivity.basePage++;
    }

    private void onFinancialDetailsClicked() {
        financialDetailsButton.setOnClickListener(view -> {
            Fragment fragment = new BusinessExistingBusinessFinancialDetailsFragment();
            changeFragment(fragment);
        });
    }
}
