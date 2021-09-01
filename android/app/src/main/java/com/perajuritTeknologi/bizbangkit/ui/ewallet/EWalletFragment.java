package com.perajuritTeknologi.bizbangkit.ui.ewallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.button.MaterialButton;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.Utils;

import org.greenrobot.eventbus.EventBus;

public class EWalletFragment extends Fragment {
    private View root;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_ewallet, container, false);

        setUpFragmentManager();
        changeFragment();

        return root;
    }

    /*
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    */

    private void setUpFragmentManager() {
        fragmentManager = getParentFragmentManager();
    }

    private void changeFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.eWalletFragmentContainer, new EWalletBalanceFragment()).commit();
    }

    public void changeToLoadingScreen() {
        fragmentTransaction.replace(R.id.eWalletFragmentContainer, new Utils.LoadingPage()).commit();
    }



}