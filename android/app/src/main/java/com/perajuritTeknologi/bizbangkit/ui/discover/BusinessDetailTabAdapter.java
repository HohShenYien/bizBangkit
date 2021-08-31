package com.perajuritTeknologi.bizbangkit.ui.discover;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.ui.profiles.ProfileAboutFragment;
import com.perajuritTeknologi.bizbangkit.ui.profiles.ProfileDetailsFragment;

import java.util.ArrayList;

public class BusinessDetailTabAdapter extends FragmentStateAdapter {
    private DataStructure.BusinessProfileDetails details;
    private ArrayList<DataStructure.Investor> investors;

    public BusinessDetailTabAdapter(@NonNull Fragment fragment, DataStructure.BusinessProfileDetails details,
                                    ArrayList<DataStructure.Investor> investors) {
        super(fragment);
        this.details = details;
        this.investors = investors;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new BusinessDetailAbout();
                break;
            case 1:
                fragment = new BusinessDetailFinance();
                break;
            case 2:
                fragment = new BusinessDetailInvestors(investors, details);
                break;
            default:
                fragment = new BusinessDetailAbout();
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
