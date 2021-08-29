package com.perajuritTeknologi.bizbangkit.ui.discover;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.event.EnterBusinessDetail;
import com.perajuritTeknologi.bizbangkit.event.ReturnToBusinessPage;
import com.perajuritTeknologi.bizbangkit.page.DiscoverPage;

import org.greenrobot.eventbus.EventBus;

public class DetailPageFragment extends Fragment {
    private View root;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private BusinessDetailTabAdapter adapter;
    private ImageButton backBtn;
    private ImageView logo;
    private TextView title, phase, percent;
    private ProgressBar progressBar;
    private DataStructure.BusinessProfileDetails details;

    public DetailPageFragment(DataStructure.BusinessProfileDetails details) {
        this.details = details;
    }

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
        logo = root.findViewById(R.id.discover_business_detail_img);
        title = root.findViewById(R.id.discover_details_title);
        phase = root.findViewById(R.id.discover_business_detail_phase);
        percent = root.findViewById(R.id.discover_business_detail_percent);
        progressBar = root.findViewById(R.id.discover_business_detail_progress);

        int valuation = Integer.parseInt(details.valuation);
        int totalNeededInThisPhase = (int) (details.phase == 1 ? 0.08 * valuation :
                0.4 * valuation);
        int gathered = (int) (details.purchasedPercent / 100f * valuation);
        int progress = gathered * 100 / totalNeededInThisPhase;

        title.setText(details.name);
        if (details.logo == null) {
            logo.setImageResource(R.drawable.default_business_logo);
        } else {
            logo.setImageBitmap(details.logo);
        }
        phase.setText("Phase " + details.phase + ": RM" + gathered + " / RM" + totalNeededInThisPhase);
        progressBar.setProgress(progress);
        percent.setText(progress + "%");
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
