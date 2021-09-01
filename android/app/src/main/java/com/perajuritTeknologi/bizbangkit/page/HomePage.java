package com.perajuritTeknologi.bizbangkit.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.perajuritTeknologi.bizbangkit.MainActivity;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.event.GoToBusinessPage;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

public class HomePage extends Fragment {
    private View root;
    private long currentFundRequired;
    private int progressFinalValue;
    private TextView username, myBusinessText, businessName, fundingReceived, phaseNum, phaseEnding;
    private MaterialButton viewPersonalBusiness;
    private RelativeLayout personalBusinessLayout;
    private ImageView personalBusinessImage;
    private LinearProgressIndicator personalBusinessProgress;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_homepage, container, false);

        setUpComponents();
        setUpPageDetails();

        onViewPersonalBusinessClicked();
        onPersonalBusinessLayoutClicked();
        return root;
    }

    private void setUpComponents() {
        username = root.findViewById(R.id.homeUsername);
        myBusinessText = root.findViewById(R.id.homePersonalBusinessMyBusinessText);
        businessName = root.findViewById(R.id.homePersonalBusinessName);
        fundingReceived = root.findViewById(R.id.homePersonalBusinessFunds);
        phaseNum = root.findViewById(R.id.homePersonalBusinessPhase);
        phaseEnding = root.findViewById(R.id.homePersonalBusinessPhaseEnding);
        personalBusinessLayout = root.findViewById(R.id.homePersonalBusinessRelativeLayout);
        viewPersonalBusiness = root.findViewById(R.id.homeViewPersonalBusiness);
        personalBusinessImage = root.findViewById(R.id.homePersonalBusinessImage);
        personalBusinessProgress = root.findViewById(R.id.homePersonalBusinessProgress);
    }

    private void setUpPageDetails() {
        username.setText(((MainActivity)getActivity()).userProfile.username);

        if (MainActivity.businessDetails.name.equals("NO_EXISTING_BUSINESS")) {
            personalBusinessLayout.setVisibility(View.GONE);
            myBusinessText.setText(("Starting a business?"));
            viewPersonalBusiness.setText(("Begin now >"));
        }

        else {

            businessName.setText(MainActivity.businessDetails.name);
            if (MainActivity.businessDetails.phase == 1) {
                currentFundRequired = Math.round(Integer.parseInt(MainActivity.businessDetails.valuation) * 0.08);
            } else if (MainActivity.businessDetails.phase == 2) {
                currentFundRequired = Math.round(Integer.parseInt(MainActivity.businessDetails.valuation) * 0.3);
            }
            fundingReceived.setText(("RM" + MainActivity.businessDetails.shareBought + " / " + currentFundRequired));
            progressFinalValue = Math.round((float) (Integer.parseInt(MainActivity.businessDetails.shareBought) / currentFundRequired));
            personalBusinessProgress.setProgress(progressFinalValue, false);

            phaseNum.setText(String.format(Locale.getDefault(), "%d", MainActivity.businessDetails.phase));
            phaseEnding.setText("N/A"); // temporary like this
        }
    }

    private void onViewPersonalBusinessClicked() {
        viewPersonalBusiness.setOnClickListener(view -> {
            EventBus.getDefault().post(new GoToBusinessPage());
        });
    }

    private void onPersonalBusinessLayoutClicked() {
        personalBusinessLayout.setOnClickListener(view -> {
            EventBus.getDefault().post(new GoToBusinessPage());
        });
    }
}
