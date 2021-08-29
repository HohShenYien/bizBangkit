package com.perajuritTeknologi.bizbangkit.ui.business;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.perajuritTeknologi.bizbangkit.MainActivity;
import com.perajuritTeknologi.bizbangkit.R;

import java.util.Objects;

public class BusinessNoBusinessFragment extends Fragment {
    private View root;
    private Button startBusinessButton;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_business_no_business, container, false);

        setUpComponents();
        setUpFragmentManager();
        onStartBusinessClicked();
        // Inflate the layout for this fragment
        return root;
    }

    private void setUpComponents() {
        startBusinessButton = root.findViewById(R.id.businessStartBusinessButton);
    }

    private void setUpFragmentManager() {
        fragmentManager = getParentFragmentManager();
    }

    private void onStartBusinessClicked() {
        startBusinessButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), NewBusinessActivity.class);
            intent.putExtra("userID", ((MainActivity) getActivity()).userProfile.userId);
            intent.putExtra("email", ((MainActivity) getActivity()).userProfile.email);
            intent.putExtra("phoneNum", ((MainActivity) getActivity()).userProfile.phoneNumber);
            startActivity(intent);
        });
    }

}





/*
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BusinessNoBusinessFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BusinessNoBusiness.
     *
    // TODO: Rename and change types and number of parameters
    public static BusinessNoBusinessFragment newInstance(String param1, String param2) {
        BusinessNoBusinessFragment fragment = new BusinessNoBusinessFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
*/