package com.perajuritTeknologi.bizbangkit;

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

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private TextView warningTxt;
    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpComponents();
        setLoginClicked();
        setInputEnterSubmit();
    }

    private void setUpComponents() {
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.etPassword);
        loginBtn = findViewById(R.id.buttonLogin);
        warningTxt = findViewById(R.id.warningTxt);
    }

    private void setLoginClicked() {
        loginBtn.setOnClickListener(v -> {
            DataStructure.UserCredentials credentials;
            try {
                credentials = APICaller.logIn(email.getText().toString(),
                        password.getText().toString());
                storeCredentials(credentials);
                redirectToMainPage();

            } catch (APICaller.LogInFails logInFails) {
                setWarningTxt(logInFails.getMessage());
                showWarningTxt();
            }
        });
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
}