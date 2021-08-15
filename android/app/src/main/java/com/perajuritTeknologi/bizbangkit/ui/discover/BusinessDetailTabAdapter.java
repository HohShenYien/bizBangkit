package com.perajuritTeknologi.bizbangkit.ui.discover;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.perajuritTeknologi.bizbangkit.ui.profiles.ProfileAboutFragment;
import com.perajuritTeknologi.bizbangkit.ui.profiles.ProfileDetailsFragment;

public class BusinessDetailTabAdapter extends FragmentStateAdapter {
    public BusinessDetailTabAdapter(@NonNull Fragment fragment) {
        super(fragment);
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
                fragment = new BusinessDetailInvestors();
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
