package com.perajuritTeknologi.bizbangkit.ui.settings;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.perajuritTeknologi.bizbangkit.LocalStorage;
import com.perajuritTeknologi.bizbangkit.LoginActivity;
import com.perajuritTeknologi.bizbangkit.R;

public class SettingsFragment extends Fragment {
    private View root;
    private MaterialButton logOutBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_settings, container, false);

        setUpComponents();
        setUpLogOutButton();

        return root;
    }

    private void setUpComponents() {
        logOutBtn = root.findViewById(R.id.buttonLogout);
    }

    private void setUpLogOutButton() {
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutMessage();
                logOut();
            }
        });
    }

    private void logOutMessage() {
        Toast toast = Toast.makeText(root.getContext(),
                "Log out successfully!",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        setUpToastColor(toast);
        toast.show();
    }

    private void setUpToastColor(Toast toast) {
        View view = toast.getView();
        int color = ContextCompat.getColor(root.getContext(), R.color.nice_green);
        view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    private void logOut() {
        LocalStorage.clearCredentials();
        Intent intent = new Intent(root.getContext(), LoginActivity.class);
        startActivity(intent);
    }
}