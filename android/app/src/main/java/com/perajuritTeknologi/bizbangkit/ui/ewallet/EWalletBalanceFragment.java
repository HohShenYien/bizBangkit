package com.perajuritTeknologi.bizbangkit.ui.ewallet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.perajuritTeknologi.bizbangkit.MainActivity;
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
        setUpBalanceAmount();

        return root;
    }

    private void setUpComponents() {
        walletBalance = root.findViewById(R.id.eWalletBalanceAmount);
        reloadButton = root.findViewById(R.id.eWalletReloadButton);
        cashOutButton = root.findViewById(R.id.eWalletCashOutButton);
    }

    private void setUpBalanceAmount() {
        walletBalance.setText(MainActivity.eWalletBalance.balance);
    }

    private void onReloadClicked() {
        reloadButton.setOnClickListener(view -> {
            Dialog dialog = createDialog();
            Button confirmButton = dialog.findViewById(R.id.dialogConfirmButton);
            confirmButton.setText(("Reload"));
            confirmButton.setOnClickListener(view1 -> {

            });
        });
    }

    private void onCashOutClicked() {
        cashOutButton.setOnClickListener(view -> {

        });
    }

    private Dialog createDialog() {
        Dialog dialog = new Dialog(root.getContext());
        dialog.setContentView(R.layout.dialog_change_wallet_balance);
        dialog.setCancelable(true);
        Button cancelActionButton = dialog.findViewById(R.id.dialogCancelButton);
        cancelActionButton.setOnClickListener(view -> {
            dialog.cancel();
        });

        return dialog;
    }

}