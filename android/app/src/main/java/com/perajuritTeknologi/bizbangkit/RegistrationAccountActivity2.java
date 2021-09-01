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
import androidx.loader.content.CursorLoader;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistrationAccountActivity2 extends AppCompatActivity {
    private EditText username, email, password1, password2;
    private TextView back, login;
    private ImageView viewProfilePic;
    private Bitmap bitmap;
    private Button signUpButton;
    private LinearProgressIndicator pageLeft, pageRight;
    private ActivityResultLauncher<String[]> choosePicture;
    public static DataStructure.UserProfileDetails userDetails = new DataStructure.UserProfileDetails();

    // 1 = not registered, 2 = successful registration, 3 = account already exists
    public static int registered = 1;

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
                    File temp = createImageFile(result);

                    userDetails.profilePicture = createImageFile(result);
                    ContentResolver resolver = getContentResolver();
                    MimeTypeMap map = MimeTypeMap.getSingleton();
                    userDetails.profilePictureMimeType = resolver.getType(result);
                    userDetails.profilePictureType = "." + map.getExtensionFromMimeType(resolver.getType(result));

                    Log.d("Ruijunnnnnnnnnnn", "file:" + Environment.getExternalStorageDirectory().toString() + result.getPath());
                    Log.d("Ruijunnnnnn filePath", temp.getAbsolutePath());

                    viewProfilePic.setImageURI(result);

                    String str = result.toString();
                    SharedPreferences sharedPreferences = getSharedPreferences("registerUserP2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("profilePic", str);
                    editor.putString("profilePicPath", result.getPath());
                    editor.putString("profilePicMimeType", userDetails.profilePictureMimeType);
                    editor.putString("profilePicType", userDetails.profilePictureType);
                    editor.apply();
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
        userDetails.dob = intent.getStringExtra("dob");
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
        SharedPreferences sharedPreferences = getSharedPreferences("registerUserP2", Context.MODE_PRIVATE);
        String n = sharedPreferences.getString("profilePic", null);
        if (n != null) {
            Bitmap bitmap = DataConversion.Base64StringToBitmap(n);
            viewProfilePic.setImageBitmap(bitmap);
        }

        String p = sharedPreferences.getString("profilePicPath", null);
        if (p != null) {
            Uri uri = Uri.parse(p);
            userDetails.profilePicture = createImageFile(uri);
        }

        String m = sharedPreferences.getString("profilePicMimeType", null);
        if (m != null) {
            userDetails.profilePictureMimeType = m;
        }

        String t = sharedPreferences.getString("profilePicType", null);
        if (t != null) {
            userDetails.profilePictureType = t;
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

    private File createImageFile(Uri uri) {
        File temp = null;
        try {
            temp = File.createTempFile("profile_pic",".jpeg",getCacheDir());
            InputStream in =  getContentResolver().openInputStream(uri);
            OutputStream out = new FileOutputStream(temp);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();

        } catch (IOException e) {
            Log.e("RuiJun", "Image file creation error", e);
        }

        return temp;
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
            loginConfirmationText.setText(("Proceed to login page without registering a new account?"));
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
                    dialog.cancel();

                    // set up connection with server
                    registerUserToServer();
                    Dialog dialog1 = setUpRegistrationPending();
                    dialog1.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (RegistrationAccountActivity2.registered == 1) {
                                Handler handler1 = new Handler();
                                handler1.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog1.cancel();
                                        showRegistrationSuccessOrFail();
                                    }
                                }, 5000);
                            }
                            else {
                                dialog1.cancel();
                                showRegistrationSuccessOrFail();
                            }
                        }
                    }, 5000);

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



    // to connect to pythonanywhere
    private static final OkHttpClient client = new OkHttpClient();

    private static final String baseUrl = "http://bizbangkit.pythonanywhere.com/";

    private static class registerTask extends AsyncTask<Request, Integer, DataStructure.UserProfileDetails> {
        @Override
        protected DataStructure.UserProfileDetails doInBackground(Request... requests) {
            Request request = requests[0];
            DataStructure.UserProfileDetails profileDetails = new DataStructure.UserProfileDetails();
            try (Response response = client.newCall(request).execute()) {

                try {
                    JSONObject jObject = new JSONObject(response.body().string());
                    String accountExist = jObject.get("message").toString();
                    Log.d("RuiJun","Account exists");
                    RegistrationAccountActivity2.registered = 3;
                } catch (JSONException e) {
                    Log.d("RuiJun","Account is new");
                    RegistrationAccountActivity2.registered = 2;
                }
                return profileDetails;

            } catch (IOException e) {
                Log.e("RuiJun", "hello there error", e);
                return profileDetails;
            }
        }

        @Override
        protected void onPostExecute(DataStructure.UserProfileDetails userProfileDetails) {
            Log.d("RuiJun", "finished server connection");
        }
    }

    private void registerUserToServer() {
        MultipartBody.Builder builder
                = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("fullname", userDetails.name)
                .addFormDataPart("ic_number", userDetails.nric)
                .addFormDataPart("username", userDetails.username)
                .addFormDataPart("password", userDetails.password)
                .addFormDataPart("dob", userDetails.dob)
                .addFormDataPart("phone_num", userDetails.phoneNumber)
                .addFormDataPart("email", userDetails.email)
                .addFormDataPart("gender", userDetails.gender)
                .addFormDataPart("user_bank_acc_no", "333333333");

        if (userDetails.profilePicture != null) {
            Log.d("Ruijunnnnnnnnnnnnnnnnnn", "profile_pic" + userDetails.profilePictureType);
            builder.addFormDataPart("file", ("profile_pic" + userDetails.profilePictureType), RequestBody.create(userDetails.profilePicture, MediaType.parse(userDetails.profilePictureMimeType)));
        }

        RequestBody requestBody = builder.build();

        Request request
                = new Request.Builder()
                .url(baseUrl + "register")
                .post(requestBody)
                .build();

        registered = 1;

        new registerTask().execute(request);
    }

    private Dialog setUpRegistrationPending() {
        Dialog dialog = new Dialog(RegistrationAccountActivity2.this);
        dialog.setContentView(R.layout.dialog_text_only);
        dialog.setCancelable(false);
        TextView completionText = dialog.findViewById(R.id.dialogTextInfo);
        completionText.setText(("Registering account..."));

        return dialog;
    }

    private void showRegistrationSuccessOrFail() {
        Dialog dialog = new Dialog(RegistrationAccountActivity2.this);
        dialog.setContentView(R.layout.dialog_ok_only);
        dialog.setCancelable(false);
        TextView completionText = dialog.findViewById(R.id.dialogOKText);
        if (registered == 2) {
            completionText.setText(("Registration successful!"));
            Button completionOKButton = dialog.findViewById(R.id.dialogOKButton);
            completionOKButton.setOnClickListener(view2 -> {
                redirectToLoginPage();
            });
        }
        else if (registered == 3) {
            completionText.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light));
            completionText.setText(("Account with the same IC number and username has already registered on BizBangkit! "));
            Button completionOKButton = dialog.findViewById(R.id.dialogOKButton);
            completionOKButton.setOnClickListener(view2 -> {
                dialog.cancel();
            });
        }
        else {
            completionText.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light));
            completionText.setText(("Unable to connect to server, please try again"));
            Button completionOKButton = dialog.findViewById(R.id.dialogOKButton);
            completionOKButton.setOnClickListener(view2 -> {
                dialog.cancel();
            });
        }
        dialog.show();
    }
}