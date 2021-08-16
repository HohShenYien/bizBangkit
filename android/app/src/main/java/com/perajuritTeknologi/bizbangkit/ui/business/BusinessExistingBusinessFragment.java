package com.perajuritTeknologi.bizbangkit.ui.business;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.perajuritTeknologi.bizbangkit.R;

public class BusinessExistingBusinessFragment extends Fragment {
    private View root;
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
        progressIndicator = root.findViewById(R.id.businessExistingBusinessProgressIndicator);
    }

    private void setUpProgress() {
        progressIndicator.setProgress(60, true);
    }


}
