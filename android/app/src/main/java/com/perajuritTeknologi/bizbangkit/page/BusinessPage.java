package com.perajuritTeknologi.bizbangkit.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.ui.business.BusinessExistingBusinessFragment;
import com.perajuritTeknologi.bizbangkit.ui.business.BusinessNoBusinessFragment;

public class BusinessPage extends Fragment {
    private View root;
    public static boolean existBusiness = false;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_businesspage, container, false);

        setUpComponents();
        setUpFragmentManager();
        setUpFragmentDeterminant();

        return root;
    }

    private void setUpComponents() { }

    private void setUpFragmentManager() {
        fragmentManager = getParentFragmentManager();
    }

    private void setUpFragmentDeterminant() {
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment;
        if (existBusiness)
            fragment = new BusinessExistingBusinessFragment();
        else
            fragment = new BusinessNoBusinessFragment();
        fragmentTransaction.replace(R.id.businessFragmentContainer, fragment)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpFragmentDeterminant();
    }
}
