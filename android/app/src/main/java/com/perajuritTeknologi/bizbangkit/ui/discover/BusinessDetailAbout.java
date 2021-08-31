package com.perajuritTeknologi.bizbangkit.ui.discover;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.perajuritTeknologi.bizbangkit.APICaller;
import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.event.BusinessOwnerEvent;
import com.perajuritTeknologi.bizbangkit.ui.profiles.ProfileAboutFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class BusinessDetailAbout extends Fragment {

    private View root;

    private DataStructure.BusinessProfileDetails business;
    private DataStructure.UserProfileDetails owner;
    private TextView ownerUserName, ownerName, ownerEmail, ownerPhone, businessType, businessAddress;
    private LinearLayout contents;
    private ProgressBar loading;

    public BusinessDetailAbout(DataStructure.BusinessProfileDetails business) {
        this.business = business;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_discover_business_detail_about, container, false);

        setUpComponents();
        getDetails();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void setUpComponents() {
        ownerUserName = root.findViewById(R.id.business_detail_owner_username);
        ownerName = root.findViewById(R.id.business_detail_owner_name);
        ownerEmail = root.findViewById(R.id.business_detail_owner_email);
        ownerPhone = root.findViewById(R.id.business_detail_owner_phone);
        businessType = root.findViewById(R.id.business_detail_type);
        businessAddress = root.findViewById(R.id.business_detail_address);
        contents = root.findViewById(R.id.business_detail_contents);
        loading = root.findViewById(R.id.business_detail_loading);

        contents.setVisibility(View.GONE);
    }

    private void getDetails() {
        APICaller.getBusinessOwner(business.businessId);
    }

    private void updateUserDetails() {
        ownerUserName.setText(owner.username);
        ownerName.setText(owner.name == null? "-" : owner.name);
        ownerEmail.setText(owner.email == null? "-" : owner.email);
        ownerPhone.setText(owner.phoneNumber == null? "-" : ProfileAboutFragment.parsePhone(owner.phoneNumber));
        businessType.setText(business.businessType);
        businessAddress.setText(business.principalAddress);
        contents.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }

    @Subscribe
    public void getBusinessOwner(BusinessOwnerEvent event) {
        this.owner = event.profile;
        updateUserDetails();
    }
}
