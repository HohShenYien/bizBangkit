package com.perajuritTeknologi.bizbangkit.ui.business;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perajuritTeknologi.bizbangkit.R;

public class BusinessNewBusinessTypeFragment extends Fragment {
    private View root;
    private CardView newBusiness, existingBusiness;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_business_new_business_type, container, false);

        setUpComponents();
        setUpFragmentManager();
        onNewBusinessClicked();
        onExistingBusinessClicked();

        return root;
    }

    private void setUpComponents() {
        newBusiness = root.findViewById(R.id.newBusinessCardView);
        existingBusiness = root.findViewById(R.id.existingBusinessCardView);
    }

    private void setUpFragmentManager() {
        fragmentManager = getParentFragmentManager();
    }

    private void onNewBusinessClicked() {
        newBusiness.setOnClickListener(view -> {
            NewBusinessActivity.businessProfileDetails.type = "New";
            replaceFragment();
        });
    }

    private void onExistingBusinessClicked() {
        existingBusiness.setOnClickListener(view -> {
            NewBusinessActivity.businessProfileDetails.type = "Existing";
            replaceFragment();
        });
    }

    private void replaceFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new BusinessNewBusinessNameFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right)
                .replace(R.id.businessNewBusinessFragmentContainer, fragment)
                .setReorderingAllowed(true)
                .addToBackStack("type")
                .commit();
        NewBusinessActivity.progressIndicatorChanges(1, 2);
    }

}


