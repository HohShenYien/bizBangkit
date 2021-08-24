package com.perajuritTeknologi.bizbangkit.ui.profiles;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.perajuritTeknologi.bizbangkit.event.ProfileTabChanged;

import org.greenrobot.eventbus.EventBus;

public class ProfileAdapter extends FragmentStateAdapter {

    public ProfileAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new ProfileAboutFragment();
                break;
            case 1:
                fragment = new ProfileDetailsFragment();
                break;
            default:
                fragment = new ProfileAboutFragment();
        }

        return fragment;

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
