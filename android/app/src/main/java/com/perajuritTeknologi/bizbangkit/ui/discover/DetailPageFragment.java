package com.perajuritTeknologi.bizbangkit.ui.discover;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.perajuritTeknologi.bizbangkit.APICaller;
import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.MainActivity;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.event.EnterBusinessDetail;
import com.perajuritTeknologi.bizbangkit.event.InvestEvent;
import com.perajuritTeknologi.bizbangkit.event.ReturnToBusinessPage;
import com.perajuritTeknologi.bizbangkit.page.DiscoverPage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class DetailPageFragment extends Fragment {
    private View root;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private BusinessDetailTabAdapter adapter;
    private ImageButton backBtn;
    private ImageView logo;
    private TextView title, phase, percent;
    private ProgressBar progressBar;
    private boolean invested;
    private MaterialButton investBtn;
    private DataStructure.BusinessProfileDetails details;
    private ArrayList<DataStructure.Investor> investors;
    private MaterialAlertDialogBuilder dialog;

    public DetailPageFragment(DataStructure.BusinessProfileDetails details, ArrayList<DataStructure.Investor> investors) {
        this.details = details;
        this.investors = investors;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_discover_business_detail, container, false);
        Log.d("ShenYien", DiscoverPage.businessDetailId + "-----");

        setUpComponents();
        checkInvested();
        setUpAdapter();
        setUpTabs();
        setUpBackBtn();

        createDialog();
        setInvestBtn();

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
        investBtn = root.findViewById(R.id.investBtn);

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
        adapter = new BusinessDetailTabAdapter(this, details, investors);
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

    private void checkInvested() {
        invested = false;
        for (DataStructure.Investor investor: investors) {
            if (investor.userId == Integer.parseInt(((MainActivity)getActivity()).userProfile.userId)) {
                invested = true;
                disableBtn();
                break;
            }
        }
    }

    private void disableBtn() {
        if (invested) {
            investBtn.setStrokeColorResource(R.color.dark_gray);
            investBtn.setText("Invested");
            investBtn.setTextColor(getResources().getColor(R.color.dark_gray));
            investBtn.setEnabled(false);
        }

    }

    private void setInvestBtn() {
        investBtn.setOnClickListener(v-> {
            dialog.show();
        });
    }

    private void createDialog() {
        String messageString = "Investing for phase " + details.phase +
                ": " + (details.phase == 2 ? "5%" : "8%") + "\n" +
                "Amount: RM" + ((details.phase == 2 ? 5 : 8) * Integer.parseInt(details.valuation) / 100);
        dialog = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Invest")
                .setMessage(messageString)
                .setNeutralButton("Cancel", (dialog1, which) -> dialog1.dismiss())
                .setPositiveButton("Invest", (dialog1, which) -> {
                    invest();
                    dialog1.dismiss();
                    }
                );
    }

    private void invest() {
        if (((details.phase == 2 ? 5 : 8) * Integer.parseInt(details.valuation) / 100) >
                Float.parseFloat(MainActivity.eWalletBalance.balance)) {
            showToast("Insufficient money! Please reload and try again", R.color.warning);
        } else {
            APICaller.invest(((MainActivity)getActivity()).userProfile.userId, details.businessId, details.phase);
        }

    }

    private void showToast(String info, int colorId) {
        Toast toast = Toast.makeText(getContext(), info, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 850);
        View view = toast.getView();
        int color = ContextCompat.getColor(getContext(), colorId);
        view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        TextView toastText = view.findViewById(android.R.id.message);
        toastText.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        toast.show();
    }

    @Subscribe
    public void invested(InvestEvent event) {
        if (event.sucessOrNot) {
            showToast("Invested successfully!", R.color.nice_green);
            ((MainActivity)getActivity()).getWalletBalance();
            EventBus.getDefault().post(new EnterBusinessDetail(details.businessId));
        } else {
            showToast("Something went wrong... Please try again", R.color.warning);
        }
    }
}
