package com.perajuritTeknologi.bizbangkit.ui.profiles;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.perajuritTeknologi.bizbangkit.APICaller;
import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.MainActivity;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.page.ProfilePage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProfileEditFragment extends Fragment {
    private View root;
    private LinearLayout goBackBtn;
    private TextInputEditText about, name, username, email, phone_start, phone_end, nric_start,
            nric_mid, nric_end, address;
    private Spinner gender;
    private MaterialButton saveBtn;
    private ActivityResultLauncher<String[]> choosePicture;
    private DataStructure.UserProfileDetails userDetails;
    private ImageView userImg;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.profile_edit, container, false);

        setUpComponents();
        getDetails();
        setGetImgActivity();
        setEditUserImg();
        setUpSaveBtn();
        setUpBackBtn();

        return root;
    }

    private void setUpComponents() {
        about = root.findViewById(R.id.profile_edit_about);
        name = root.findViewById(R.id.profile_edit_name);
        username = root.findViewById(R.id.profile_edit_username);
        email = root.findViewById(R.id.profile_edit_email);
        phone_start = root.findViewById(R.id.profile_edit_phone_start);
        phone_end = root.findViewById(R.id.profile_edit_phone_end);
        nric_start = root.findViewById(R.id.profile_edit_ic_first);
        nric_mid = root.findViewById(R.id.profile_edit_ic_mid);
        nric_end = root.findViewById(R.id.profile_edit_ic_end);
        address = root.findViewById(R.id.profile_edit_address);
        gender = root.findViewById(R.id.profile_edit_gender);
        saveBtn = root.findViewById(R.id.profile_edit_save_btn);
        userImg = root.findViewById(R.id.profile_edit_img);
        goBackBtn = root.findViewById(R.id.edit_go_back_btn);

        userDetails = new DataStructure.UserProfileDetails();
    }

    private void getDetails() {
        DataStructure.UserProfileDetails user = ((MainActivity)getActivity()).userProfile;
        about.setText(user.aboutme.compareTo("null") == 0 ? "This user is very lazy and didn't include an about me" : user.aboutme);
        name.setText(user.name);
        username.setText(user.username);
        email.setText(user.email);
        phone_start.setText(user.phoneNumber.substring(0, 2));
        phone_end.setText(user.phoneNumber.substring(2));

        String nric = ProfileAboutFragment.parseIc(user.nric);
        nric_start.setText(nric.substring(0, 6));
        nric_mid.setText(nric.substring(7, 9));
        nric_end.setText(nric.substring(10));
        address.setText(user.address.compareTo("null") == 0 ? "Not available" : user.address);
        gender.setSelection(user.gender.compareTo("M") == 0 ? 0: 1);

        Bitmap userImg = ((MainActivity) getActivity()).userImg;
        if (userImg == null) {
            if (user.gender.compareTo("M") == 0) {
                this.userImg.setImageResource(R.drawable.male_default);
            } else {
                this.userImg.setImageResource(R.drawable.female_default);
            }
        } else {
            this.userImg.setImageBitmap(userImg);
        }
    }

    private void setGetImgActivity() {
        choosePicture = registerForActivityResult(new ActivityResultContracts.OpenDocument(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    userDetails.profilePicture = createImageFile(result);
                    ContentResolver resolver = getActivity().getContentResolver();
                    MimeTypeMap map = MimeTypeMap.getSingleton();
                    userDetails.profilePictureMimeType = resolver.getType(result);
                    userDetails.profilePictureType = "." + map.getExtensionFromMimeType(resolver.getType(result));

                    userImg.setImageURI(result);
                    Log.d("ShenYien", result.toString());
                }
            }
        });
    }

    private File createImageFile(Uri uri) {
        File temp = null;
        try {
            temp = File.createTempFile("profile_pic",".jpeg", getContext().getCacheDir());
            InputStream in =  getActivity().getContentResolver().openInputStream(uri);
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

    private void setEditUserImg() {
        userImg.setOnClickListener(v -> {
            choosePicture.launch(new String[]{"image/*"});
        });
    }

    private void setUpSaveBtn() {
        saveBtn.setOnClickListener(v -> {
            userDetails.gender = gender.getSelectedItem().toString().compareTo("Male") == 0 ? "M" : "F";
            userDetails.username = username.getText().toString();
            userDetails.name = name.getText().toString();
            userDetails.address = address.getText().toString();
            userDetails.aboutme = about.getText().toString();
            userDetails.email = email.getText().toString();
            userDetails.phoneNumber = phone_start.getText().toString() + phone_end.getText().toString();
            userDetails.nric = nric_start.getText().toString() + nric_mid.getText().toString() +
                    nric_end.getText().toString();
            userDetails.dob = ((MainActivity)getActivity()).userProfile.dob;
            userDetails.userId = ((MainActivity)getActivity()).userProfile.userId;

            saveBtn.setText("Saving...");
            saveBtn.setAlpha(0.5f);
            saveBtn.setClickable(false);

            APICaller.saveProfile(userDetails);
        });
    }

    private void setUpBackBtn() {
        goBackBtn.setOnClickListener(v -> {
            ((MainActivity)getActivity()).changeFragment(new ProfilePage());
        });
    }
}
