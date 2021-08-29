package com.perajuritTeknologi.bizbangkit.ui.business;

import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.perajuritTeknologi.bizbangkit.R;

public class BusinessExistingBusinessLicenseFragment extends Fragment {
    private View root;
    private TextView warningText;
    private TextInputEditText licenseNumber;
    private MaterialButton uploadLicenseButton, saveButton;
    private ActivityResultLauncher<String[]> choosePicture;
    private LinearLayout imageLayout;
    private AppCompatImageView licenseImage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_business_existing_license, container, false);

        setUpComponents();
        setUpUploadLicense();

        onSaveClicked();
        return root;
    }

    private void setUpComponents() {
        warningText = root.findViewById(R.id.businessLicenseWarningText);
        licenseNumber = root.findViewById(R.id.editTextExistingBusinessLicense);
        uploadLicenseButton = root.findViewById(R.id.existingBusinessLicenseUploadButton);
        saveButton = root.findViewById(R.id.existingBusinessLicenseSaveButton);
        imageLayout = root.findViewById(R.id.existingBusinessLicenseImageLayout);
        licenseImage = root.findViewById(R.id.existingBusinessLicenseImageView);

        //need to depend on the phase of the business
    }

    private void setUpUploadLicense() {
        choosePicture = registerForActivityResult(new ActivityResultContracts.OpenDocument(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    uploadLicenseButton.setText(("Change image"));
                    imageLayout.setVisibility(View.VISIBLE);
                    licenseImage.setImageURI(result);
                }
            }
        });
        uploadLicenseButton.setOnClickListener(view -> {
            choosePicture.launch(new String[] {"image/*"});
        });
    }

    private void onSaveClicked() {
        saveButton.setOnClickListener(view1 -> {
            Toast toast = Toast.makeText(root.getContext(), "License saved!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 850);
            View view = toast.getView();
            int color = ContextCompat.getColor(root.getContext(), R.color.nice_green);
            view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            TextView toastText = view.findViewById(android.R.id.message);
            toastText.setTextColor(ContextCompat.getColor(root.getContext(), android.R.color.black));
            toast.show();

            getParentFragmentManager().popBackStack();
        });
    }

}
