package com.perajuritTeknologi.bizbangkit.ui.business;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.R;


public class BusinessNewBusinessNameFragment extends Fragment {
    private View root;
    private ImageView backButton;
    private Button nextButton;
    private EditText businessName;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_business_new_business_name, container, false);

        setUpComponents();
        setUpFragmentManager();
        onBackButtonClicked();
        onNextButtonClicked();

        return root;
    }

    private void setUpComponents() {
        backButton = root.findViewById(R.id.businessNewBusinessNameBackButton);
        nextButton = root.findViewById(R.id.businessNewBusinessNameNextButton);
        businessName = root.findViewById(R.id.editTextRegisterBusinessName);
    }

    private void onBackButtonClicked() {
        backButton.setOnClickListener(view -> {
            fragmentManager.popBackStack();
            NewBusinessActivity.progressIndicatorChanges(2,1);
            NewBusinessActivity.currentFragmentPos = 1;
        });
    }

    private void setUpFragmentManager() {
        fragmentManager = getParentFragmentManager();
    }

    private void onNextButtonClicked() {
        nextButton.setOnClickListener(view -> {
            if (onNextDeterminant()) {
                NewBusinessActivity.businessProfileDetails.name = businessName.getText().toString();

                fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new BusinessNewBusinessDetailsFragment();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right)
                        .replace(R.id.businessNewBusinessFragmentContainer, fragment)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
                NewBusinessActivity.progressIndicatorChanges(2, 3);
                NewBusinessActivity.currentFragmentPos = 3;
            }
        });
    }

    private boolean onNextDeterminant() {
        if (businessName.getText().length() == 0) {
            Toast toast = Toast.makeText(root.getContext(), "Please enter a business name!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 850);
            View view = toast.getView();
            int color = ContextCompat.getColor(root.getContext(), R.color.light_pink);
            view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            TextView toastText = view.findViewById(android.R.id.message);
            toastText.setTextColor(ContextCompat.getColor(root.getContext(), android.R.color.holo_red_light));
            toast.show();

            return false;
        }
        return true;
    }


}





/*
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BusinessNewBusinessNameFragment#newInstance} factory method to
 * create an instance of this fragment.


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BusinessNewBusinessNameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BusinessNewBusinessNameFragment.

// TODO: Rename and change types and number of parameters
public static BusinessNewBusinessNameFragment newInstance(String param1, String param2) {
    BusinessNewBusinessNameFragment fragment = new BusinessNewBusinessNameFragment();
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