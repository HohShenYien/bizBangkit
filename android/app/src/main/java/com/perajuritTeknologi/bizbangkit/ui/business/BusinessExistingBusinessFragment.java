package com.perajuritTeknologi.bizbangkit.ui.business;

import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.perajuritTeknologi.bizbangkit.R;

public class BusinessExistingBusinessFragment extends Fragment {
    private View root;
    private int progressFinalValue;
    private TextView progressPercent, progressValues;
    private CircularProgressIndicator progressIndicator;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_business_existing_business, container, false);

        setUpComponents();
        setUpProgress();
        return root;
    }

    private void setUpComponents() {
        progressPercent = root.findViewById(R.id.businessExistingBusinessProgressNumber);
        progressIndicator = root.findViewById(R.id.businessExistingBusinessProgressIndicator);
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

    // will set depending on data from server (sample for now)
}
