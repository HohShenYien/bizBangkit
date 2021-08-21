package com.perajuritTeknologi.bizbangkit;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

public class RegistrationAccountActivity2 extends AppCompatActivity {
    private EditText username, email, password1, password2;
    private TextView back, login;
    private ImageView viewProfilePic;
    private Bitmap bitmap;
    private Button signUpButton;
    private LinearProgressIndicator pageLeft, pageRight;
    private ActivityResultLauncher<String[]> choosePicture;
    private DataStructure.UserProfileDetails userDetails = new DataStructure.UserProfileDetails();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_account2);
        receiveIntent();
        setUpComponents();
        setAllChangedText();
        saveAllTextChanges();
        onBackClicked();
        onLoginClicked();
        onSignUpClicked();
        choosePicture = registerForActivityResult(new ActivityResultContracts.OpenDocument(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    try {
                        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(result, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), DataConversion.getBitmapRotation(fileDescriptor), true);
                        parcelFileDescriptor.close();
                        viewProfilePic.setImageBitmap(bitmap);

                        String str = DataConversion.BitmapToBase64String(bitmap);
                        userDetails.profilePicture = str;
                        SharedPreferences sharedPreferences = getSharedPreferences("registerUserP2", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("profilePic", str);
                        editor.apply();
                    } catch (IOException e) {
                        Log.e("RuiJun", "Registration add profile picture error");
                    }
                }
            }
        });
    }

    private void receiveIntent() {
        Intent intent = getIntent();
        userDetails.name = intent.getStringExtra("name");
        userDetails.nric = intent.getStringExtra("nric");
        userDetails.phoneNumber = intent.getStringExtra("phone");
        userDetails.gender = intent.getStringExtra("gender");
    }

    private void setUpComponents() {
        username = findViewById(R.id.editTextRegisterUsername);
        email = findViewById(R.id.editTextRegisterEmail);
        password1 = findViewById(R.id.editTextRegisterPassword);
        password2 = findViewById(R.id.editTextRegisterPasswordConfirm);
        back = findViewById(R.id.registerBack);
        login = findViewById(R.id.loginFromRegister);
        viewProfilePic = findViewById(R.id.registerProfilePic);
        signUpButton = findViewById(R.id.registerSignUpButton);
        pageLeft = findViewById(R.id.registrationProgress1);
        pageRight = findViewById(R.id.registrationProgress2);

        pageLeft.setProgress(100);
        pageLeft.setIndicatorDirection(LinearProgressIndicator.INDICATOR_DIRECTION_RIGHT_TO_LEFT);
        pageLeft.setProgress(0, true);
        pageRight.setProgress(100, true);
    }

    private void setAllChangedText() {
        //SharedPreferences sharedPreferences = getSharedPreferences("registerUserP1", Context.MODE_PRIVATE);
        //sharedPreferences.edit().clear().apply();
        SharedPreferences sharedPreferences = getSharedPreferences("registerUserP2", Context.MODE_PRIVATE);
        String n = sharedPreferences.getString("profilePic", null);
        if (n != null) {
            Bitmap bitmap = DataConversion.Base64StringToBitmap(n);
            viewProfilePic.setImageBitmap(bitmap);
        }

        LocalStorage.setChangedText(this, username, "registerUserP2", "username");
        LocalStorage.setChangedText(this, email, "registerUserP2", "email");
        LocalStorage.setChangedText(this, password1, "registerUserP2", "password1");
        LocalStorage.setChangedText(this, password2, "registerUserP2", "password2");
    }

    private void saveAllTextChanges() {
        LocalStorage.saveCurrentTextChange(this, username, "registerUserP2", "username");
        LocalStorage.saveCurrentTextChange(this, email, "registerUserP2", "email");
        LocalStorage.saveCurrentTextChange(this, password1, "registerUserP2", "password1");
        LocalStorage.saveCurrentTextChange(this, password2, "registerUserP2", "password2");
    }

    public void onAddProfilePictureClicked(View v) {
        choosePicture.launch(new String[]{"image/*"});
    }

    private void onBackClicked() {
        back.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RegistrationAccountActivity1.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        });
    }

    private void onLoginClicked() {
        login.setOnClickListener(view -> {
            Dialog dialog = new Dialog(RegistrationAccountActivity2.this);
            dialog.setContentView(R.layout.dialog_yes_or_no);
            dialog.setCancelable(true);
            TextView loginConfirmationText = dialog.findViewById(R.id.dialogYesOrNoText);
            loginConfirmationText.setText("Proceed to login page without registering a new account?");
            Button loginConfirmationYesButton = dialog.findViewById(R.id.dialogYesButton);
            loginConfirmationYesButton.setOnClickListener(view1 -> {
                redirectToLoginPage();
            });
            Button loginConfirmationNoButton = dialog.findViewById(R.id.dialogNoButton);
            loginConfirmationNoButton.setOnClickListener(view1 -> {
                dialog.cancel();
            });
            dialog.show();
        });
    }

    private void onSignUpClicked() {
        signUpButton.setOnClickListener(view -> {
            if (onSignUpDeterminant()) {
                Dialog dialog = new Dialog(RegistrationAccountActivity2.this);
                dialog.setContentView(R.layout.dialog_yes_or_no);
                dialog.setCancelable(true);
                TextView loginConfirmationText = dialog.findViewById(R.id.dialogYesOrNoText);
                loginConfirmationText.setText(("Confirm registration with the information entered?"));
                Button loginConfirmationYesButton = dialog.findViewById(R.id.dialogYesButton);
                loginConfirmationYesButton.setOnClickListener(view1 -> {
                    userDetails.username = username.getText().toString();
                    userDetails.email = email.getText().toString();
                    userDetails.password = password1.getText().toString();
                    // set up connection with server here
                    redirectToLoginPage();
                });
                Button loginConfirmationNoButton = dialog.findViewById(R.id.dialogNoButton);
                loginConfirmationNoButton.setOnClickListener(view1 -> {
                    dialog.cancel();
                });
                dialog.show();
            }
        });
    }

    private boolean onSignUpDeterminant() {
        if (username.getText().length() == 0 || email.getText().length() == 0 ||
            password1.getText().length() == 0 || password2.getText().length() == 0) { ;
            showWarningInfo("There are incomplete fields!");
            return false;
        }
        else if (!password1.getText().toString().equals(password2.getText().toString())) {
            showWarningInfo("Passwords do not match!");
            return false;
        }
        return true;
    }

    private void showWarningInfo(String info) {
        Toast toast = Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 850);
        View view = toast.getView();
        int color = ContextCompat.getColor(getApplicationContext(), R.color.light_pink);
        view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        TextView toastText = view.findViewById(android.R.id.message);
        toastText.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light));
        toast.show();
    }

    private void redirectToLoginPage() {
        SharedPreferences sharedPreferences1 = getSharedPreferences("registerUserP1", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences2 = getSharedPreferences("registerUserP2", Context.MODE_PRIVATE);
        sharedPreferences1.edit().clear().apply();
        sharedPreferences2.edit().clear().apply();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

        startActivity(intent);
    }

}