package com.perajuritTeknologi.bizbangkit.ui.profiles;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ScrollingView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.MainActivity;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.event.ProfileScrolled;

import org.greenrobot.eventbus.EventBus;

public class ProfileAboutFragment extends Fragment {
    private View root;
    private NestedScrollView scrollingView;
    private TextView name, username, email, gender, phone, address, nric, about;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile_general, container, false);
        setUpScrollView();
        setUpComponents();
        setUpDetails();
        return root;
    }

    private void setUpScrollView() {
        scrollingView = root.findViewById(R.id.profile_scroll);
        scrollingView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                EventBus.getDefault().post(new ProfileScrolled(Math.min(scrollY, 300)));
            }
        });
    }

    private void setUpComponents() {
        name = root.findViewById(R.id.profile_name);
        email = root.findViewById(R.id.profile_email);
        username = root.findViewById(R.id.profile_username);
        gender = root.findViewById(R.id.profile_gender);
        phone = root.findViewById(R.id.profile_phone);
        address = root.findViewById(R.id.profile_address);
        nric = root.findViewById(R.id.profile_ic);
        about = root.findViewById(R.id.profile_about);
    }

    private void setUpDetails() {
        DataStructure.UserProfileDetails user = ((MainActivity) getActivity()).userProfile;
        name.setText(user.name);
        username.setText(user.username);
        email.setText(user.email);
        gender.setText(parseGender(user.gender));
        nric.setText(parseIc(user.nric));
        address.setText(user.address.compareTo("null") == 0 ? "Not available" : user.address);
        phone.setText(parsePhone(user.phoneNumber));
        about.setText(user.aboutme.compareTo("null") == 0 ? "This user is very lazy and didn't include an about me" : user.aboutme);
    }

    public static String parseGender(String gender) {
        switch (gender){
            case "M":
                return "Male";
            case "F":
                return "Female";
            default:
                return "-";
        }
    }

    public static String parseIc(String nric) {
        StringBuilder result = new StringBuilder();
        String paddedIC = String.format("%012d", Long.parseLong(nric));
        result.append(paddedIC.substring(0, 6));
        result.append("-");
        result.append(paddedIC.substring(6, 8));
        result.append("-");
        result.append(paddedIC.substring(8));
        return result.toString();
    }

    public static String parsePhone(String phone) {
        return "+60" + phone;
    }
}
