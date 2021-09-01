package com.perajuritTeknologi.bizbangkit.ui.ewallet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.perajuritTeknologi.bizbangkit.APICaller;
import com.perajuritTeknologi.bizbangkit.LocalStorage;
import com.perajuritTeknologi.bizbangkit.MainActivity;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.Utils;

public class EWalletBalanceFragment extends Fragment {
    private View root;
    private TextView walletBalance;
    private MaterialButton reloadButton, cashOutButton;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_ewallet_balance, container, false);

        setUpComponents();
        setUpBalanceAmount();
        setUpFragmentManager();

        onReloadClicked();
        onCashOutClicked();

        return root;
    }

    private void setUpComponents() {
        walletBalance = root.findViewById(R.id.eWalletBalanceAmount);
        reloadButton = root.findViewById(R.id.eWalletReloadButton);
        cashOutButton = root.findViewById(R.id.eWalletCashOutButton);
    }

    private void setUpBalanceAmount() {
        walletBalance.setText(("RM " + MainActivity.eWalletBalance.balance));
    }

    private void setUpFragmentManager() {
        fragmentManager = getParentFragmentManager();
    }

    private void onReloadClicked() {
        reloadButton.setOnClickListener(view -> {
            Dialog dialog = createDialog();
            EditText reloadAmount = dialog.findViewById(R.id.editTextBalanceAmount);
            Button confirmButton = dialog.findViewById(R.id.dialogConfirmButton);
            confirmButton.setText(("Reload"));
            confirmButton.setOnClickListener(view1 -> {
                dialog.cancel();
                APICaller.changeWalletBalance(LocalStorage.getID(), LocalStorage.getToken(), reloadAmount.getText().toString(), "reload");
                changeToLoadingScreen();
            });
            dialog.show();
        });
    }

    private void onCashOutClicked() {
        cashOutButton.setOnClickListener(view -> {
            Dialog dialog = createDialog();
            EditText reloadAmount = dialog.findViewById(R.id.editTextBalanceAmount);
            Button confirmButton = dialog.findViewById(R.id.dialogConfirmButton);
            confirmButton.setText(("Cash Out"));
            confirmButton.setOnClickListener(view1 -> {
                dialog.cancel();
                APICaller.changeWalletBalance(LocalStorage.getID(), LocalStorage.getToken(), reloadAmount.getText().toString(), "withdraw");
                changeToLoadingScreen();
            });
            dialog.show();
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

    private void changeToLoadingScreen() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.eWalletFragmentContainer, new Utils.LoadingPage())
                .commit();
    }

}