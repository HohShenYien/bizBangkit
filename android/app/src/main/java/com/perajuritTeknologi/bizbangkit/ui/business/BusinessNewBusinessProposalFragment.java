package com.perajuritTeknologi.bizbangkit.ui.business;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.perajuritTeknologi.bizbangkit.BuildConfig;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.page.BusinessPage;

import java.io.File;

public class BusinessNewBusinessProposalFragment extends Fragment {
    private View root;
    private EditText businessDescription;
    private CardView businessVideo, businessUpload;
    private ActivityResultLauncher<Uri> takeVideo;
    private ActivityResultLauncher<String[]> chooseVideo;
    private TextView filePath;
    private ImageView backButton;
    private Button finishButton;
    private FragmentManager fragmentManager;


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
            Dialog dialog = new Dialog(root.getContext());
            dialog.setContentView(R.layout.dialog_yes_or_no);
            dialog.setCancelable(true);
            TextView completionConfirmation = dialog.findViewById(R.id.dialogYesOrNoText);
            completionConfirmation.setText(("Confirm registration with the information entered?"));
            Button completionYesButton = dialog.findViewById(R.id.dialogYesButton);
            completionYesButton.setOnClickListener(view1 -> {
                dialog.cancel();
                Dialog dialog1 = new Dialog(root.getContext());
                dialog1.setContentView(R.layout.dialog_ok_only);
                dialog1.setCancelable(false);
                TextView completionText = dialog1.findViewById(R.id.dialogOKText);
                completionText.setText(("Registration Completed!"));
                Button completionOKButton = dialog1.findViewById(R.id.dialogOKButton);
                completionOKButton.setOnClickListener(view2 -> {
                    BusinessPage.existBusiness = true;
                    getActivity().finish();
                });
                dialog1.show();
            });
            Button completionNoButton = dialog.findViewById(R.id.dialogNoButton);
            completionNoButton.setOnClickListener(view1 -> {
                dialog.cancel();
            });
            dialog.show();
        });
    }
}