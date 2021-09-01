package com.perajuritTeknologi.bizbangkit.ui.business;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.perajuritTeknologi.bizbangkit.MainActivity;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.ui.discover.BusinessDetailInvestors;

import java.util.Locale;

public class BusinessExistingBusinessFragment extends Fragment {
    private View root;
    private long currentFundRequired;
    private int progressFinalValue;
    private RelativeLayout progressBackground;
    private TextView businessName, progressPercent, progressValues, proposedDate, phaseNumText, phaseEndText, workersEmployedText;
    private CircularProgressIndicator progressIndicator;
    private MaterialButton discussionButton, licenseButton, workersButton, financialDetailsButton, regretButton, cheatingButton;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    // phase number to determine what to show
    public static boolean cheated = false;
    public static int phaseNum = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_business_existing_business, container, false);

        setUpComponents();
        setUpFragmentManager();

        setUpPhaseDeterminant();

        onPrevPhaseClicked();
        onFastForwardClicked();
        onLicenseClicked();
        onFinancialDetailsClicked();

        return root;
    }

    private void setUpComponents() {
        businessName = root.findViewById(R.id.businessExistingBusinessName);
        progressBackground = root.findViewById(R.id.businessExistingBusinessProgressRelativeLayout);
        progressPercent = root.findViewById(R.id.businessExistingBusinessProgressNumber);
        progressValues = root.findViewById(R.id.businessExistingBusinessFundsNumber);
        progressIndicator = root.findViewById(R.id.businessExistingBusinessProgressIndicator);
        proposedDate = root.findViewById(R.id.businessExistingBusinessProposedDate);
        phaseNumText = root.findViewById(R.id.businessExistingBusinessPhase);
        phaseEndText = root.findViewById(R.id.businessExistingBusinessPhaseEnding);
        workersEmployedText = root.findViewById(R.id.businessExistingBusinessWorkersEmployed);
        discussionButton = root.findViewById(R.id.businessExistingBusinessDiscussionPage);
        licenseButton = root.findViewById(R.id.businessExistingBusinessPermitLicense);
        workersButton = root.findViewById(R.id.businessExistingBusinessSeekWorkers);
        financialDetailsButton = root.findViewById(R.id.businessExistingBusinessFinancialDetails);

        regretButton = root.findViewById(R.id.existingBusinessRegretButton);
        cheatingButton = root.findViewById(R.id.existingBusinessCheatingButton);
    }

    private void setUpFragmentManager() {
        fragmentManager = getParentFragmentManager();
    }

    private void setUpDetails() {
        // setting up business name
        businessName.setText(MainActivity.businessDetails.name);


        // setting up progress bar section (left side)
        if (!cheated) {
            if (MainActivity.businessDetails.phase == 1) {
                currentFundRequired = Math.round(Integer.parseInt(MainActivity.businessDetails.valuation) * 0.08);
                phaseNum = 1;
            } else if (MainActivity.businessDetails.phase == 2) {
                currentFundRequired = Math.round(Integer.parseInt(MainActivity.businessDetails.valuation) * 0.3);
                phaseNum = 2;
            }
            progressValues.setText(("RM " + MainActivity.businessDetails.shareBought + " / " + currentFundRequired));
            progressFinalValue = Math.round((Float.parseFloat(MainActivity.businessDetails.shareBought) / (float)currentFundRequired) * 100);
            Log.d("RuiJun progress", String.format("%d", progressFinalValue));
            setUpProgress(progressFinalValue);
        }


        // setting up business details section (right side)
        proposedDate.setText(MainActivity.businessDetails.commencementDate);
        phaseNumText.setText(String.format(Locale.getDefault(),"%d", MainActivity.businessDetails.phase));


    }

    private void setUpPhaseDeterminant() {
        setUpDetails();
        if (cheated) {
            if (phaseNum == 2) {
                setUpProgress(75);
                progressValues.setText(("RM 5,250 / 7,000"));
                phaseNumText.setText(("2"));
                workersEmployedText.setText(("3"));

                financialDetailsButton.setEnabled(false);
                financialDetailsButton.setAlpha(0.5f);
            }
            else if (phaseNum == 3) {
                setUpProgress(100);
                progressValues.setText(("RM 7,000 / 7,000"));
                phaseNumText.setText(("3"));
                phaseEndText.setText(("95 days"));
                workersEmployedText.setText(("5"));
            }
            else if (phaseNum == 4) {
                setUpProgress(100);
                progressValues.setText(("RM 7,000 / 7,000"));
                phaseNumText.setText(("4"));
                phaseEndText.setText(("155 days"));
                workersEmployedText.setText(("5"));
            }
        }
        else {
            if (MainActivity.businessDetails.type.equals("1")) {
                licenseButton.setEnabled(false);
                licenseButton.setAlpha(0.5f);
                workersButton.setEnabled(false);
                workersButton.setAlpha(0.5f);
            }
            financialDetailsButton.setEnabled(false);
            financialDetailsButton.setAlpha(0.5f);
        }
    }

    // will set depending on data from server (sample for now)
    private void setUpProgress(int progressFinalValue) {
        ValueAnimator animator = ValueAnimator.ofInt(0, progressFinalValue);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                progressPercent.setText((valueAnimator.getAnimatedValue().toString() + "%"));
            }
        });
        animator.start();
        progressIndicator.setProgress(progressFinalValue, true);

        if (progressFinalValue == 100) {
            int from = ContextCompat.getColor(root.getContext(), R.color.light_gray);
            int to = ContextCompat.getColor(root.getContext(), R.color.bright_green);

            ValueAnimator anim = new ValueAnimator();
            anim.setIntValues(from, to);
            anim.setEvaluator(new ArgbEvaluator());
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    progressBackground.getBackground().setColorFilter((Integer)valueAnimator.getAnimatedValue(), PorterDuff.Mode.SRC_IN);
                }
            });
            anim.setDuration(700);
            anim.setStartDelay(1000);
            anim.start();
        }
    }

    private void changeFragment(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_to_bottom)
                .replace(R.id.businessFragmentContainer, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
        MainActivity.basePage++;
    }

    private void onLicenseClicked() {
        licenseButton.setOnClickListener(view -> {
            Fragment fragment = new BusinessExistingBusinessLicenseFragment();
            changeFragment(fragment);
        });
    }

    private void onFinancialDetailsClicked() {
        financialDetailsButton.setOnClickListener(view -> {
            Fragment fragment = new BusinessExistingBusinessFinancialDetailsFragment();
            changeFragment(fragment);
        });
    }


    /**
     *
     * this is for prototype usage only
     *
     * */

    // go back to previous phase
    private void onPrevPhaseClicked() {
        regretButton.setOnClickListener(view -> {
            if (phaseNum > 1 && MainActivity.businessDetails.type.equals("1")) {
                if (phaseNum == 2) cheated = false;
                phaseNum--;
                Fragment fragment = new BusinessExistingBusinessFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.slide_out_to_left)
                        .replace(R.id.businessFragmentContainer, fragment)
                        .commit();
            }
            else if (phaseNum > 2) {
                if (phaseNum == 3) cheated = false;
                phaseNum--;
                Fragment fragment = new BusinessExistingBusinessFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.slide_out_to_left)
                        .replace(R.id.businessFragmentContainer, fragment)
                        .commit();
            }
            else {
                makeToast("Minimum phase reached!");
            }
        });
    }

    // the fast forward section
    private void onFastForwardClicked() {
        cheatingButton.setOnClickListener(view -> {
            if (phaseNum < 4) {
                phaseNum++;
                cheated = true;
                Fragment fragment = new BusinessExistingBusinessFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_left, R.anim.fade_out)
                        .replace(R.id.businessFragmentContainer, fragment)
                        .commit();
            }
            else {
                makeToast("Maximum phase reached!");
            }
        });

    }

    private void makeToast(String msg) {
        Toast toast = Toast.makeText(root.getContext(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 850);
        View view = toast.getView();
        int color = ContextCompat.getColor(root.getContext(), R.color.pastel_orange);
        view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        TextView toastText = view.findViewById(android.R.id.message);
        toastText.setTextColor(ContextCompat.getColor(root.getContext(), android.R.color.holo_red_light));
        toast.show();
    }

}
