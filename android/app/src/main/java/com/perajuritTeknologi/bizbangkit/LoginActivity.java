package com.perajuritTeknologi.bizbangkit;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.perajuritTeknologi.bizbangkit.event.LogInEvent;
import com.perajuritTeknologi.bizbangkit.event.TabChanged;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private TextView warningTxt, registerNewAcc;
    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpComponents();
        setLoginClicked();
        setRegisterNewAccClicked();
        setInputEnterSubmit();
    }

    private void setUpComponents() {
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.etPassword);
        loginBtn = findViewById(R.id.buttonLogin);
        warningTxt = findViewById(R.id.warningTxt);
        registerNewAcc = findViewById(R.id.textView4);
    }

    private void setLoginClicked() {
        loginBtn.setOnClickListener(v -> {
            DataStructure.UserCredentials credentials;
            APICaller.logIn(email.getText().toString(),
                    password.getText().toString());

            loginBtn.setText("Logging in...");
            loginBtn.setAlpha(.5f);
            loginBtn.setClickable(false);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void setWarningTxt(String message) {
        warningTxt.setText(message);
    }

    private void showWarningTxt() {
        warningTxt.setVisibility(View.VISIBLE);
    }

    private void storeCredentials(DataStructure.UserCredentials credentials) {
        LocalStorage.setToken(credentials.token);
        LocalStorage.setID(credentials.userId);
    }

    private void redirectToMainPage() {
        Intent intent;
        intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void setInputEnterSubmit() {
        password.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                loginBtn.performClick();
            }
            return false;
        });
    }

    private void setRegisterNewAccClicked() {
        registerNewAcc.setOnClickListener(v -> {
            redirectToRegisterPage();
        });
    }

    private void redirectToRegisterPage() {
        Intent intent = new Intent(getApplicationContext(), RegistrationAccountActivity1.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.dialog_yes_or_no);
        dialog.setCancelable(true);
        TextView exitConfirmationText = dialog.findViewById(R.id.dialogYesOrNoText);
        exitConfirmationText.setText("Are you sure you want to exit BizBangkit?");
        Button exitConfirmationYesButton = dialog.findViewById(R.id.dialogYesButton);
        exitConfirmationYesButton.setOnClickListener(view -> {
            super.onBackPressed();
        });
        Button exitConfirmationNoButton = dialog.findViewById(R.id.dialogNoButton);
        exitConfirmationNoButton.setOnClickListener(view -> {
            dialog.cancel();
        });
        dialog.show();
    }

    @Subscribe
    public void onLogInEvent(LogInEvent event) {
        if (event.credentials.userId.compareTo("error") == 0) {
            setWarningTxt("Incorrect email or password!");
            showWarningTxt();
            loginBtn.setText("Login");
            loginBtn.setAlpha(1);
            loginBtn.setClickable(true);
        } else {
            storeCredentials(event.credentials);
            redirectToMainPage();
        }
    }
}