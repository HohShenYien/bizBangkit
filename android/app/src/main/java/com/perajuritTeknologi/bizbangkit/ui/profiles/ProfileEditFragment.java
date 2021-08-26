package com.perajuritTeknologi.bizbangkit.ui.profiles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.MainActivity;
import com.perajuritTeknologi.bizbangkit.R;

public class ProfileEditFragment extends Fragment {
    private View root;
    private TextInputEditText about, name, username, email, phone_start, phone_end, nric_start,
            nric_mid, nric_end, address;
    private Spinner gender;
    private MaterialButton saveBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.profile_edit, container, false);
        setUpComponents();
        getDetails();
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
    }

    private void getDetails() {
        DataStructure.UserProfileDetails user = ((MainActivity)getActivity()).userProfile;
        about.setText(user.aboutme == null ? "This user is very lazy and didn't include an about me" : user.aboutme);
        name.setText(user.name);
        username.setText(user.username);
        email.setText(user.email);
        phone_start.setText(user.phoneNumber.substring(0, 2));
        phone_end.setText(user.phoneNumber.substring(2));

        String nric = ProfileAboutFragment.parseIc(user.nric);
        nric_start.setText(nric.substring(0, 6));
        nric_mid.setText(nric.substring(7, 9));
        nric_end.setText(nric.substring(10));
        address.setText(user.address);
        gender.setSelection(user.gender.compareTo("M") == 0 ? 0: 1);
    }

}
