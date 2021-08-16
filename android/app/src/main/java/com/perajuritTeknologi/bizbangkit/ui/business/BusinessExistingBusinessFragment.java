package com.perajuritTeknologi.bizbangkit.ui.business;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perajuritTeknologi.bizbangkit.R;

public class BusinessExistingBusinessFragment extends Fragment {
    private View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_business_existing_business, container, false);


        return root;
    }


}
