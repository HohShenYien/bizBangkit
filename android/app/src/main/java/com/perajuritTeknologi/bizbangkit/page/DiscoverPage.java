package com.perajuritTeknologi.bizbangkit.page;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.perajuritTeknologi.bizbangkit.APICaller;
import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.Utils;
import com.perajuritTeknologi.bizbangkit.event.EnterBusinessDetail;
import com.perajuritTeknologi.bizbangkit.event.GetBusinessDetails;
import com.perajuritTeknologi.bizbangkit.event.ImageEvent;
import com.perajuritTeknologi.bizbangkit.event.ProfileScrolled;
import com.perajuritTeknologi.bizbangkit.event.ReturnToBusinessPage;
import com.perajuritTeknologi.bizbangkit.ui.discover.CardStyleFragment;
import com.perajuritTeknologi.bizbangkit.ui.discover.DetailPageFragment;
import com.perajuritTeknologi.bizbangkit.ui.discover.ListStyleFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class DiscoverPage extends Fragment {
    private View root;
    private MaterialButton filterBtn;
    private FragmentContainerView fragmentContainer;
    private MaterialToolbar toolbar;
    private ImageButton changeViewBtn;
    private boolean isCardView;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Fragment currentMainFragment, cardFragment, listFragment;
    private DataStructure.BusinessProfileDetails curBusiness;

    public static int businessDetailId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_discoverpage, container, false);
        isCardView = true;

        setUpComponents();
        setUpBtnHint();
        setUpFragmentManager();

        setUpFragments();
        setDefaultFragment();
        setUpFragmentChangeButton();

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
        filterBtn = root.findViewById(R.id.discover_filter_button);
        changeViewBtn = root.findViewById(R.id.changeViewButton);
        fragmentContainer = root.findViewById(R.id.discover_fragment_container);
        toolbar = root.findViewById(R.id.discover_top_bar);
    }

    private void setUpBtnHint() {
        TooltipCompat.setTooltipText(filterBtn, "Filter businesses");
        TooltipCompat.setTooltipText(changeViewBtn, "Change view");
    }

    private void setDefaultFragment() {
        currentMainFragment = cardFragment;
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.discover_fragment_container,
                currentMainFragment).commit();
        changeViewBtn.setImageResource(R.drawable.list_ic);
    }

    private void setUpFragments() {
        cardFragment = new CardStyleFragment();
        listFragment = new ListStyleFragment();
    }

    private void setUpFragmentManager() {
        fragmentManager = getFragmentManager();
    }

    private void setUpFragmentChangeButton() {
        changeViewBtn.setOnClickListener(v -> {
            transaction = fragmentManager.beginTransaction();
            if (isCardView) {
                currentMainFragment = listFragment;
                changeViewBtn.setImageResource(R.drawable.ic_red_card);
            } else {
                currentMainFragment = cardFragment;
                changeViewBtn.setImageResource(R.drawable.list_ic);
            }
            isCardView = !isCardView;
            transaction.replace(R.id.discover_fragment_container,
                    currentMainFragment).commit();
        });
    }

    private void enterDetailFragment(DataStructure.BusinessProfileDetails details) {
        transaction = fragmentManager.beginTransaction();
        Fragment newFragment = new DetailPageFragment(details);
        transaction.replace(R.id.discover_fragment_container,
                newFragment).commit();
    }

    private void backToHere() {
        transaction = fragmentManager.beginTransaction();
        toolbar.setVisibility(View.VISIBLE);
        transaction.replace(R.id.discover_fragment_container,
                currentMainFragment).commit();
    }

    @Subscribe
    public void enterBusiness(EnterBusinessDetail event) {
        businessDetailId = event.id;
        transaction = fragmentManager.beginTransaction();
        APICaller.getBusinessDetails(businessDetailId);
        toolbar.setVisibility(View.GONE);
        Fragment loadingPage = new Utils.LoadingPage();
        transaction.replace(R.id.discover_fragment_container, loadingPage).commit();
    }

    @Subscribe
    public void backFromBusiness(ReturnToBusinessPage event) {
        backToHere();
    }

    @Subscribe
    public void receivedBusinessInformation(GetBusinessDetails event) {
        if (event.details.logoPath != null && event.details.logoPath.startsWith("./pictures")) {
            curBusiness = event.details;
            APICaller.getImg(event.details.logoPath, "P01", "DetailsLogo");
        } else {
            enterDetailFragment(event.details);
        }
    }

    @Subscribe
    public void getBusinessLogo(ImageEvent event) {
        if (event.event_id.compareTo("P01") == 0) {
            curBusiness.logo = event.image.image;
            enterDetailFragment(curBusiness);
        }
    }
}
