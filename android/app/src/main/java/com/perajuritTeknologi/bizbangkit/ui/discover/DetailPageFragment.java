package com.perajuritTeknologi.bizbangkit.ui.discover;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.event.EnterBusinessDetail;
import com.perajuritTeknologi.bizbangkit.event.ReturnToBusinessPage;
import com.perajuritTeknologi.bizbangkit.page.cmDiscoverPage;

import org.greenrobot.eventbus.EventBus;

public class DetailPageFragment extends Fragment {
    private View root;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private BusinessDetailTabAdapter adapter;
    private ImageButton backBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_discover_business_detail, container, false);
        Log.d("ShenYien", DiscoverPage.businessDetailId + "-----");

        setUpComponents();
        setUpAdapter();
        setUpTabs();
        setUpBackBtn();

        return root;
    }

    private void setUpComponents() {
        tabLayout = root.findViewById(R.id.business_detail_tab_layout);
        viewPager = root.findViewById(R.id.business_detail_pager);
        backBtn = root.findViewById(R.id.business_detail_back_btn);
    }

    private void setUpAdapter() {
        adapter = new BusinessDetailTabAdapter(this);
        viewPager.setAdapter(adapter);
    }

    private void setUpTabs() {
        String[] tabNames = {"About", "Finance", "Investors"};
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabNames[position])
        ).attach();
    }

    private void setUpBackBtn() {
        backBtn.setOnClickListener(v -> {
            EventBus.getDefault().post(new ReturnToBusinessPage());
        });
    }
}
