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

import com.perajuritTeknologi.bizbangkit.MainActivity;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.Utils;
import com.perajuritTeknologi.bizbangkit.event.GetPersonalBusinessDetails;
import com.perajuritTeknologi.bizbangkit.ui.business.BusinessExistingBusinessFragment;
import com.perajuritTeknologi.bizbangkit.ui.business.BusinessNoBusinessFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class BusinessPage extends Fragment {
    private View root;
    public static boolean existBusiness;
    private boolean fromNewBusiness;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_businesspage, container, false);

        setUpFragmentManager();
        setUpFragmentDeterminant();

        return root;
    }

    private void setUpFragmentManager() {
        fragmentManager = getParentFragmentManager();
    }

    private void setUpFragmentDeterminant() {
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment;

        existBusiness = !MainActivity.businessDetails.name.equals("NO_EXISTING_BUSINESS");

        if (existBusiness) {
            fragment = new BusinessExistingBusinessFragment();
            fromNewBusiness = false;
        } else {
            fragment = new BusinessNoBusinessFragment();
            fromNewBusiness = true;
        }

        fragmentTransaction.replace(R.id.businessFragmentContainer, fragment)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fromNewBusiness) {
            setUpFragmentDeterminant();
        }
    }
}
