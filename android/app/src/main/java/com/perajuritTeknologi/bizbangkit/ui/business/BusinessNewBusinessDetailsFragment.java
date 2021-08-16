package com.perajuritTeknologi.bizbangkit.ui.business;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.perajuritTeknologi.bizbangkit.R;

public class BusinessNewBusinessDetailsFragment extends Fragment {
    private View root;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_business_new_business_details, container, false);

        setUpComponents();
        setUpFragmentManager();
        onNextButtonClicked();
        onBackKeyClicked();
        return root;
    }

    private void setUpComponents() {
        nextButton = root.findViewById(R.id.businessNewBusinessDetailsNextButton);
    }

    private void setUpFragmentManager() {
        fragmentManager = getParentFragmentManager();
    }

    private void setUpFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new BusinessNewBusinessProposalFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right)
                .replace(R.id.businessNewBusinessFragmentContainer, fragment)
                .setReorderingAllowed(true)
                .addToBackStack("type")
                .commit();
        NewBusinessActivity.progressIndicatorChanges(3, 4);
    }

    private void onNextButtonClicked() {
        nextButton.setOnClickListener(view -> {
            setUpFragment();
        });
    }

    private void onBackKeyClicked() {
        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    fragmentManager.popBackStack();
                    NewBusinessActivity.progressIndicatorChanges(3, 2);
                    return true;
                }
                return false;
            }
        });
    }
}