package com.perajuritTeknologi.bizbangkit.ui.business;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.page.BusinessPage;

public class BusinessNewBusinessProposalFragment extends Fragment {
    private View root;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Button finishButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_business_new_business_proposal, container, false);

        setUpComponents();
        setUpFragmentManager();
        onBackKeyClicked();
        onFinishButtonClicked();
        return root;
    }

    private void setUpComponents() {
        finishButton = root.findViewById(R.id.businessNewBusinessProposalFinishButton);
    }

    private void setUpFragmentManager() {
        fragmentManager = getParentFragmentManager();
    }

    private void onFinishButtonClicked() {
        finishButton.setOnClickListener(view -> {
            BusinessPage.existBusiness = true;
            getActivity().finish();
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
                    NewBusinessActivity.progressIndicatorChanges(4, 3);
                    return true;
                }
                return false;
            }
        });
    }
}