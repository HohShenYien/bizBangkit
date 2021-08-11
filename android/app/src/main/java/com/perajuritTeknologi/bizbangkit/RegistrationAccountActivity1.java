package com.perajuritTeknologi.bizbangkit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;

public class RegistrationAccountActivity1 extends AppCompatActivity {
    private EditText name, nric1, nric2, nric3, phoneNum1, phoneNum2;
    private TextView warningInfo, login, tncGoBrowser, tncDetail;
    private CheckBox tncAgree;
    private Button continueButton;
    private LinearProgressIndicator pageLeft, pageRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_account1);
        setUpComponents();
        setTncDetail();
        setTncGoBrowser();
        setAllChangedText();
        setEditTextAware();
        saveAllTextChanges();
        onLoginClicked();
        onContinueClicked();
    }

    private void setUpComponents() {
        name = findViewById(R.id.editTextRegisterName);
        nric1 = findViewById(R.id.editTextRegisterNRIC1);
        nric2 = findViewById(R.id.editTextRegisterNRIC2);
        nric3 = findViewById(R.id.editTextRegisterNRIC3);
        phoneNum1 = findViewById(R.id.editTextRegisterPhoneNum1);
        phoneNum2 = findViewById(R.id.editTextRegisterPhoneNum2);
        warningInfo = findViewById(R.id.registerWarningInfo);
        login = findViewById(R.id.loginFromRegister);
        tncGoBrowser = findViewById(R.id.registerTnCBrowser);
        tncDetail = findViewById(R.id.registerTnCDetail);
        tncAgree = findViewById(R.id.registerTnCCheckbox);
        continueButton = findViewById(R.id.registerContinueButton);
        pageLeft = findViewById(R.id.registrationProgress1);
        pageRight = findViewById(R.id.registrationProgress2);

        pageLeft.setProgress(100, true);
        pageRight.setProgress(0);
    }

    private void setTncDetail() {
        InputStream inputStream = getResources().openRawResource(R.raw.terms_and_conditions);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String data = "";
        String line = null;
        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (line != null) {
            data += line + "\n";
            try {
                line = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        tncDetail.setText(data);
    }

    private void setTncGoBrowser() {
        tncGoBrowser.setMovementMethod(LinkMovementMethod.getInstance());
        String link = "<a href='https://pastebin.com/KqJarwWW'>(view in browser)</a>";
        tncGoBrowser.setText(Html.fromHtml(link, Html.FROM_HTML_MODE_COMPACT));
    }

    private void toNextTextField(EditText editText1, EditText editText2, int limit) {
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editText1.getText().length() == limit) {
                    editText2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void setEditTextAware() {
        toNextTextField(nric1, nric2, 6);
        toNextTextField(nric2, nric3, 2);
        toNextTextField(phoneNum1, phoneNum2, 2);
    }

    private void setAllChangedText() {
        //SharedPreferences sharedPreferences = getSharedPreferences("registerUserP1", Context.MODE_PRIVATE);
        //sharedPreferences.edit().clear().apply();
        LocalStorage.setChangedText(this, name, "registerUserP1", "name");
        LocalStorage.setChangedText(this, nric1, "registerUserP1", "nric1");
        LocalStorage.setChangedText(this, nric2, "registerUserP1", "nric2");
        LocalStorage.setChangedText(this, nric3, "registerUserP1", "nric3");
        LocalStorage.setChangedText(this, phoneNum1, "registerUserP1", "phoneNum1");
        LocalStorage.setChangedText(this, phoneNum2, "registerUserP1", "phoneNum2");
    }

    private void saveAllTextChanges() {
        LocalStorage.saveCurrentTextChange(this, name, "registerUserP1", "name");
        LocalStorage.saveCurrentTextChange(this, nric1, "registerUserP1", "nric1");
        LocalStorage.saveCurrentTextChange(this, nric2, "registerUserP1", "nric2");
        LocalStorage.saveCurrentTextChange(this, nric3, "registerUserP1", "nric3");
        LocalStorage.saveCurrentTextChange(this, phoneNum1, "registerUserP1", "phoneNum1");
        LocalStorage.saveCurrentTextChange(this, phoneNum2, "registerUserP1", "phoneNum2");
    }

    private void onLoginClicked() {
        login.setOnClickListener(view -> {
            Dialog dialog = new Dialog(RegistrationAccountActivity1.this);
            dialog.setContentView(R.layout.dialog_yes_or_no);
            dialog.setCancelable(true);
            TextView loginConfirmationText = dialog.findViewById(R.id.dialogYesOrNoText);
            loginConfirmationText.setText("Proceed to login page without registering a new account?");
            Button loginConfirmationYesButton = dialog.findViewById(R.id.dialogYesButton);
            loginConfirmationYesButton.setOnClickListener(view1 -> {
                dialog.cancel();
                redirectToLoginPage();
            });
            Button loginConfirmationNoButton = dialog.findViewById(R.id.dialogNoButton);
            loginConfirmationNoButton.setOnClickListener(view1 -> {
                dialog.cancel();
            });
            dialog.show();
        });
    }


    public final void redirectToLoginPage() {
        SharedPreferences sharedPreferences1 = getSharedPreferences("registerUserP1", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences2 = getSharedPreferences("registerUserP2", Context.MODE_PRIVATE);
        sharedPreferences1.edit().clear().apply();
        sharedPreferences2.edit().clear().apply();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void onContinueClicked() {
        warningInfo.setVisibility(View.INVISIBLE);
        continueButton.setOnClickListener(view -> {
            if (onContinueDeterminant()) {
                redirectToSecondPage();
            }
        });
    }

    private boolean onContinueDeterminant() {
        if (name.getText().length() == 0 || nric1.getText().length() !=  6 ||
                nric2.getText().length() != 2 || nric3.getText().length() != 4 ||
                phoneNum1.getText().length() != 2 || phoneNum2.getText().length() < 7) { ;
            showWarningInfo("There are incomplete fields!");
            return false;
        }
        else if (!tncAgree.isChecked()) {
            showWarningInfo("You need to agree to the terms and conditions to continue!");
            return false;
        }
        return true;
    }

    private void showWarningInfo(String info) {
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f , 0.0f );
        fadeOut.setDuration(600);
        fadeOut.setFillAfter(true);
        fadeOut.setStartOffset(1500);

        warningInfo.setText(info);
        warningInfo.setVisibility(View.VISIBLE);
        warningInfo.startAnimation(fadeOut);
    }

    private void redirectToSecondPage() {
        Intent intent = new Intent(getApplicationContext(), RegistrationAccountActivity2.class);
        intent.putExtra("name", name.getText().toString());
        intent.putExtra("nric", (nric1.getText().toString() + nric2.getText().toString() + nric3.getText().toString()));
        intent.putExtra("phone", (("60" + phoneNum1.getText().toString() + phoneNum2.getText().toString())));
        startActivity(intent);
    }
/*
    public final DataStructure.UserProfileDetails getUserDetails() {
        return new DataStructure.UserProfileDetails(
                name.getText().toString(),
                Integer.parseInt(nric1.getText().toString() + nric2.getText().toString() + nric3.getText().toString()),
                Integer.parseInt("60" + phoneNum1.getText().toString() + phoneNum2.getText().toString()),
                null,
                null,
                null);
    }*/

}