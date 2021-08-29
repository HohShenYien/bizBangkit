package com.perajuritTeknologi.bizbangkit.ui.business;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.perajuritTeknologi.bizbangkit.BuildConfig;
import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.MainActivity;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.RegistrationAccountActivity2;
import com.perajuritTeknologi.bizbangkit.page.BusinessPage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BusinessNewBusinessProposalFragment extends Fragment {
    private View root;
    private TextInputEditText businessDescription;
    private CardView businessVideo, businessUpload;
    private ActivityResultLauncher<Uri> takeVideo;
    private ActivityResultLauncher<String[]> chooseVideo;
    private TextView filePath;
    private ImageView backButton;
    private Button finishButton;
    private FragmentManager fragmentManager;

    // 1 = not registered, 2 = successful registration, 3 = account already exists
    public static int registered = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_business_new_business_proposal, container, false);

        setUpComponents();
        saveText();
        setUpTakeVideoClicked();
        onTakeVideoClicked();
        setUpUploadClicked();
        onUploadClicked();
        setUpFragmentManager();
        onBackButtonClicked();
        onFinishButtonClicked();

        return root;
    }

    private void setUpComponents() {
        businessDescription = root.findViewById(R.id.editTextRegisterProposalText);
        businessVideo = root.findViewById(R.id.businessVideoCardView);
        businessUpload = root.findViewById(R.id.businessUploadCardView);
        filePath = root.findViewById(R.id.businessVideoFile);
        backButton = root.findViewById(R.id.businessNewBusinessProposalBackButton);
        finishButton = root.findViewById(R.id.businessNewBusinessProposalFinishButton);
    }

    private void saveText() {
        businessDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0)
                    NewBusinessActivity.businessProfileDetails.shortDescription = null;
                else
                    NewBusinessActivity.businessProfileDetails.shortDescription = editable.toString();
            }
        });
    }

    private void setUpTakeVideoClicked() {
        takeVideo = registerForActivityResult(new ActivityResultContracts.TakeVideo(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                filePath.setText(("Video: proposal.mp4"));
            }
        });
    }

    private void onTakeVideoClicked() {
        businessVideo.setOnClickListener(view -> {
            takeVideo.launch(getUri());
        });
    }

    private Uri getUri() {
        String fileName = root.getContext().getCacheDir() + "/proposal.mp4";
        File file = new File(fileName);
        Log.d("Rui Junnnnnnnnnnnnnnnnnnnn", fileName);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.d("Rui Junnnnnnnnnnnnnnnnnnnn URI", Uri.fromFile(file).toString());
            return FileProvider.getUriForFile(root.getContext(), BuildConfig.APPLICATION_ID, file);
        }
        else {
            return Uri.fromFile(file);
        }*/
        Log.d("Rui Junnnnnnnnnnnnnnnnnnnn URI", Uri.fromFile(file).toString());
        return Uri.fromFile(file);
    }

    private void setUpUploadClicked() {
        chooseVideo = registerForActivityResult(new ActivityResultContracts.OpenDocument(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    filePath.setText(("Video: proposal.mp4"));
                }
            }
        });
    }

    private void onUploadClicked() {
        businessUpload.setOnClickListener(view -> {
            chooseVideo.launch(new String[] {"video/*"});
        });
    }

    private void onBackButtonClicked() {
        backButton.setOnClickListener(view -> {
            fragmentManager.popBackStack();
            NewBusinessActivity.progressIndicatorChanges(4,3);
            NewBusinessActivity.currentFragmentPos = 3;
        });
    }

    private void setUpFragmentManager() {
        fragmentManager = getParentFragmentManager();
    }

    private void onFinishButtonClicked() {
        finishButton.setOnClickListener(view -> {
            if (onFinishDeterminant()) {
                Dialog dialog = new Dialog(root.getContext());
                dialog.setContentView(R.layout.dialog_yes_or_no);
                dialog.setCancelable(true);
                TextView completionConfirmation = dialog.findViewById(R.id.dialogYesOrNoText);
                completionConfirmation.setText(("Confirm registration with the information entered?"));
                Button completionYesButton = dialog.findViewById(R.id.dialogYesButton);
                completionYesButton.setOnClickListener(view1 -> {
                    dialog.cancel();

                    // set up connection to server and send data
                    registerUserToServer();
                    Dialog dialog1 = setUpRegistrationPending();
                    dialog1.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (BusinessNewBusinessProposalFragment.registered == 1) {
                                Handler handler1 = new Handler();
                                handler1.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog1.cancel();
                                        showRegistrationSuccessOrFail();
                                    }
                                }, 5000);
                            } else {
                                dialog1.cancel();
                                showRegistrationSuccessOrFail();
                            }
                        }
                    }, 5000);
                });
                Button completionNoButton = dialog.findViewById(R.id.dialogNoButton);
                completionNoButton.setOnClickListener(view1 -> {
                    dialog.cancel();
                });
                dialog.show();
            }
        });
    }

    private boolean onFinishDeterminant() {
        if (businessDescription.getText().length() == 0) {
            return makeToastWarning();
        }
        return true;
    }

    private boolean makeToastWarning() {
        Toast toast = Toast.makeText(root.getContext(), "There are incomplete fields!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 850);
        View view = toast.getView();
        int color = ContextCompat.getColor(root.getContext(), R.color.light_pink);
        view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        TextView toastText = view.findViewById(android.R.id.message);
        toastText.setTextColor(ContextCompat.getColor(root.getContext(), android.R.color.holo_red_light));
        toast.show();
        return false;
    }




    // to connect to pythonanywhere
    private static final OkHttpClient client = new OkHttpClient();

    private static final String baseUrl = "http://bizbangkit.pythonanywhere.com/";

    private static class registerTask extends AsyncTask<Request, Integer, DataStructure.BusinessProfileDetails> {
        @Override
        protected DataStructure.BusinessProfileDetails doInBackground(Request... requests) {
            Request request = requests[0];
            DataStructure.BusinessProfileDetails profileDetails = new DataStructure.BusinessProfileDetails();
            try (Response response = client.newCall(request).execute()) {
                JSONObject jObject = new JSONObject(response.body().string());
                try {
                    String accountExist = jObject.get("message").toString();
                    Log.d("RuiJun","Business exists");
                    BusinessNewBusinessProposalFragment.registered = 3;
                } catch (JSONException e) {
                    Log.d("RuiJun","Business is new");
                    MainActivity.businessDetails.type = jObject.get("bus_phase").toString();
                    MainActivity.businessDetails.name = jObject.get("bus_name").toString();
                    MainActivity.businessDetails.valuation = jObject.get("bus_valuation").toString();
                    MainActivity.businessDetails.commencementDate = jObject.get("bus_start_date").toString();
                    MainActivity.businessDetails.shareBought = String.format(Locale.getDefault(),"%d",Math.round(Float.parseFloat(MainActivity.businessDetails.valuation)
                            * jObject.getInt("share_bought")/100f));
                    Log.d("RuiJun","5");
                    BusinessNewBusinessProposalFragment.registered = 2;
                }
                return profileDetails;

            } catch (IOException | JSONException e) {
                Log.e("RuiJun", "either server connection problem or JSONObject problem", e);
                return profileDetails;
            }
        }

        @Override
        protected void onPostExecute(DataStructure.BusinessProfileDetails businessProfileDetails) {
            Log.d("RuiJun", "finished server connection");
        }
    }

    private void registerUserToServer() {
        MultipartBody.Builder builder
                = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", NewBusinessActivity.userID)
                .addFormDataPart("bus_name", NewBusinessActivity.businessProfileDetails.name)
                .addFormDataPart("bus_type", NewBusinessActivity.businessProfileDetails.businessType)
                .addFormDataPart("bus_valuation", NewBusinessActivity.businessProfileDetails.valuation)
                .addFormDataPart("bus_total_emp", "0")
                .addFormDataPart("bus_current_emp", "0")
                .addFormDataPart("bus_phone", NewBusinessActivity.phoneNum)
                .addFormDataPart("bus_email", NewBusinessActivity.userEmail)
                .addFormDataPart("bus_bank_acc", NewBusinessActivity.userID+"99")
                .addFormDataPart("bus_ops_start_time", "09:00")
                .addFormDataPart("bus_ops_end_time", "05:30")
                .addFormDataPart("bus_day", NewBusinessActivity.businessProfileDetails.commencementDate)
                .addFormDataPart("bus_reg_address", NewBusinessActivity.businessProfileDetails.principalAddress)
                .addFormDataPart("bus_loc_address_city", NewBusinessActivity.businessProfileDetails.branchAddress);

        if (NewBusinessActivity.businessProfileDetails.type.equals("New")) {
            builder.addFormDataPart("bus_lic_no", "1");
        }
        else if (NewBusinessActivity.businessProfileDetails.type.equals("Existing")){
            builder.addFormDataPart("bus_lic_no", "2");
        }

        if (NewBusinessActivity.businessProfileDetails.licenseNumber != null) {
            builder.addFormDataPart("bus_ssm_id", NewBusinessActivity.businessProfileDetails.licenseNumber);
        }
        else {
            builder.addFormDataPart("bus_ssm_id", "0");
        }

        RequestBody requestBody = builder.build();

        Request request
                = new Request.Builder()
                .url(baseUrl + "register/business")
                .post(requestBody)
                .build();

        registered = 1;

        new registerTask().execute(request);
    }



    private Dialog setUpRegistrationPending() {
        Dialog dialog = new Dialog(root.getContext());
        dialog.setContentView(R.layout.dialog_text_only);
        dialog.setCancelable(false);
        TextView completionText = dialog.findViewById(R.id.dialogTextInfo);
        completionText.setText(("Registering account..."));

        return dialog;
    }

    private void showRegistrationSuccessOrFail() {
        Dialog dialog = new Dialog(root.getContext());
        dialog.setContentView(R.layout.dialog_ok_only);
        dialog.setCancelable(false);
        TextView completionText = dialog.findViewById(R.id.dialogOKText);
        if (registered == 2) {
            completionText.setText(("Registration successful!"));
            Button completionOKButton = dialog.findViewById(R.id.dialogOKButton);
            completionOKButton.setOnClickListener(view2 -> {
                BusinessPage.existBusiness = true;
                getActivity().finish();
            });
        }
        else if (registered == 3) {
            completionText.setTextColor(ContextCompat.getColor(root.getContext(), android.R.color.holo_red_light));
            completionText.setText(("Account with the same business name, email and bank account has already registered on BizBangkit! "));
            Button completionOKButton = dialog.findViewById(R.id.dialogOKButton);
            completionOKButton.setOnClickListener(view2 -> {
                dialog.cancel();
            });
        }
        else {
            completionText.setTextColor(ContextCompat.getColor(root.getContext(), android.R.color.holo_red_light));
            completionText.setText(("Unable to connect to server, please try again"));
            Button completionOKButton = dialog.findViewById(R.id.dialogOKButton);
            completionOKButton.setOnClickListener(view2 -> {
                dialog.cancel();
            });
        }
        dialog.show();
    }

}