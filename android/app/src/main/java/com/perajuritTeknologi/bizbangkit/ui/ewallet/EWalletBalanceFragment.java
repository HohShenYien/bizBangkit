package com.perajuritTeknologi.bizbangkit.ui.ewallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.perajuritTeknologi.bizbangkit.R;

public class EWalletBalanceFragment extends Fragment {
    private View root;
    private TextView walletBalance;
    private MaterialButton reloadButton, cashOutButton;
    private EWalletFragment eWalletFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_ewallet_balance, container, false);

        setUpComponents();

        return root;
    }

    private void setUpComponents() {
        walletBalance = root.findViewById(R.id.eWalletBalanceAmount);
        reloadButton = root.findViewById(R.id.eWalletReloadButton);
        cashOutButton = root.findViewById(R.id.eWalletCashOutButton);
    }

    private void onReloadClicked() {
        reloadButton.setOnClickListener(view -> {

        });
    }

    private void onCashOutClicked() {
        cashOutButton.setOnClickListener(view -> {

        });
    }


}