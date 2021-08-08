package com.perajuritTeknologi.bizbangkit.page;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.event.ProfileScrolled;
import com.perajuritTeknologi.bizbangkit.ui.profiles.ProfileAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ProfilePage extends Fragment {
    private View root;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    ProfileAdapter profileAdapter;
    Guideline guidelineImg, guidelineTxt;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_profilepage, container, false);

        setUpComponents();
        setUpAdapter();
        setUpTabs();
        setUpGuideLines();

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
        tabLayout = root.findViewById(R.id.tab_layout);
        viewPager = root.findViewById(R.id.pager);
        guidelineImg = root.findViewById(R.id.guideline4);
        guidelineTxt = root.findViewById(R.id.guideline5);
    }

    private void setUpTabs() {
        String[] tabNames = {"Profile", "Details"};
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabNames[position])
        ).attach();
    }

    private void setUpGuideLines() {
        guidelineImg.setGuidelineBegin(325);
        guidelineTxt.setGuidelineBegin(500);
    }

    private void setUpAdapter() {
        profileAdapter = new ProfileAdapter(this);
        viewPager.setAdapter(profileAdapter);
    }

    @Subscribe
    public void handleScroll(ProfileScrolled event) {
        shrinkComponent(event.scrollPosition);
    }

    private void shrinkComponent(int scrollY) {
        int margin = scrollY * 175 / 300;
        guidelineImg.setGuidelineBegin(325 - margin);
        guidelineTxt.setGuidelineBegin(500 - margin);

    }
}
